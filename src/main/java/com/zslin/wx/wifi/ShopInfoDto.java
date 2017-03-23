package com.zslin.wx.wifi;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/23 9:34.
 * 门店信息DTO对象
 */
public class ShopInfoDto {

    /** 门店名称 */
    private String shop_name;

    /** 门店内设备的设备类型，0-未添加设备，4-密码型设备，31-portal型设备 */
    private String protocol_type;

    /** 门店内设备总数 */
    private Integer ap_count;

    /** 商家主页模板类型,0-模板；1-自设URL */
    private Integer template_id;

    /** 商家主页链接 */
    private String homepage_url;

    /** 顶部常驻入口上显示的文本内容：0--欢迎光临+公众号名称；1--欢迎光临+门店名称；2--已连接+公众号名称+WiFi；3--已连接+门店名称+Wi-Fi */
    private Integer bar_type;

    /** 连网完成页链接 */
    private String finishpage_url;

    /** 门店ID（适用于微信卡券、微信门店业务），具体定义参考微信门店，与shop_id一一对应 */
    private String poi_id;

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getProtocol_type() {
        return protocol_type;
    }

    public void setProtocol_type(String protocol_type) {
        this.protocol_type = protocol_type;
    }

    public Integer getAp_count() {
        return ap_count;
    }

    public void setAp_count(Integer ap_count) {
        this.ap_count = ap_count;
    }

    public Integer getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(Integer template_id) {
        this.template_id = template_id;
    }

    public String getHomepage_url() {
        return homepage_url;
    }

    public void setHomepage_url(String homepage_url) {
        this.homepage_url = homepage_url;
    }

    public Integer getBar_type() {
        return bar_type;
    }

    public void setBar_type(Integer bar_type) {
        this.bar_type = bar_type;
    }

    public String getFinishpage_url() {
        return finishpage_url;
    }

    public void setFinishpage_url(String finishpage_url) {
        this.finishpage_url = finishpage_url;
    }

    public String getPoi_id() {
        return poi_id;
    }

    public void setPoi_id(String poi_id) {
        this.poi_id = poi_id;
    }
}
