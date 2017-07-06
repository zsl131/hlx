package com.zslin.admin.controller;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.annotations.Token;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.tools.MyBeanUtils;
import com.zslin.basic.tools.TokenTools;
import com.zslin.basic.utils.ParamFilterUtil;
import com.zslin.client.tools.ClientFileTools;
import com.zslin.client.tools.ClientJsonTools;
import com.zslin.meituan.model.MeituanConfig;
import com.zslin.meituan.model.MeituanShop;
import com.zslin.meituan.service.IMeituanConfigService;
import com.zslin.meituan.service.IMeituanShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 钟述林 393156105@qq.com on 2017/7/5 14:27.
 */
@Controller
@RequestMapping(value = "admin/meituan")
@AdminAuth(name = "美团管理", psn = "美团管理", orderNum = 10, porderNum = 1, pentity = 0, icon = "fa fa-tumblr")
public class AdminMeituanController {

    @Autowired
    private IMeituanConfigService meituanConfigService;

    @Autowired
    private IMeituanShopService meituanShopService;

    @Autowired
    private ClientFileTools clientFileTools;

    @AdminAuth(name="美团配置管理", orderNum=1, icon="fa fa-cog", type="1")
    @RequestMapping(value="config", method= RequestMethod.GET)
    public String index(Model model, HttpServletRequest request) {
        MeituanConfig config = meituanConfigService.loadOne();
        if(config==null) {
            config = new MeituanConfig();
        }
        model.addAttribute("meituanConfig", config);
        return "admin/meituan/config";
    }

    @RequestMapping(value="config", method=RequestMethod.POST)
    public String index(MeituanConfig meituanConfig, HttpServletRequest request) {
        MeituanConfig c = meituanConfigService.loadOne();
        if(c==null) {
            meituanConfigService.save(meituanConfig);
            sendConfig2Client("update", meituanConfig);
        } else {
            MyBeanUtils.copyProperties(meituanConfig, c, new String[]{"id"});
            meituanConfigService.save(c);
            sendConfig2Client("update", c);
        }
        return "redirect:/admin/meituan/config";
    }

    @RequestMapping(value = "listShop", method = RequestMethod.GET)
    @AdminAuth(name="美团门店列表", orderNum=1, icon="fa fa-list", type="1")
    public String listShop(Model model, Integer page, HttpServletRequest request) {
        Page<MeituanShop> datas = meituanShopService.findAll(ParamFilterUtil.getInstance().buildSearch(model, request),
                SimplePageBuilder.generate(page));
        model.addAttribute("datas", datas);
        model.addAttribute("config", meituanConfigService.loadOne());
        return "admin/meituan/listShop";
    }

    @Token(flag= Token.READY)
    @AdminAuth(name = "添加美团门店", orderNum = 2, icon="fa fa-plus")
    @RequestMapping(value="add", method= RequestMethod.GET)
    public String add(Model model, HttpServletRequest request) {
        MeituanShop shop = new MeituanShop();
        model.addAttribute("meituanShop", shop);
        return "admin/meituan/addShop";
    }

    @Token(flag= Token.CHECK)
    @RequestMapping(value="add", method=RequestMethod.POST)
    public String add(Model model, MeituanShop meituanShop, HttpServletRequest request) {
        if(TokenTools.isNoRepeat(request)) { //不是重复提交
            meituanShopService.save(meituanShop);
        }
        return "redirect:/admin/meituan/listShop";
    }

    @AdminAuth(name="删除美团门店", orderNum=4, icon = "fa fa-remove")
    @RequestMapping(value="delete/{id}", method=RequestMethod.POST)
    public @ResponseBody
    String delete(@PathVariable Integer id) {
        try {
            MeituanShop shop = meituanShopService.findOne(id);

            meituanShopService.delete(shop);
            return "1";
        } catch (Exception e) {
            return "0";
        }
    }

    private void sendConfig2Client(String action, MeituanConfig config) {
        String content = ClientJsonTools.buildDataJson(ClientJsonTools.buildMeituanConfig(action, config));
        clientFileTools.setChangeContext(content, true);
    }
}
