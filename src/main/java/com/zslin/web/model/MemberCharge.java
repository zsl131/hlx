package com.zslin.web.model;

import com.zslin.web.vo.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/7 10:46.
 * 会员充值记录，该记录可以修改余额，由于会员每次充值的等级折扣都不一样，所以需要分开存储
 */
@Entity
@Table(name = "t_member_charge")
public class MemberCharge extends BaseEntity {

    /** 等级 */
    private Integer level=1;

    /** 名称 */
    private String name="";

    /** 折扣，未除以100的折扣率，正整数 */
    private Integer discount=100;

    /** 充值金额，单位：元 */
    @Column(name = "charge_money")
    private Float chargeMoney=0f;

    /** 赠送金额，单位，元 */
    @Column(name = "give_money")
    private Float giveMoney=0f;

    /** 此次充值余额，余额 */
    private Integer balance;

    @Column(name = "account_id")
    private Integer accountId;

    private String openid;

    private String headimg;

    private String nickname;

    private String phone;

    /** 每次充值加1 */
    @Column(name = "order_no")
    private Integer orderNo=1;

    /** 状态，0-待处理，1-已收款确认，-1-用户取消 */
    private String status;

    /** 审核人员Id */
    @Column(name = "verify_account_id")
    private Integer verifyAccountId;

    /** 审核人员名称 */
    @Column(name = "verify_account_name")
    private String verifyAccountName;

    /** 审核人员Openid */
    @Column(name = "verify_account_openid")
    private String verifyAccountOpenid;

    /** 审核人员电话 */
    @Column(name = "verify_account_phone")
    private String verifyAccountPhone;

    @Column(name = "verify_date")
    private Date verifyDate;

    public Integer getVerifyAccountId() {
        return verifyAccountId;
    }

    public void setVerifyAccountId(Integer verifyAccountId) {
        this.verifyAccountId = verifyAccountId;
    }

    public String getVerifyAccountName() {
        return verifyAccountName;
    }

    public void setVerifyAccountName(String verifyAccountName) {
        this.verifyAccountName = verifyAccountName;
    }

    public String getVerifyAccountOpenid() {
        return verifyAccountOpenid;
    }

    public void setVerifyAccountOpenid(String verifyAccountOpenid) {
        this.verifyAccountOpenid = verifyAccountOpenid;
    }

    public String getVerifyAccountPhone() {
        return verifyAccountPhone;
    }

    public void setVerifyAccountPhone(String verifyAccountPhone) {
        this.verifyAccountPhone = verifyAccountPhone;
    }

    public Date getVerifyDate() {
        return verifyDate;
    }

    public void setVerifyDate(Date verifyDate) {
        this.verifyDate = verifyDate;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public Float getChargeMoney() {
        return chargeMoney;
    }

    public void setChargeMoney(Float chargeMoney) {
        this.chargeMoney = chargeMoney;
    }

    public Float getGiveMoney() {
        return giveMoney;
    }

    public void setGiveMoney(Float giveMoney) {
        this.giveMoney = giveMoney;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getHeadimg() {
        return headimg;
    }

    public void setHeadimg(String headimg) {
        this.headimg = headimg;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
