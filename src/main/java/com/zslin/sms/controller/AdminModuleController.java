package com.zslin.sms.controller;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.annotations.Token;
import com.zslin.basic.exception.SystemException;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.tools.MyBeanUtils;
import com.zslin.basic.tools.TokenTools;
import com.zslin.basic.utils.ParamFilterUtil;
import com.zslin.sms.model.Module;
import com.zslin.sms.service.IModuleService;
import com.zslin.sms.tools.SmsTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/14 10:24.
 */
@Controller
@RequestMapping(value = "admin/module")
@AdminAuth(name = "短信模板管理", psn = "短信管理", orderNum = 1, porderNum = 1, pentity = 0, icon = "fa fa-envelope-open")
public class AdminModuleController {

    @Autowired
    private IModuleService moduleService;

    @Autowired
    private SmsTools smsTools;

    @GetMapping(value = "list")
    @AdminAuth(name = "短信模板列表", orderNum = 1, type = "1", icon = "fa fa-envelope-open")
    public String list(Model model, Integer page, HttpServletRequest request) {
        Page<Module> datas = moduleService.findAll(ParamFilterUtil.getInstance().buildSearch(model, request),
                SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("id_d")));
        model.addAttribute("datas", datas);
        return "admin/module/list";
    }

    @Token(flag= Token.READY)
    @AdminAuth(name = "添加短信模板", orderNum = 2, icon="fa fa-plus")
    @RequestMapping(value="add", method= RequestMethod.GET)
    public String add(Model model, HttpServletRequest request) {
        Module module = new Module();
        model.addAttribute("module", module);
        return "admin/module/add";
    }

    @Token(flag= Token.CHECK)
    @RequestMapping(value="add", method=RequestMethod.POST)
    public String add(Model model, Module module, HttpServletRequest request) {
        if(TokenTools.isNoRepeat(request)) { //不是重复提交
            try {
                Integer iid = smsTools.addModule(module.getSign(), module.getContent());
                module.setIid(iid);
            } catch (SystemException e) {
                String code = e.getCode();
                String msg = e.getMessage();
//                module.setStatus(code);
                module.setStatus("1".equals(code)?"0":code);
                module.setReason(msg);
            } finally {
            }

            moduleService.save(module);
        }
        return "redirect:/admin/module/list";
    }

    @Token(flag= Token.READY)
    @AdminAuth(name="修改短信模板", orderNum=3, icon = "fa fa-pencil")
    @RequestMapping(value="update/{id}", method=RequestMethod.GET)
    public String update(Model model, @PathVariable Integer id, HttpServletRequest request) {
        Module m = moduleService.findOne(id);
        model.addAttribute("module", m);
        return "admin/module/update";
    }

    @Token(flag= Token.CHECK)
    @RequestMapping(value="update/{id}", method=RequestMethod.POST)
    public String update(Model model, @PathVariable Integer id, Module module, HttpServletRequest request) {
        if(TokenTools.isNoRepeat(request)) {
            Module m = moduleService.findOne(id);
            MyBeanUtils.copyProperties(module, m, new String[]{"id"});
            moduleService.save(m);
        }
        return "redirect:/admin/module/list";
    }

    @AdminAuth(name="删除短信模板", orderNum=4, icon = "fa fa-remove")
    @RequestMapping(value="delete/{id}", method=RequestMethod.POST)
    public @ResponseBody
    String delete(@PathVariable Integer id) {
        try {
            Module module = moduleService.findOne(id);
            if(module.getIid()!=null && module.getIid()>0) {
                smsTools.deleteModule(module.getIid()); //同时删除接口中心的短信模板
            }
            moduleService.delete(id);
            return "1";
        } catch (Exception e) {
            return "1";
        }
    }

    @AdminAuth(name="同步短信模板", orderNum=4, icon = "fa fa-inbox")
    @RequestMapping(value="synch", method=RequestMethod.POST)
    public @ResponseBody String synch() {
        try {
            smsTools.listModules(); //获取列表的同时进行同步操作
            return "1";
        } catch (Exception e) {
            return "0";
        }
    }

    @AdminAuth(name="提交短信模板到中心", orderNum=4, icon = "fa fa-share")
    @RequestMapping(value="send/{id}", method=RequestMethod.POST)
    public @ResponseBody
    String send(@PathVariable Integer id) {
        Module m = moduleService.findOne(id);
        String res = "1";
        if(!"1".equals(m.getStatus())) {
            try {
                Integer iid = smsTools.addModule(m.getSign(), m.getContent());
                m.setIid(iid);
            } catch (SystemException e) {
                String code = e.getCode();
                String msg = e.getMessage();
                if(!"-3".equals(code)) {
                    m.setStatus("1".equals(code)?"0":code);
                    m.setReason(msg);
                    res = code;
                }
            } finally {
            }
            moduleService.save(m);
        }
        return res;
    }
}
