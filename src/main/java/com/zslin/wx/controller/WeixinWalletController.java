package com.zslin.wx.controller;

import com.zslin.web.service.IWalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/8 10:18.
 * 微信用户钱包
 */
@Controller
@RequestMapping(value = "wx/wallet")
public class WeixinWalletController {

    @Autowired
    private IWalletService walletService;

}
