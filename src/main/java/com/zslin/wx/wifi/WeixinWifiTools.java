package com.zslin.wx.wifi;

import com.alibaba.fastjson.JSON;
import com.zslin.weixin.model.Shop;
import com.zslin.weixin.service.IShopService;
import com.zslin.wx.tools.AccessTokenTools;
import com.zslin.wx.tools.JsonTools;
import com.zslin.wx.tools.WeixinUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/21 23:49.
 * 微信的Wifi管理工具类
 */
@Component
public class WeixinWifiTools {

    @Autowired
    private AccessTokenTools accessTokenTools;

    @Autowired
    private IShopService shopService;

    /**
     * 获取Wifi链接数据统计
     * @param shopId 微信门店Id
     * @param beginDate 开始日期，格式：yyyy-MM-dd
     * @param endDate 结束日期，格式：yyyy-MM-dd
     */
    public void getWifiData(Integer shopId, String beginDate, String endDate) {
        String url = "https://api.weixin.qq.com/bizwifi/statistics/list?access_token="+accessTokenTools.getAccessToken();
        String param = "{\"shop_id\":"+shopId+", \"begin_date\":\""+beginDate+"\", \"end_date\":\""+endDate+"\"}";
        WeixinUtil.httpRequest(url, "POST", param);
        JSONObject jsonObj = WeixinUtil.httpRequest(url, "POST", param);
        //以下是返回值
        /**
         * {
         "errcode": 0,
         "data": [
         {
         "shop_id": "-1",
         "statis_time": 1430409600000,
         "total_user": 2,
         "homepage_uv": 0,
         "new_fans": 0,
         "total_fans": 4,
         "wxconnect_user": 8,
         "connect_msg_user": 5
         },
         {
         "shop_id": "-1",
         "statis_time": 1430496000000,
         "total_user": 2,
         "homepage_uv": 0,
         "new_fans": 0,
         "total_fans": 4,
         "wxconnect_user": 4,
         "connect_msg_user": 3
         }
         ]
         }
         */
    }

    /**
     * 设置WIFI连接完成后的显示页面
     * @param shopId 微信门店Id
     * @param finishPage 连接完成后显示页面的URL地址
     * @return
     */
    public boolean setWifiFinishPage(Integer shopId, String finishPage) {
        String url = "https://api.weixin.qq.com/bizwifi/finishpage/set?access_token="+accessTokenTools.getAccessToken();
        String param = "{\"shop_id\":"+shopId+", \"finishpage_url\":\""+finishPage+"\"}";
        WeixinUtil.httpRequest(url, "POST", param);
        JSONObject jsonObj = WeixinUtil.httpRequest(url, "POST", param);
        Integer errcode = jsonObj.getInt("errcode");
        return errcode==0;
    }

    /**
     * 设置Wifi链接成功的主页，只设置固定页面
     * @param shopId 微信门店Id
     * @param homePage 商家首页url地址
     */
    public void setWifiHome(Integer shopId, String homePage) {
        String url = "https://api.weixin.qq.com/bizwifi/homepage/set?access_token="+accessTokenTools.getAccessToken();
        String param = "{\"shop_id\":"+shopId+", \"template_id\":1, \"struct\":{\"url\":\""+homePage+"\"}}";
        WeixinUtil.httpRequest(url, "POST", param);
//        JSONObject jsonObj = WeixinUtil.httpRequest(url, "POST", param);
//        Integer errcode = jsonObj.getInt("errcode");
    }

    /**
     * 获取WIFI设备的二维码图片
     * @param shopId 微信门店ID
     * @param ssid wifi设备id
     * @return
     */
    public String getWifiPic(Integer shopId, String ssid) {
        String qrUrl = null;
        String url = "https://api.weixin.qq.com/bizwifi/qrcode/get?access_token="+accessTokenTools.getAccessToken();
        String param = "{\"shop_id\":"+shopId+", \"ssid\":\""+ssid+"\", \"img_id\":1}";
        JSONObject jsonObj = WeixinUtil.httpRequest(url, "POST", param);
        Integer errcode = jsonObj.getInt("errcode");
        if(errcode==0) {
            qrUrl = jsonObj.getJSONObject("data").getString("qrcode_url");
        }
        return qrUrl;
    }

    /**
     * 向门店内添加密码设备
     * @param shopId 微信门店ID
     * @param ssid wifi名称
     * @param password wifi密码
     * @return
     */
    public boolean addDevice2Shop(Integer shopId, String ssid, String password) {
        String url = "https://api.weixin.qq.com/bizwifi/device/add?access_token="+accessTokenTools.getAccessToken();
        String param = "{\"shop_id\":"+shopId+", \"ssid\":\""+ssid+"\", \"password\":\""+password+"\"}";
        JSONObject jsonObj = WeixinUtil.httpRequest(url, "POST", param);
        Integer errcode = jsonObj.getInt("errcode");
        return errcode==0; //errcode为0表示添加成功
    }

    /**
     * 通过MAC地址删除设备
     * @param bssid MAC地址
     * @return
     */
    public boolean deleteDevice(String bssid) {
        String url = "https://api.weixin.qq.com/bizwifi/device/delete?access_token="+accessTokenTools.getAccessToken();
        String param = "{\"bssid\":\""+bssid+"\"}";
        JSONObject jsonObj = WeixinUtil.httpRequest(url, "POST", param);
        Integer errcode = jsonObj.getInt("errcode");
        return errcode==0; //errcode为0表示添加成功
    }

    /**
     * 获取门店下的所有WIFI设备
     * 1、先获取wifi和密码
     * 2、再获取wifi和MAC地址
     * 只有这样才能即显示密码又能通过MAC删除设备
     * @param shopId 微信门店id
     * @return
     */
    public List<WIFIDto> listWifi(Integer shopId) {
        List<WIFIDto> res = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        String url = "https://api.weixin.qq.com/bizwifi/shop/get?access_token="+accessTokenTools.getAccessToken();
        String param = "{\"shop_id\":"+shopId+"}";
        JSONObject jsonObj = WeixinUtil.httpRequest(url, "POST", param);
        Integer errcode = jsonObj.getInt("errcode");
        if(errcode==0) { //无错
            JSONArray array = jsonObj.getJSONObject("data").getJSONArray("ssid_password_list");
            for(int i=0;i<array.length();i++) {
                JSONObject single = array.getJSONObject(i);
                String ssid = single.getString("ssid");
                String password = single.getString("password");
                map.put(ssid, password);
            }
        }

        url = "https://api.weixin.qq.com/bizwifi/device/list?access_token="+accessTokenTools.getAccessToken();
        jsonObj = WeixinUtil.httpRequest(url, "POST", param);
        errcode = jsonObj.getInt("errcode");
        if(errcode==0) { //无错
            JSONArray array = jsonObj.getJSONObject("data").getJSONArray("records");
            for(int i=0;i<array.length();i++) {
                WIFIDto dto = JSON.toJavaObject(JSON.parseObject(array.getJSONObject(i).toString()), WIFIDto.class);
                dto.setPassword(map.get(dto.getSsid()));
                res.add(dto);
            }
        }

        for(String ssid : map.keySet()) {
            WIFIDto dto = new WIFIDto();
            dto.setSsid(ssid); dto.setPassword(map.get(ssid)); dto.setShop_id(shopId);
            if(!res.contains(dto)) {
                res.add(dto);
            }
        }

        return res;
    }

    /**
     * 获取微信门店详细信息
     * @param shopId 微信门店ID
     * @return
     */
    public ShopInfoDto getShopInfo(Integer shopId) {
//        Map<String, String> res = new HashMap<>();
        ShopInfoDto dto = new ShopInfoDto();
        String url = "https://api.weixin.qq.com/bizwifi/shop/get?access_token="+accessTokenTools.getAccessToken();
        String param = "{\"shop_id\":"+shopId+"}";
        JSONObject jsonObj = WeixinUtil.httpRequest(url, "POST", param);
        Integer errcode = jsonObj.getInt("errcode");
        if(errcode==0) { //无错
            dto = JSON.toJavaObject(JSON.parseObject(jsonObj.getJSONObject("data").toString()), ShopInfoDto.class);
        }
        return dto;
    }

    /**
     * 同步微信门店
     */
    public void listShop() {
        String url = "https://api.weixin.qq.com/bizwifi/shop/list?access_token="+accessTokenTools.getAccessToken();
        String param = "{\"pageindex\":1, \"pagesize\":10}";
        JSONObject jsonObj = WeixinUtil.httpRequest(url, "POST", param);
        JSONObject data = jsonObj.getJSONObject("data");

        Integer count = data.getInt("totalcount");
        if(count!=null && count>0) {
            JSONArray array = data.getJSONArray("records");
            for(int i=0;i<array.length();i++) {
                JSONObject single = array.getJSONObject(i);
                String shopName = single.getString("shop_name");
                Integer shopId = single.getInt("shop_id");
                Shop s = shopService.findByShopId(shopId);
                if(s==null) { //如果不存在，则新建
                    s = new Shop();
                }
                s.setShopId(shopId);
                s.setShopName(shopName);
                shopService.save(s);
            }
        }
    }
}
