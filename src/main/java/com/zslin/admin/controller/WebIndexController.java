package com.zslin.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/23 9:17.
 */
@Controller
public class WebIndexController {

    @GetMapping(value = {"", "/"})
    public String index() {
        return "redirect:/admin";
    }
}
