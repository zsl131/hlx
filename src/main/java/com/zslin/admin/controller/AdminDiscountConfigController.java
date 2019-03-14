package com.zslin.admin.controller;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.tools.MyBeanUtils;
import com.zslin.client.tools.ClientFileTools;
import com.zslin.client.tools.ClientJsonTools;
import com.zslin.web.model.DiscountConfig;
import com.zslin.web.service.IDiscountConfigService;
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
@RequestMapping(value = "admin/discountConfig")
@AdminAuth(name = "折扣配置管理", psn = "折扣管理", orderNum = 12, porderNum = 1, pentity = 0, icon = "fa fa-rmb")
public class AdminDiscountConfigController {

    @Autowired
    private IDiscountConfigService discountConfigService;

    @Autowired
    private ClientFileTools clientFileTools;

    @AdminAuth(name="折扣配置管理", orderNum=1, icon="fa fa-rmb", type="1")
    @RequestMapping(value="index", method= RequestMethod.GET)
    public String index(Model model, HttpServletRequest request) {
        DiscountConfig config = discountConfigService.loadOne();
        if(config==null) {config = new DiscountConfig();}
        model.addAttribute("config", config);
        return "admin/discountConfig/index";
    }

    @RequestMapping(value="index", method=RequestMethod.POST)
    public String index(Model model, DiscountConfig config, HttpServletRequest request) {

        DiscountConfig dc = discountConfigService.loadOne();
        if(dc==null) {
            discountConfigService.save(config);
            send2Client(config);
        } else {
            MyBeanUtils.copyProperties(config, dc, new String[]{"id"});
            discountConfigService.save(dc);
            send2Client(dc);
        }

        request.getSession().setAttribute("config", dc); //修改后需要修改一次Session中的值
        return "redirect:/admin/discountConfig/index";
    }

    private void send2Client(DiscountConfig dc) {
        String content = ClientJsonTools.buildDataJson(ClientJsonTools.buildDiscountConfig(dc));
        clientFileTools.setChangeContext(content, true);
    }
}
