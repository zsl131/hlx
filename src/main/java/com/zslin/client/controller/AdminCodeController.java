package com.zslin.client.controller;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.annotations.Token;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.tools.MyBeanUtils;
import com.zslin.basic.tools.TokenTools;
import com.zslin.basic.utils.ParamFilterUtil;
import com.zslin.client.model.Code;
import com.zslin.client.service.ICodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/5 15:03.
 */
@Controller
@RequestMapping(value = "admin/code")
@AdminAuth(name = "接口代码管理", orderNum = 2, porderNum = 1, psn = "接口管理", pentity = 0, icon = "fa fa-columns")
public class AdminCodeController {

    @Autowired
    private ICodeService codeService;

    @GetMapping(value = "list")
    @AdminAuth(name = "接口代码管理", orderNum = 1, type = "1", icon = "fa fa-columns")
    public String list(Model model, Integer page, HttpServletRequest request) {
        Page<Code> datas = codeService.findAll(ParamFilterUtil.getInstance().buildSearch(model, request),
                SimplePageBuilder.generate(page));
        model.addAttribute("datas", datas);
        return "admin/code/list";
    }

    @Token(flag= Token.READY)
    @AdminAuth(name = "添加接口代码", orderNum = 2, icon="fa fa-plus")
    @RequestMapping(value="add", method= RequestMethod.GET)
    public String add(Model model, HttpServletRequest request) {
        Code c = new Code();
        model.addAttribute("code", c);
        return "admin/code/add";
    }

    @Token(flag= Token.CHECK)
    @RequestMapping(value="add", method=RequestMethod.POST)
    public String add(Model model, Code code, HttpServletRequest request) {
        if(TokenTools.isNoRepeat(request)) { //不是重复提交
            codeService.save(code);
        }
        return "redirect:/admin/code/list";
    }

    @Token(flag= Token.READY)
    @AdminAuth(name="修改接口代码", orderNum=3, icon = "fa fa-pencil")
    @RequestMapping(value="update/{id}", method=RequestMethod.GET)
    public String update(Model model, @PathVariable Integer id, HttpServletRequest request) {
        Code c = codeService.findOne(id);
        model.addAttribute("code", c);
        return "admin/code/update";
    }

    @Token(flag= Token.CHECK)
    @RequestMapping(value="update/{id}", method=RequestMethod.POST)
    public String update(Model model, @PathVariable Integer id, Code code, HttpServletRequest request) {
//		Boolean isRepeat = (Boolean) request.getAttribute("isRepeat");
        if(TokenTools.isNoRepeat(request)) {
            Code c = codeService.findOne(id);
            MyBeanUtils.copyProperties(code, c, new String[]{"id"});
            codeService.save(c);
        }
        return "redirect:/admin/code/list";
    }

    @AdminAuth(name="删除接口代码", orderNum=4, icon = "fa fa-remove")
    @RequestMapping(value="delete/{id}", method=RequestMethod.POST)
    public @ResponseBody String delete(@PathVariable Integer id) {
        try {
            codeService.delete(id);
            return "1";
        } catch (Exception e) {
            return "0";
        }
    }
}
