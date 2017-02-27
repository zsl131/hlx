package com.zslin.admin.controller;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.utils.ParamFilterUtil;
import com.zslin.web.model.ActivityRecord;
import com.zslin.web.service.IActivityRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 钟述林 393156105@qq.com on 2017/1/25 22:55.
 */
@Controller
@RequestMapping(value = "admin/activityRecord")
@AdminAuth(name = "活动记录", porderNum = 1, orderNum = 5, pentity = 0, psn = "应用管理", icon = "fa fa-file-text-o")
public class AdminActivityRecordController {

    @Autowired
    private IActivityRecordService activityRecordService;

    @GetMapping(value = "list")
    @AdminAuth(name = "活动记录", orderNum = 1, type = "1", icon = "fa fa-file-text-o")
    public String list(Model model, Integer page, HttpServletRequest request) {
        Page<ActivityRecord> datas = activityRecordService.findAll(ParamFilterUtil.getInstance().buildSearch(model, request),
                SimplePageBuilder.generate(page));
        model.addAttribute("datas", datas);
        return "admin/activityRecord";
    }
}
