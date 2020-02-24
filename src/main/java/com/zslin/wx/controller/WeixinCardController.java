package com.zslin.wx.controller;

import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.tools.NormalTools;
import com.zslin.basic.utils.ParamFilterUtil;
import com.zslin.card.model.*;
import com.zslin.card.service.ICardApplyService;
import com.zslin.card.service.ICardService;
import com.zslin.card.service.ICardUnderService;
import com.zslin.card.service.IGrantCardService;
import com.zslin.card.tools.CardNoTools;
import com.zslin.client.tools.ClientFileTools;
import com.zslin.client.tools.ClientJsonTools;
import com.zslin.kaoqin.model.Worker;
import com.zslin.kaoqin.service.IWorkerService;
import com.zslin.web.model.Account;
import com.zslin.web.service.IAccountService;
import com.zslin.wx.tools.AccountTools;
import com.zslin.wx.tools.SessionTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by zsl on 2018/10/14.
 */
@Controller
@RequestMapping(value = "wx/card")
public class WeixinCardController {

    private static final String REDIRECT = "redirect:/wx/account/me";

    @Autowired
    private ICardService cardService;

    @Autowired
    private IGrantCardService grantCardService;

    @Autowired
    private IWorkerService workerService;

    @Autowired
    private CardNoTools cardNoTools;

    @Autowired
    private IAccountService accountService;

    @Autowired
    private ICardUnderService cardUnderService;

    @Autowired
    private ClientFileTools clientFileTools;

    @Autowired
    private ICardApplyService cardApplyService;

    /** 卡券分配 */
    @GetMapping(value = "allot")
    public String allot(Model model, HttpServletRequest request) {
        String openid = SessionTools.getOpenid(request);
        Account a = accountService.findByOpenid(openid);
        if(a==null || (!AccountTools.ADMIN.equals(a.getType()) && !AccountTools.PARTNER.equals(a.getType()) && !AccountTools.MANAGER.equals(a.getType()))) {
            return REDIRECT;
        }
        List<Worker> workerList = workerService.findCanSendCard();
        model.addAttribute("workerList", workerList);
        return "weixin/card/allot";
    }

    @PostMapping(value = "queryNos")
    public @ResponseBody List<Integer> queryNos(String type, Integer count) {
        return cardNoTools.buildAllotNos(type, count);
    }

    @PostMapping(value = "queryCard2ShopNos")
    public @ResponseBody List<Integer> queryCard2ShopNos(String type, Integer count) {
        return cardNoTools.buildGrantCardNos(type, count);
    }

    @PostMapping(value = "card2Shop")
    public @ResponseBody String card2Shop(String type, Integer count, HttpServletRequest request) {
        String openid = SessionTools.getOpenid(request);
        Account a = accountService.findByOpenid(openid);
        if(a==null || (!AccountTools.ADMIN.equals(a.getType()) && !AccountTools.PARTNER.equals(a.getType()))) {
            return "无权限";
        }

        List<Integer> list =cardNoTools.buildGrantCardNos(type, count);

        String createDay = NormalTools.curDate("yyyyMMdd");
        Integer orderNo = cardNoTools.queryMaxOrderNo(createDay);
        String batchNo = cardNoTools.buildBatchNo(createDay, orderNo);
        for(Integer no : list) {
            GrantCard gc = new GrantCard();
            gc.setStatus("0"); //在店
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
        return "1";
    }

    @PostMapping(value = "setCardUnder")
    public @ResponseBody String setCardUnder(Integer workerId, String type, Integer count, HttpServletRequest request) {
        List<Integer> nos = cardNoTools.buildAllotNos(type, count);
        Worker w = workerService.findOne(workerId);
        for(Integer no : nos) {
            CardUnder cu = new CardUnder();
            cu.setCreateTime(NormalTools.curDate("yyyy-MM-dd HH:mm:ss"));
            cu.setCardNo(no);
            cu.setCardType(type);
            cu.setUnderName(w.getName());
            cu.setUnderKey(w.getPhone());
            cardUnderService.save(cu);

            sendCardUnder2Client(cu);
        }

        for(Integer no : nos) {
            Card c = new Card();
            c.setNo(no);
            c.setStatus("0");
            c.setType(type);
            cardService.save(c);
            //TODO 发送到客户端

            sendCard2Client("addOrUpdate", c);
        }

        grantCardService.updateStatus("1", nos.toArray(new Integer[nos.size()]));

        sendGrantCardStatus2Client("1", nos);
        return "1";
    }

    @GetMapping(value = "listCardApply")
    public String listCardApply(Model model, Integer page, HttpServletRequest request) {
        String openid = SessionTools.getOpenid(request);
        Account a = accountService.findByOpenid(openid);
        if(a==null || (!AccountTools.ADMIN.equals(a.getType()) && !AccountTools.PARTNER.equals(a.getType()) && !AccountTools.MANAGER.equals(a.getType()))) {
            return REDIRECT;
        }
        Page<CardApply> datas = cardApplyService.findAll(ParamFilterUtil.getInstance().buildSearch(model, request),
                SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("id_d")));
        model.addAttribute("datas", datas);
        return "weixin/card/listCardApply";
    }

    @PostMapping(value = "applyCard")
    public @ResponseBody String applyCard(Integer cardNo, String status, HttpServletRequest request) {
        String openid = SessionTools.getOpenid(request);
        Account a = accountService.findByOpenid(openid);
        if(a==null || (!AccountTools.ADMIN.equals(a.getType()) && !AccountTools.PARTNER.equals(a.getType()) && !AccountTools.MANAGER.equals(a.getType()))) {
            return "无权限";
        }
        CardApply ca = cardApplyService.findByCardNo(cardNo);
        if(ca==null) {return "无数据";} else if(!"0".equals(ca.getStatus())) {
            return "不可再次审核";
        } else if(a.getPhone().equalsIgnoreCase(ca.getApplyKey()) && !AccountTools.ADMIN.equals(a.getType())) {
            return "不可审核自己的申请";
        } else {
            ca.setStatus(status);
            ca.setVerifyCreateTime(NormalTools.curDate("yyyy-MM-dd HH:mm:ss"));
            ca.setVerifyKey(a.getPhone());
            ca.setVerifyName(a.getName());
            cardApplyService.save(ca);
            sendCardApply2Client(ca);

            if("1".equalsIgnoreCase(status)) {
                Card card = cardService.findByNo(cardNo);
                if(card==null) {card = new Card();}
                card.setStatus("0");
                card.setType(ca.getCardType());
                card.setNo(ca.getCardNo());
                cardService.save(card);
                sendCard2Client("addOrUpdate", card);

                GrantCard gc = grantCardService.findByCardNo(cardNo);
                gc.setStatus("1");
                grantCardService.save(gc);
                sendGrantCard2Client("addOrUpdate", gc);
            }

            return "1";
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

    private void sendCardUnder2Client(CardUnder obj) {
        String content = ClientJsonTools.buildDataJson(ClientJsonTools.buildCardUnder(obj));
        clientFileTools.setChangeContext(content, true);
    }

    private void sendGrantCardStatus2Client(String status, List<Integer> nos) {
        String content = ClientJsonTools.buildDataJson(ClientJsonTools.buildGrantCardStatus(status, nos));
        clientFileTools.setChangeContext(content, true);
    }

    private void sendCardApply2Client(CardApply obj) {
        String content = ClientJsonTools.buildDataJson(ClientJsonTools.buildCardApply(obj));
        clientFileTools.setChangeContext(content, true);
    }
}
