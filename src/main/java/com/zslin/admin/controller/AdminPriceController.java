package com.zslin.admin.controller;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.tools.MyBeanUtils;
import com.zslin.client.tools.ClientFileTools;
import com.zslin.client.tools.ClientJsonTools;
import com.zslin.web.model.Price;
import com.zslin.web.service.IPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 钟述林 393156105@qq.com on 2017/1/25 23:46.
 */
@Controller
@RequestMapping(value = "admin/price")
@AdminAuth(name = "价格管理", psn = "会员管理", orderNum = 12, porderNum = 1, pentity = 0, icon = "fa fa-rmb")
public class AdminPriceController {

    @Autowired
    private IPriceService priceService;

    @Autowired
    private ClientFileTools clientFileTools;

    @AdminAuth(name="价格配置管理", orderNum=1, icon="fa fa-rmb", type="1")
    @RequestMapping(value="index", method= RequestMethod.GET)
    public String index(Model model, HttpServletRequest request) {
        Price price = priceService.loadOne();
        if(price==null) {price = new Price();}
        model.addAttribute("price", price);
        return "admin/price/index";
    }

    @RequestMapping(value="index", method=RequestMethod.POST)
    public String index(Model model, Price price, HttpServletRequest request) {

        Price p = priceService.loadOne();
        if(p==null) {
            priceService.save(price);
            send2Client(price);
        } else {
            MyBeanUtils.copyProperties(price, p, new String[]{"id"});
            priceService.save(p);
            send2Client(p);
        }

        request.getSession().setAttribute("price", price); //修改后需要修改一次Session中的值
        return "redirect:/admin/price/index";
    }

    private void send2Client(Price p) {
        String content = ClientJsonTools.buildDataJson(ClientJsonTools.buildPrice(p));
        clientFileTools.setChangeContext(content, true);
    }
}
