package com.zslin.wx.controller;

import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.repository.SimpleSpecificationBuilder;
import com.zslin.client.model.Orders;
import com.zslin.client.service.IOrdersService;
import com.zslin.client.tools.ClientFileTools;
import com.zslin.client.tools.ClientJsonTools;
import com.zslin.web.model.Account;
import com.zslin.web.service.IAccountService;
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
@RequestMapping(value = "wx/orders")
public class WeixinOrdersController {

    @Autowired
    private IOrdersService ordersService;

    @Autowired
    private IAccountService accountService;

    @Autowired
    private ClientFileTools clientFileTools;

    /** 管理员的列表，用于有友情价权限的用户列表（股东） */
    @GetMapping(value = "listAdmin")
    public String listAdmin(Model model, Integer page, HttpServletRequest request) {
        String openid = SessionTools.getOpenid(request);
        String phone = accountService.findPhoneByOpenid(openid);
        if(phone!=null && !"".equalsIgnoreCase(phone)) {
            SimpleSpecificationBuilder builder = new SimpleSpecificationBuilder("discountReason", "eq", phone);
            Page<Orders> datas = ordersService.findAll(builder.generate(), SimplePageBuilder.generate(page,
                    SimpleSortBuilder.generateSort("id_d")));
            model.addAttribute("datas", datas);
        }
        return "weixin/orders/listAdmin";
    }

    /** 用于管理员确认有友情价订单 */
    @GetMapping(value = "confirmFriend")
    public String confirmFriend(Model model, String no, HttpServletRequest request) {
        String openid = SessionTools.getOpenid(request);
        Orders orders = ordersService.findByNo(no);
        Account a = accountService.findByOpenid(openid);
        model.addAttribute("orders", orders);
        model.addAttribute("account", a);
        //TODO 只有orders.discountReason与account.phone相等才显示确认操作按钮
        return "weixin/orders/confirmFriend";
    }

    /** 用于管理员确认是否有友情价订单 */
    @PostMapping(value = "confirmFriend")
    public @ResponseBody String confirmFriend(String no, String res, HttpServletRequest request) {
        String openid = SessionTools.getOpenid(request);
        Orders orders = ordersService.findByNo(no);
        Account a = accountService.findByOpenid(openid);
        if(orders.getDiscountReason().equalsIgnoreCase(a.getPhone())) {
            //res为1表示确认通过；res为0表示驳回
            if("1".equalsIgnoreCase(res)) {
                orders.setStatus("6"); //确认通过
                orders.setDiscountType("2");
            } else {
                orders.setStatus("-3"); //被老板驳回
            }
            ordersService.save(orders);

            //TODO 通知客户端
            sendOrders2Client(orders);
        }
        return "1";
    }

    private void sendOrders2Client(Orders orders) {
        String content = ClientJsonTools.buildDataJson(ClientJsonTools.buildOrders(orders));
        clientFileTools.setChangeContext(content, true);
    }
}
