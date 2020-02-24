package com.zslin.card.model;

import javax.persistence.*;

/**
 * 发放卡券，用于发放到店内
 * Created by zsl on 2018/10/13.
 */
@Entity
@Table(name = "c_grant_card")
public class GrantCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** 卡券类型 */
    @Column(name = "card_type")
    private String cardType;

    /** 卡券编号 */
    @Column(name = "card_no")
    private Integer cardNo;

    @Column(name = "create_time")
    private String createTime;

    /** 格式yyyyMMdd */
    @Column(name = "create_day")
    private String createDay;

    @Column(name = "order_no")
    private Integer orderNo;

    /** 批次编号 */
    @Column(name = "batch_no")
    private String batchNo;

    /** 状态，0-在店；1-已发出 */
    private String status="0";

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateDay() {
        return createDay;
    }

    public void setCreateDay(String createDay) {
        this.createDay = createDay;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }
}
