package com.zslin.basic.controller;

import com.zslin.basic.dto.AuthToken;
import com.zslin.basic.model.User;
import com.zslin.basic.service.IUserService;
import com.zslin.basic.tools.*;
import com.zslin.web.model.Commodity;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by zsl-pc on 2016/9/7.
 */
@Controller
@RequestMapping(value = "admin")
public class AdminController {

    @Autowired
    private ConfigTools configTools;

    @Autowired
    private IUserService userService;


    private static final String PATH_PRE = "clientProgram";

    /** 后台首页 */
    @RequestMapping(value={"", "/"}, method= RequestMethod.GET)
    public String index(Model model, HttpServletRequest request) {
        return "admin/basic/index";
    }

    @RequestMapping(value="uploadClient", method=RequestMethod.POST)
    public String update(HttpServletRequest request, @RequestParam("file")MultipartFile[] files) {
        if (files != null && files.length >= 1) {
            BufferedOutputStream bw = null;
            try {
                String fileName = files[0].getOriginalFilename();
                if (fileName != null) {

                    File oldFile = new File(configTools.getFilePath() + fileName);
                    if (oldFile.exists()) {
                        oldFile.delete();
                    }

                    File outFile = new File(configTools.getFilePath(PATH_PRE) + File.separator + fileName);
                    FileUtils.copyInputStreamToFile(files[0].getInputStream(), outFile);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bw != null) {
                        bw.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "redirect:/admin";
    }

    /** 修改密码 */
    @RequestMapping(value="updatePwd")
    public String updatePwd(Model model, Integer flag, String oldPwd, String password, String nickname, HttpServletRequest request) {
        String method = request.getMethod(); //获取请求方式，GET、POST
        if("get".equalsIgnoreCase(method)) {
            model.addAttribute("flag", flag);
            return "admin/basic/updatePwd";
        } else if("post".equalsIgnoreCase(method)) {
            AuthToken at = (AuthToken) request.getSession().getAttribute(AuthToken.SESSION_NAME);
            User user = at.getUser();
            try {
                if(password!=null && !"".equals(password)) { //如果没有输入密码，则不修改
                    if(!SecurityUtil.md5(user.getUsername(), oldPwd).equals(user.getPassword())) {
                        model.addAttribute("errorMsg", "原始密码输入错误");
                        return "admin/basic/updatePwd";
                    }
                    user.setPassword(SecurityUtil.md5(user.getUsername(), password));
                }
                user.setNickname(nickname);
                userService.save(user);
                return "redirect:/admin/updatePwd?flag=1";
            } catch (Exception e) {
                //e.printStackTrace();
                return "redirect:/admin/updatePwd?flag=2";
            }
        }
        return "redirect:/admin/updatePwd";
    }
}
