package com.zslin.weixin.controller;

import com.zslin.basic.tools.SecurityUtil;
import com.zslin.kaoqin.model.Worker;
import com.zslin.kaoqin.service.IWorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;

/**
 * 微信登陆
 */
@Controller
@RequestMapping(value = "weixin/login")
public class WeixinLoginController {

    @Autowired
    private IWorkerService workerService;

    @GetMapping(value = "index")
    public String index(Model model, HttpServletRequest request) {

        return "weixin/login/index";
    }

    /** 登陆提交 */
    @PostMapping(value = "onSubmit")
    public @ResponseBody String onSubmit(String phone, String password) {
        try {
            Worker worker = workerService.findByPhone(phone);
            if(worker==null) {
                return "-1"; //未查到对应员工信息
            }
            if(!worker.getPassword().equals(SecurityUtil.md5(password))) {
                return "-2"; //密码不正确
            }
            return "1"; //登陆验证成功
        } catch (Exception e) {
            return "-10"; //解密失败
        }
    }
}
