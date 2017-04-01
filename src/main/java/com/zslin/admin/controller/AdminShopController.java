package com.zslin.admin.controller;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.utils.ParamFilterUtil;
import com.zslin.weixin.model.Shop;
import com.zslin.weixin.service.IShopService;
import com.zslin.wx.wifi.ShopInfoDto;
import com.zslin.wx.wifi.WIFIDto;
import com.zslin.wx.wifi.WeixinWifiTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/23 9:17.
 */
@Controller
@RequestMapping(value = "admin/shop")
@AdminAuth(name = "微信门店管理", psn = "微信管理", orderNum = 12, porderNum = 1, pentity = 0, icon = "fa fa-shopping-bag")
public class AdminShopController {

    @Autowired
    private IShopService shopService;

    @Autowired
    private WeixinWifiTools weixinWifiTools;

    @GetMapping(value = "list")
    @AdminAuth(name = "微信门店管理", type = "1", orderNum = 1, icon = "fa fa-shopping-bag")
    public String list(Model model, Integer page, HttpServletRequest request) {
        Page<Shop> datas = shopService.findAll(ParamFilterUtil.getInstance().buildSearch(model, request),
                SimplePageBuilder.generate(page));
        model.addAttribute("datas", datas);
        return "admin/shop/list";
    }

    @AdminAuth(name="同步微信门店", orderNum=4, icon = "fa fa-check")
    @RequestMapping(value="synch", method=RequestMethod.POST)
    public @ResponseBody
    String synch() {
        try {
            weixinWifiTools.listShop();
            return "1";
        } catch (Exception e) {
            return "0";
        }
    }

    @GetMapping(value = "showShopInfo")
    @AdminAuth(name = "查看门店详细信息", orderNum = 1, icon = "fa fa-eye")
    public String showShopInfo(Model model, Integer shopId, HttpServletRequest request) {
        ShopInfoDto datas = weixinWifiTools.getShopInfo(shopId);
        model.addAttribute("shop", datas);
        return "admin/shop/showShopInfo";
    }

    @AdminAuth(name = "添加设备到门店", orderNum = 2, icon="fa fa-plus")
    @RequestMapping(value="add", method=RequestMethod.POST)
    public @ResponseBody String add(Model model, Integer shopId, String ssid, String password, HttpServletRequest request) {
        weixinWifiTools.addDevice2Shop(shopId, ssid, password);
        return "1";
    }

    @AdminAuth(name = "获取门店所有设备", orderNum = 1, icon = "fa fa-list")
    @GetMapping(value = "listDevice")
    public String listDevice(Model model, Integer shopId, HttpServletRequest request) {
        List<WIFIDto> datas = weixinWifiTools.listWifi(shopId);
        model.addAttribute("datas", datas);
        return "admin/shop/listDevice";
    }

    @AdminAuth(name="删除WIFI设备", orderNum=4, icon = "fa fa-remove")
    @RequestMapping(value="delete/{bssid}", method=RequestMethod.POST)
    public @ResponseBody
    String delete(@PathVariable String bssid) {
        try {
            weixinWifiTools.deleteDevice(bssid);
            return "1";
        } catch (Exception e) {
            return "0";
        }
    }

    @AdminAuth(name="设置首页UFL", orderNum=4, icon = "fa fa-cog")
    @RequestMapping(value="setHomePage", method=RequestMethod.POST)
    public @ResponseBody
    String setHomePage(Integer shopId, String homePage) {
        try {
            weixinWifiTools.setWifiHome(shopId, homePage);
            return "1";
        } catch (Exception e) {
            return "0";
        }
    }

    @AdminAuth(name="设置连接后的URL", orderNum=4, icon = "fa fa-cog")
    @RequestMapping(value="setFirstPage", method=RequestMethod.POST)
    public @ResponseBody
    String setFirstPage(Integer shopId, String firstPage) {
        try {
            weixinWifiTools.setWifiFinishPage(shopId, firstPage);
            return "1";
        } catch (Exception e) {
            return "0";
        }
    }

    @GetMapping(value = "getWifiPic")
    @AdminAuth(name="获取Wifi二维码", orderNum=4, icon = "fa fa-eye")
    public String getWifiPic(Integer shopId, String ssid) {
        String url = weixinWifiTools.getWifiPic(shopId, ssid);
        return "redirect:"+url;
    }
}
