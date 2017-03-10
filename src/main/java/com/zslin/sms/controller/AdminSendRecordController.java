package com.zslin.sms.controller;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.utils.ParamFilterUtil;
import com.zslin.sms.model.SendRecord;
import com.zslin.sms.service.ISendRecordService;
import com.zslin.sms.tools.SmsTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/14 10:24.
 */
@Controller
@RequestMapping(value = "admin/sendRecord")
@AdminAuth(name = "短信发送管理", psn = "短信管理", orderNum = 3, porderNum = 1, pentity = 0, icon = "fa fa-file-text-o")
public class AdminSendRecordController {

    @Autowired
    private ISendRecordService sendRecordService;

    @Autowired
    private SmsTools smsTools;

    @GetMapping(value = "list")
    @AdminAuth(name = "短信发送记录", orderNum = 1, type = "1", icon = "fa fa-file-text-o")
    public String list(Model model, Integer page, HttpServletRequest request) {
        Page<SendRecord> datas = sendRecordService.findAll(ParamFilterUtil.getInstance().buildSearch(model, request),
                SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("createDate_d")));
        model.addAttribute("datas", datas);
        model.addAttribute("surplus", smsTools.surplus());
        return "admin/sendRecord/list";
    }
}
