package com.zslin.admin.controller;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.tools.PinyinToolkit;
import com.zslin.weixin.dao.ITemplateMessageRelationDao;
import com.zslin.weixin.dto.TemplateMessageDto;
import com.zslin.weixin.model.TemplateMessageRelation;
import com.zslin.weixin.tools.TemplateMessageAnnotationTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping(value = "admin/templateMessageRelation")
@AdminAuth(name = "微信模板消息关联管理", psn = "微信管理", orderNum = 7, porderNum = 1, pentity = 0, icon = "fa fa-commenting")
public class AdminTemplateMessageRelationController {

    @Autowired
    private ITemplateMessageRelationDao templateMessageRelationDao;

    @Autowired
    private TemplateMessageAnnotationTools templateMessageAnnotationTools;

    @GetMapping(value = "list")
    @AdminAuth(name = "微信模板消息关联管理", type = "1", orderNum = 1, icon = "fa fa-commenting")
    public String list(Model model, String type, HttpServletRequest request) {
        List<TemplateMessageRelation> configed = templateMessageRelationDao.findAll();
        List<TemplateMessageDto> noConfig = templateMessageAnnotationTools.findNoConfigTemplateMessage();
        model.addAttribute("configed", configed);
        model.addAttribute("noConfig", noConfig);
        type = (type!=null && !"2".equals(type))?"1":"2";
        model.addAttribute("type", type);
        return "admin/templateMessageRelation/list";
    }

    ///name: objName, tempId: tempId, kv: keyValues
    @AdminAuth(name="添加微信模板关联数据", orderNum=4, icon = "fa fa-remove")
    @RequestMapping(value="add", method=RequestMethod.POST)
    public @ResponseBody String add(String name, String tempId, String kv) {
        TemplateMessageRelation tmr = new TemplateMessageRelation();
        tmr.setTemplateName(name);
        tmr.setTemplatePinyin(PinyinToolkit.cn2Spell(name, ""));
        tmr.setKeyValues(kv);
        tmr.setTemplateId(tempId);
        templateMessageRelationDao.save(tmr);
        return "1";
    }

    @AdminAuth(name="删除微信模板消息", orderNum=4, icon = "fa fa-remove")
    @RequestMapping(value="delete/{id}", method=RequestMethod.POST)
    public @ResponseBody
    String delete(@PathVariable Integer id) {
        try {
            templateMessageRelationDao.delete(id);
            return "1";
        } catch (Exception e) {
            return "0";
        }
    }
}
