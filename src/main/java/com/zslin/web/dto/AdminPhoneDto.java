package com.zslin.web.dto;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/14 14:15.
 * 股东电话的DTO对象
 */
public class AdminPhoneDto {
    //联系电话
    private String phone;
    //姓名，可能为空
    private String name;
    //openid
    private String openid;
    //用户Id
    private Integer accountId;

    public AdminPhoneDto(String phone, String name, String openid, Integer accountId) {
        this.phone = phone;
        this.name = name;
        this.openid = openid;
        this.accountId = accountId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }
}
