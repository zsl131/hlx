package com.zslin.web.model;

import com.zslin.web.vo.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by 钟述林 393156105@qq.com on 2017/4/9 19:07.
 * 被删除的钱包记录
 * 当用户绑定手机号码时检测到对应手机号码已经有钱包存在，说明可能是先到店办理会员卡，再通过微信进行手机号码绑定，则需要将两个Wallet合并，并删除其中一个
 */
@Entity
@Table(name = "t_wallet_removed")
public class WalletRemoved extends BaseEntity {

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

    @Column(name = "remove_time")
    private String removeTime;

    public String getRemoveTime() {
        return removeTime;
    }

    public void setRemoveTime(String removeTime) {
        this.removeTime = removeTime;
    }

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
