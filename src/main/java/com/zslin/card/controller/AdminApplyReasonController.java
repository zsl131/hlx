package com.zslin.card.controller;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.annotations.Token;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.tools.MyBeanUtils;
import com.zslin.basic.tools.TokenTools;
import com.zslin.basic.utils.ParamFilterUtil;
import com.zslin.card.model.ApplyReason;
import com.zslin.card.service.IApplyReasonService;
import com.zslin.client.tools.ClientFileTools;
import com.zslin.client.tools.ClientJsonTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by zsl on 2018/10/13.
 */
@Controller
@RequestMapping(value = "admin/applyReason")
@AdminAuth(name = "申请原因管理", psn = "卡券管理", orderNum = 9, porderNum = 10, pentity = 0, icon = "fa fa-comment-o")
public class AdminApplyReasonController {

    @Autowired
    private IApplyReasonService applyReasonService;

    @Autowired
    private ClientFileTools clientFileTools;

    @GetMapping(value = "list")
    @AdminAuth(name = "申请原因管理", type = "1", orderNum = 1, icon = "fa fa-comment-o")
    public String list(Model model, Integer page, HttpServletRequest request) {
        Page<ApplyReason> datas = applyReasonService.findAll(ParamFilterUtil.getInstance().buildSearch(model, request),
                SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("id_d")));
        model.addAttribute("datas", datas);
        return "admin/applyReason/list";
    }

    @Token(flag= Token.READY)
    @AdminAuth(name = "添加申请原因", orderNum = 2, icon="fa fa-plus")
    @RequestMapping(value="add", method= RequestMethod.GET)
    public String add(Model model, HttpServletRequest request) {
        model.addAttribute("applyReason", new ApplyReason());
        return "admin/applyReason/add";
    }

    @Token(flag= Token.CHECK)
    @RequestMapping(value="add", method=RequestMethod.POST)
    public String add(Model model, ApplyReason applyReason, HttpServletRequest request) {
        if(TokenTools.isNoRepeat(request)) { //不是重复提交
            applyReasonService.save(applyReason);
            sendReason2Client("add", applyReason);
        }
        return "redirect:/admin/applyReason/list";
    }

    @Token(flag= Token.READY)
    @AdminAuth(name="修改申请原因", orderNum=3, icon = "fa fa-pencil")
    @RequestMapping(value="update/{id}", method=RequestMethod.GET)
    public String update(Model model, @PathVariable Integer id, HttpServletRequest request) {
        ApplyReason ar = applyReasonService.findOne(id);
        model.addAttribute("applyReason", ar);
        return "admin/applyReason/update";
    }

    @Token(flag= Token.CHECK)
    @RequestMapping(value="update/{id}", method=RequestMethod.POST)
    public String update(Model model, @PathVariable Integer id, ApplyReason applyReason, HttpServletRequest request) {
        if(TokenTools.isNoRepeat(request)) {
            ApplyReason ar = applyReasonService.findOne(id);
            MyBeanUtils.copyProperties(applyReason, ar, "id");
            applyReasonService.save(ar);

            sendReason2Client("update", ar);
        }
        return "redirect:/admin/applyReason/list";
    }

    @AdminAuth(name="删除申请原因", orderNum=4, icon = "fa fa-remove")
    @RequestMapping(value="delete/{id}", method=RequestMethod.POST)
    public @ResponseBody
    String delete(@PathVariable Integer id) {
        try {
            sendReason2Client("delete", applyReasonService.findOne(id));
            applyReasonService.delete(id);
            return "1";
        } catch (Exception e) {
            return "0";
        }
    }

    private void sendReason2Client(String action, ApplyReason w) {
        String content = ClientJsonTools.buildDataJson(ClientJsonTools.buildCardApplyReason(action, w));
        clientFileTools.setChangeContext(content, true);
    }
}
