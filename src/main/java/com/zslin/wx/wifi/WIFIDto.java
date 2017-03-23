package com.zslin.wx.wifi;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/23 9:50.
 * WIFI设备DTO对象
 */
public class WIFIDto {

    /** 门店ID */
    private Integer shop_id;

    /** 连网设备ssid */
    private String ssid;

    /** 无线MAC地址，删除时需要 */
    private String bssid;

    /** 门店内设备的设备类型，0-未添加设备，4-密码型设备，31-portal型设备 */
    private Integer protocol_type;

    /** 密码 */
    private String password;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WIFIDto wifiDto = (WIFIDto) o;

        return ssid != null ? ssid.equals(wifiDto.ssid) : wifiDto.ssid == null;

    }

    @Override
    public int hashCode() {
        return ssid != null ? ssid.hashCode() : 0;
    }

    public Integer getShop_id() {
        return shop_id;
    }

    public void setShop_id(Integer shop_id) {
        this.shop_id = shop_id;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getBssid() {
        return bssid;
    }

    public void setBssid(String bssid) {
        this.bssid = bssid;
    }

    public Integer getProtocol_type() {
        return protocol_type;
    }

    public void setProtocol_type(Integer protocol_type) {
        this.protocol_type = protocol_type;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
