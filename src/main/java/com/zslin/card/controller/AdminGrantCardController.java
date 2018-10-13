package com.zslin.card.controller;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.tools.NormalTools;
import com.zslin.basic.utils.ParamFilterUtil;
import com.zslin.card.model.GrantCard;
import com.zslin.card.service.IGrantCardService;
import com.zslin.card.tools.CardNoTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by zsl on 2018/10/13.
 */
@Controller
@RequestMapping(value = "admin/grantCard")
@AdminAuth(name = "卡券到店管理", psn = "卡券管理", orderNum = 9, porderNum = 10, pentity = 0, icon = "fa fa-credit-card")
public class AdminGrantCardController {

    @Autowired
    private IGrantCardService grantCardService;

    @Autowired
    private CardNoTools cardNoTools;

    @GetMapping(value = "list")
    @AdminAuth(name = "卡券到店管理", type = "1", orderNum = 1, icon = "fa fa-credit-card")
    public String list(Model model, Integer page, HttpServletRequest request) {
        Page<GrantCard> datas = grantCardService.findAll(ParamFilterUtil.getInstance().buildSearch(model, request),
                SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("id_d")));
        model.addAttribute("datas", datas);
        return "admin/grantCard/list";
    }

    @AdminAuth(name = "卡券到店", orderNum = 2, icon="fa fa-plus")
    @RequestMapping(value="add", method= RequestMethod.GET)
    public String add() {
        return "admin/grantCard/add";
    }

    @RequestMapping(value="add", method=RequestMethod.POST)
    public String add(HttpServletRequest request) {
        String type = request.getParameter("type"); //类型
        Integer count = Integer.parseInt(request.getParameter("count")); //数量

        List<Integer> list =cardNoTools.buildGrantCardNos(type, count);
        String createDay = NormalTools.curDate("yyyyMMdd");
        Integer orderNo = cardNoTools.queryMaxOrderNo(createDay);
        String batchNo = cardNoTools.buildBatchNo(createDay, orderNo);
        for(Integer no : list) {
            GrantCard gc = new GrantCard();
            gc.setStatus("0");
            gc.setCardType(type);
            gc.setBatchNo(batchNo);
            gc.setCardNo(no);
            gc.setCreateDay(createDay);
            gc.setCreateTime(NormalTools.curDate("yyyy-MM-dd HH:mm:ss"));
            gc.setOrderNo(orderNo);
            grantCardService.save(gc);
        }

        return "redirect:/admin/grantCard/list";
    }

    @RequestMapping(value = "queryNos", method = RequestMethod.POST)
    public @ResponseBody List<Integer> queryNos(String type, Integer count) {
        List<Integer> list = cardNoTools.buildGrantCardNos(type, count);
        return list;
    }
}
