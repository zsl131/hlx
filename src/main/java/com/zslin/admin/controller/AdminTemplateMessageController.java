package com.zslin.admin.controller;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.annotations.Token;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.tools.MyBeanUtils;
import com.zslin.basic.tools.PinyinToolkit;
import com.zslin.basic.tools.TokenTools;
import com.zslin.basic.utils.ParamFilterUtil;
import com.zslin.web.model.TemplateMessage;
import com.zslin.web.service.ITemplateMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 钟述林 393156105@qq.com on 2017/1/26 0:10.
 */
@Controller
@RequestMapping(value = "admin/templateMessage")
@AdminAuth(name = "微信模板消息管理", psn = "微信管理", orderNum = 7, porderNum = 1, pentity = 0, icon = "fa fa-commenting")
public class AdminTemplateMessageController {

    @Autowired
    private ITemplateMessageService templateMessageService;

    @GetMapping(value = "list")
    @AdminAuth(name = "微信模板消息管理", type = "1", orderNum = 1, icon = "fa fa-commenting")
    public String list(Model model, Integer page, HttpServletRequest request) {
        Page<TemplateMessage> datas = templateMessageService.findAll(ParamFilterUtil.getInstance().buildSearch(model, request),
                SimplePageBuilder.generate(page));
        model.addAttribute("datas", datas);
        return "admin/templateMessage/list";
    }

    @Token(flag= Token.READY)
    @AdminAuth(name = "添加微信模板消息", orderNum = 2, icon="fa fa-plus")
    @RequestMapping(value="add", method= RequestMethod.GET)
    public String add(Model model, HttpServletRequest request) {
        TemplateMessage tm = new TemplateMessage();
        model.addAttribute("templateMessage", tm);
        return "admin/templateMessage/add";
    }

    @Token(flag= Token.CHECK)
    @RequestMapping(value="add", method=RequestMethod.POST)
    public String add(Model model, TemplateMessage templateMessage, HttpServletRequest request) {
        if(TokenTools.isNoRepeat(request)) { //不是重复提交
            templateMessage.setCode(PinyinToolkit.cn2Spell(templateMessage.getTitle(), ""));
            templateMessageService.save(templateMessage);
        }
        return "redirect:/admin/templateMessage/list";
    }

    @Token(flag= Token.READY)
    @AdminAuth(name="修改微信模板消息", orderNum=3, icon = "fa fa-pencil")
    @RequestMapping(value="update/{id}", method=RequestMethod.GET)
    public String update(Model model, @PathVariable Integer id, HttpServletRequest request) {
        TemplateMessage tm = templateMessageService.findOne(id);
        model.addAttribute("templateMessage", tm);
        return "admin/templateMessage/update";
    }

    @Token(flag= Token.CHECK)
    @RequestMapping(value="update/{id}", method=RequestMethod.POST)
    public String update(Model model, @PathVariable Integer id, TemplateMessage templateMessage, HttpServletRequest request) {
//		Boolean isRepeat = (Boolean) request.getAttribute("isRepeat");
        if(TokenTools.isNoRepeat(request)) {
            TemplateMessage tm = templateMessageService.findOne(id);
            templateMessage.setCode(PinyinToolkit.cn2Spell(templateMessage.getTitle(), ""));
            MyBeanUtils.copyProperties(templateMessage, tm, new String[]{"id"});
            templateMessageService.save(tm);
        }
        return "redirect:/admin/templateMessage/list";
    }

    @AdminAuth(name="删除微信模板消息", orderNum=4, icon = "fa fa-remove")
    @RequestMapping(value="delete/{id}", method=RequestMethod.POST)
    public @ResponseBody
    String delete(@PathVariable Integer id) {
        try {
            templateMessageService.delete(id);
            return "1";
        } catch (Exception e) {
            return "0";
        }
    }
}
