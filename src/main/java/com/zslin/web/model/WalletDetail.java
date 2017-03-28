package com.zslin.web.model;

import com.zslin.web.vo.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by 钟述林 393156105@qq.com on 2017/1/23 14:32.
 * 钱包详情
 */
@Entity
@Table(name = "t_wallet_detail")
public class WalletDetail extends BaseEntity {

    private String openid;

    /** 对应用户Id */
    @Column(name = "account_id")
    private Integer accountId;

    /** 对应用户名称 */
    @Column(name = "account_name")
    private String accountName;

    /** 类型，1-现金；2-积分 */
    private String type;

    /** 原因，如：充值、活动、点评、消费等 */
    private String reason;

    /** 方式，1-进帐，-1-出帐 */
    private String flag;

    /** 数量，如果是现金，单位为分 */
    private Integer amount;

    /** 剩余，当是积分时，有可能积分只使用一部份 */
    private Integer surplus;

    /** 交易类型 */
    @Column(name = "tran_type")
    private String tranType;

    /** 状态，0-正常；1-部份使用；2-全部使用；-1-过期 */
    private String status="0";

    public Integer getSurplus() {
        return surplus;
    }

    public void setSurplus(Integer surplus) {
        this.surplus = surplus;
    }

    /** 状态，0-正常；1-部份使用；2-全部使用；-1-过期 */
    public String getStatus() {
        return status;
    }

    /** 状态，0-正常；1-部份使用；2-全部使用；-1-过期 */
    public void setStatus(String status) {
        this.status = status;
    }

    public String getTranType() {
        return tranType;
    }

    public void setTranType(String tranType) {
        this.tranType = tranType;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
