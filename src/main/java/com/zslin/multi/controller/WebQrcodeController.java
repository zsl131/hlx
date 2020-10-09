package com.zslin.multi.controller;

import com.zslin.multi.dao.IBaseQrcodeDao;
import com.zslin.multi.model.BaseQrcode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "web/qrcode")
public class WebQrcodeController {

    @Autowired
    private IBaseQrcodeDao baseQrcodeDao;

    @GetMapping(value = "index")
    public String index(Integer id) {
        BaseQrcode code = baseQrcodeDao.findOne(id);
        return "redirect:"+code.getUrl();
    }
}
