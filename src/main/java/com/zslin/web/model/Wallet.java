package com.zslin.web.model;

import com.zslin.web.vo.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by 钟述林 393156105@qq.com on 2017/1/23 14:35.
 * 钱包
 */
@Entity
@Table(name = "t_wallet")
public class Wallet extends BaseEntity {

    private String openid;

    /** 对应用户Id */
    @Column(name = "account_id")
    private Integer accountId;

    /** 对应用户名称 */
    @Column(name = "account_name")
    private String accountName;

    /** 剩余现金，单位分 */
    private Integer money;

    /** 剩余积分 */
    private Integer score;

    /** 支付密码 */
    private String password;

    /** 手机号码 */
    private String phone;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
