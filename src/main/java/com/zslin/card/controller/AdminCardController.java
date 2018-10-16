package com.zslin.card.controller;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.tools.NormalTools;
import com.zslin.basic.utils.ParamFilterUtil;
import com.zslin.card.model.Card;
import com.zslin.card.model.GrantCard;
import com.zslin.card.service.ICardService;
import com.zslin.card.service.IGrantCardService;
import com.zslin.card.tools.CardNoTools;
import com.zslin.client.tools.ClientFileTools;
import com.zslin.client.tools.ClientJsonTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by zsl on 2018/10/13.
 */
@Controller
@RequestMapping(value = "admin/card")
@AdminAuth(name = "代金券管理", psn = "卡券管理", orderNum = 9, porderNum = 10, pentity = 0, icon = "fa fa-credit-card")
public class AdminCardController {

    @Autowired
    private ICardService cardService;

    @Autowired
    private IGrantCardService grantCardService;

    @Autowired
    private CardNoTools cardNoTools;

    @Autowired
    private ClientFileTools clientFileTools;

    @GetMapping(value = "list")
    @AdminAuth(name = "代金券管理", type = "1", orderNum = 1, icon = "fa fa-credit-card")
    public String list(Model model, Integer page, HttpServletRequest request) {
        Page<Card> datas = cardService.findAll(ParamFilterUtil.getInstance().buildSearch(model, request),
                SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("id_d")));
        model.addAttribute("datas", datas);
        return "admin/card/list";
    }

    @AdminAuth(name = "添加代金券", orderNum = 2, icon="fa fa-plus")
    @RequestMapping(value="add", method= RequestMethod.GET)
    public String add() {
        return "admin/card/add";
    }

    @RequestMapping(value="add", method=RequestMethod.POST)
    public String add(HttpServletRequest request) {
        String type = request.getParameter("type"); //类型
        Integer count = Integer.parseInt(request.getParameter("count")); //数量

        List<Integer> list =cardNoTools.buildGrantCardNos(type, count);
        for(Integer no : list) {
            Card c = new Card();
            c.setNo(no);
            c.setStatus("0");
            c.setType(type);
            cardService.save(c);
            //TODO 需要发到客户端

            sendCard2Client("addOrUpdate", c);
        }

        String createDay = NormalTools.curDate("yyyyMMdd");
        Integer orderNo = cardNoTools.queryMaxOrderNo(createDay);
        String batchNo = cardNoTools.buildBatchNo(createDay, orderNo);
        for(Integer no : list) {
            GrantCard gc = new GrantCard();
            gc.setStatus("1");
            gc.setCardType(type);
            gc.setBatchNo(batchNo);
            gc.setCardNo(no);
            gc.setCreateDay(createDay);
            gc.setCreateTime(NormalTools.curDate("yyyy-MM-dd HH:mm:ss"));
            gc.setOrderNo(orderNo);
            grantCardService.save(gc);
            //TODO 需要发到客户端
            sendGrantCard2Client("addOrUpdate", gc);
        }

        return "redirect:/admin/card/list";
    }

    @RequestMapping(value = "queryNos", method = RequestMethod.POST)
    public @ResponseBody List<Integer> queryNos(String type, Integer count) {
        List<Integer> list = cardNoTools.buildGrantCardNos(type, count);
        return list;
    }

    @AdminAuth(name="作废卡券", orderNum=4, icon = "fa fa-remove")
    @RequestMapping(value="abolish/{id}", method= RequestMethod.POST)
    public @ResponseBody
    String abolish(@PathVariable Integer id) {
        try {
            cardService.updateStatus("2", id); //作废
            sendCard2Client("addOrUpdate", cardService.findOne(id));
            return "1";
        } catch (Exception e) {
            return "0";
        }
    }

    private void sendGrantCard2Client(String action, GrantCard obj) {
        String content = ClientJsonTools.buildDataJson(ClientJsonTools.buildGrantCard(action, obj));
        clientFileTools.setChangeContext(content, true);
    }

    private void sendCard2Client(String action, Card obj) {
        String content = ClientJsonTools.buildDataJson(ClientJsonTools.buildCard(action, obj));
        clientFileTools.setChangeContext(content, true);
    }
}
