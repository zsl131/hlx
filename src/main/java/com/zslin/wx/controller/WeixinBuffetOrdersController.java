package com.zslin.wx.controller;

import com.zslin.basic.exception.SystemException;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.repository.SimpleSpecificationBuilder;
import com.zslin.basic.tools.NormalTools;
import com.zslin.client.model.Orders;
import com.zslin.client.service.IOrdersService;
import com.zslin.client.tools.ClientFileTools;
import com.zslin.client.tools.ClientJsonTools;
import com.zslin.web.model.Account;
import com.zslin.web.model.BuffetOrder;
import com.zslin.web.service.*;
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

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/17 8:48.
 */
@Controller
@RequestMapping(value = "wx/buffetOrders")
public class WeixinBuffetOrdersController {

    @Autowired
    private IAccountService accountService;

    @Autowired
    private ClientFileTools clientFileTools;

    @Autowired
    private IPriceService priceService;

    @Autowired
    private IRulesService rulesService;

    @Autowired
    private IWalletService walletService;

    @Autowired
    private IBuffetOrderService buffetOrderService;

    /** 买票 */
    @GetMapping(value = "buy")
    public String buy(Model model, String type, HttpServletRequest request) {
        String openid = SessionTools.getOpenid(request);
        boolean isDinner = "2".equals(type)?false:true; //type为2时表示买早餐票，否则是晚餐票
        Account a = accountService.findByOpenid(openid);
        if(a==null) {
            throw new SystemException("用户不存在，不可买票！");
        }
        model.addAttribute("account", a);
        model.addAttribute("isDinner", isDinner);
        model.addAttribute("rules", rulesService.findByStoreSn());
        model.addAttribute("price", priceService.findByStoreSn());
        model.addAttribute("wallet", walletService.findByOpenid(openid));
        return "weixin/buffetOrders/buy";
    }

    /** 管理员的列表，用于有友情价权限的用户列表（股东） */
    @GetMapping(value = "listAdmin")
    public String listAdmin(Model model, Integer page, HttpServletRequest request) {
        String openid = SessionTools.getOpenid(request);
        String phone = accountService.findPhoneByOpenid(openid);
        if(phone!=null && !"".equalsIgnoreCase(phone)) {
            SimpleSpecificationBuilder builder = new SimpleSpecificationBuilder("discountReason", "eq", phone);
            builder.add("discountType", "eq", "2").add("type", "eq", "4");
            Page<BuffetOrder> datas = buffetOrderService.findAll(builder.generate(), SimplePageBuilder.generate(page,
                    SimpleSortBuilder.generateSort("id_d")));
            model.addAttribute("datas", datas);
        }
        return "weixin/buffetOrders/listAdmin";
    }

    /** 显示订单，用于处理美团 */
    @GetMapping(value = "show")
    public String show(Model model, String no, HttpServletRequest request) {
        String openid = SessionTools.getOpenid(request);
        BuffetOrder orders = buffetOrderService.findByNo(no);
        Account a = accountService.findByOpenid(openid);
        model.addAttribute("orders", orders);
        model.addAttribute("account", a);
        return "weixin/buffetOrders/show";
    }

    /** 显示订单，用于处理美团 */
    @PostMapping(value = "checkMeituan")
    public @ResponseBody String checkMeituan(String no, String res, HttpServletRequest request) {
        String openid = SessionTools.getOpenid(request);
        BuffetOrder orders = buffetOrderService.findByNo(no);
        Account a = accountService.findByOpenid(openid);
        if(a.getType().equals(AccountTools.ADMIN)) {
            //只有在就餐状态才需要修改
            if(orders.getStatus().equals("2")) {
                orders.setStatus("3");
                buffetOrderService.save(orders);

                //TODO 通知客户端
                sendOrders2Client(orders);
            }
        }
        return "1";
    }

    /** 用于管理员确认有友情价订单 */
    @GetMapping(value = "confirmFriend")
    public String confirmFriend(Model model, String no, HttpServletRequest request) {
        String openid = SessionTools.getOpenid(request);
        BuffetOrder orders = buffetOrderService.findByNo(no);
        Account a = accountService.findByOpenid(openid);
        model.addAttribute("orders", orders);
        model.addAttribute("account", a);
        //TODO 只有orders.discountReason与account.phone相等才显示确认操作按钮
        return "weixin/buffetOrders/confirmFriend";
    }

    /** 用于管理员确认是否有友情价订单 */
    @PostMapping(value = "confirmFriend")
    public @ResponseBody String confirmFriend(String no, String res, HttpServletRequest request) {
        String openid = SessionTools.getOpenid(request);
        BuffetOrder orders = buffetOrderService.findByNo(no);
        Account a = accountService.findByOpenid(openid);
        if(orders.getDiscountReason().equalsIgnoreCase(a.getPhone())) {
            //res为1表示确认通过；res为0表示驳回
            if("1".equalsIgnoreCase(res)) {
                orders.setStatus("6"); //确认通过
                orders.setDiscountType("2");
            } else {
                orders.setStatus("-3"); //被老板驳回
                orders.setEndLong(System.currentTimeMillis());
                orders.setEndTime(NormalTools.curDate());
            }
            buffetOrderService.save(orders);

            //TODO 通知客户端
            sendOrders2Client(orders);
        }
        return "1";
    }

    private void sendOrders2Client(BuffetOrder orders) {
        String content = ClientJsonTools.buildDataJson(ClientJsonTools.buildBuffetOrders(orders));
        clientFileTools.setChangeContext(content, true);
    }
}
