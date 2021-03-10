package com.zslin.admin.controller;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.annotations.Token;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.tools.MyBeanUtils;
import com.zslin.basic.tools.TokenTools;
import com.zslin.basic.utils.ParamFilterUtil;
import com.zslin.client.tools.ClientFileTools;
import com.zslin.client.tools.ClientJsonTools;
import com.zslin.multi.dao.IStoreDao;
import com.zslin.multi.model.Store;
import com.zslin.web.model.Rules;
import com.zslin.web.service.IRulesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by 钟述林 393156105@qq.com on 2017/1/25 23:46.
 */
@Controller
@RequestMapping(value = "admin/rules")
@AdminAuth(name = "全局配置管理", psn = "应用管理", orderNum = 12, porderNum = 1, pentity = 0, icon = "fa fa-cog")
public class AdminRulesController {

    @Autowired
    private IRulesService rulesService;

    @Autowired
    private ClientFileTools clientFileTools;

    @Autowired
    private IStoreDao storeDao;

    /*@AdminAuth(name="全局配置管理", orderNum=1, icon="fa fa-cog", type="1")
    @RequestMapping(value="index", method= RequestMethod.GET)
    public String index(Model model, HttpServletRequest request) {
        Rules rules = rulesService.loadOne();
        if(rules==null) {rules = new Rules();}
        model.addAttribute("rules", rules);
        return "admin/rules/index";
    }

    @RequestMapping(value="index", method=RequestMethod.POST)
    public String index(Model model, Rules rules, HttpServletRequest request) {
        String spe = rules.getSpe();
        if(spe==null || "".equalsIgnoreCase(spe)) {spe = "15:30";}
        spe = spe.replace("：",":");
        rules.setSpe(spe);
        Rules r = rulesService.loadOne();
        if(r==null) {
            rulesService.save(rules);
            send2Client(rules);
        } else {
            MyBeanUtils.copyProperties(rules, r, new String[]{"id"});
            rulesService.save(r);
            send2Client(r);
        }

        return "redirect:/admin/rules/index";
    }*/

    @GetMapping(value = "list")
    @AdminAuth(name = "多店全局配置", type = "1", orderNum = 1, icon = "fa fa-cog")
    public String list(Model model, Integer page, HttpServletRequest request) {
        Page<Rules> datas = rulesService.findAll(ParamFilterUtil.getInstance().buildSearch(model, request),
                SimplePageBuilder.generate(page));
        model.addAttribute("datas", datas);
        return "admin/rules/list";
    }

    @RequestMapping(value="index", method= RequestMethod.GET)
    public String index(Model model, HttpServletRequest request) {
        return "redirect:/admin/rules/list";
    }

    @Token(flag= Token.READY)
    @AdminAuth(name = "添加全局配置", orderNum = 2, icon="fa fa-plus")
    @RequestMapping(value="add", method= RequestMethod.GET)
    public String add(Model model, HttpServletRequest request) {
        model.addAttribute("rules", new Rules());

        List<Store> storeList = storeDao.findAll();
        model.addAttribute("storeList", storeList);
        return "admin/rules/add";
    }

    @Token(flag= Token.CHECK)
    @RequestMapping(value="add", method=RequestMethod.POST)
    public String add(Model model, Rules rules, HttpServletRequest request) {
        if(TokenTools.isNoRepeat(request)) { //不是重复提交
            rulesService.save(rules);

            send2Client(rules);
        }
        return "redirect:/admin/rules/list";
    }

    @Token(flag= Token.READY)
    @AdminAuth(name="修改全局配置", orderNum=3, icon = "fa fa-pencil")
    @RequestMapping(value="update/{id}", method=RequestMethod.GET)
    public String update(Model model, @PathVariable Integer id, HttpServletRequest request) {
        Rules r = rulesService.findOne(id);
        model.addAttribute("rules", r);
        return "admin/rules/update";
    }

    @Token(flag= Token.CHECK)
    @RequestMapping(value="update/{id}", method=RequestMethod.POST)
    public String update(Model model, @PathVariable Integer id, Rules rules, HttpServletRequest request) {
        if(TokenTools.isNoRepeat(request)) {
            Rules r = rulesService.findOne(id);
            MyBeanUtils.copyProperties(rules, r, "storeSn", "storeName", "storeId");
            rulesService.save(r);

            send2Client(r);
        }
        return "redirect:/admin/rules/list";
    }

    private void send2Client(Rules r) {
        String json = ClientJsonTools.buildDataJson(ClientJsonTools.buildRules(r));
        clientFileTools.setChangeContext(r.getStoreSn(), json, true);
    }
}
