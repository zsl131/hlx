package com.zslin.card.model;

import javax.persistence.*;

/**
 * 卡券申请
 * Created by zsl on 2018/10/12.
 */
@Entity
@Table(name = "c_card_apply")
public class CardApply {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /** 卡券类型 */
    @Column(name = "card_type")
    private String cardType;

    /** 卡券编号 */
    @Column(name = "card_no")
    private Integer cardNo;

    /** 申请原因 */
    @Column(name = "apply_reason")
    private String applyReason;

    /** 申请人姓名 */
    @Column(name = "apply_name")
    private String applyName;

    /** 申请人身份识别，如Openid */
    @Column(name = "apply_key")
    private String applyKey;

    /** 申请时间 */
    @Column(name = "apply_create_time")
    private String applyCreateTime;

    /** 0-待审核；1-审核通过；2-被驳回 */
    private String status;

    /** 审核人姓名 */
    @Column(name = "verify_name")
    private String verifyName;

    /** 审核人身份识别，如Openid */
    @Column(name = "verify_key")
    private String verifyKey;

    /** 审核时间 */
    @Column(name = "verify_create_time")
    private String verifyCreateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public Integer getCardNo() {
        return cardNo;
    }

    public void setCardNo(Integer cardNo) {
        this.cardNo = cardNo;
    }

    public String getApplyReason() {
        return applyReason;
    }

    public void setApplyReason(String applyReason) {
        this.applyReason = applyReason;
    }

    public String getApplyName() {
        return applyName;
    }

    public void setApplyName(String applyName) {
        this.applyName = applyName;
    }

    public String getApplyKey() {
        return applyKey;
    }

    public void setApplyKey(String applyKey) {
        this.applyKey = applyKey;
    }

    public String getApplyCreateTime() {
        return applyCreateTime;
    }

    public void setApplyCreateTime(String applyCreateTime) {
        this.applyCreateTime = applyCreateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVerifyName() {
        return verifyName;
    }

    public void setVerifyName(String verifyName) {
        this.verifyName = verifyName;
    }

    public String getVerifyKey() {
        return verifyKey;
    }

    public void setVerifyKey(String verifyKey) {
        this.verifyKey = verifyKey;
    }

    public String getVerifyCreateTime() {
        return verifyCreateTime;
    }

    public void setVerifyCreateTime(String verifyCreateTime) {
        this.verifyCreateTime = verifyCreateTime;
    }
}
