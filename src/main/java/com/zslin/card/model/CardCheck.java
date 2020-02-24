package com.zslin.card.model;

import javax.persistence.*;

/**
 * 卡券核销
 * Created by zsl on 2018/10/13.
 */
@Entity
@Table(name = "c_card_check")
public class CardCheck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "card_type")
    private String cardType;

    @Column(name = "card_no")
    private Integer cardNo;

    @Column(name = "under_name")
    private String underName;

    @Column(name = "under_key")
    private String underKey;

    /** 核销月份，格式：yyyyMM */
    @Column(name = "check_month")
    private String checkMonth;

    /** 核销日期，格式：yyyyMMdd */
    @Column(name = "check_day")
    private String checkDay;

    /** 核销时间，格式：yyyy-MM-dd  HH:mm:ss*/
    @Column(name = "check_time")
    private String checkTime;

    /** 对应订单编号 */
    @Column(name = "order_no")
    private String orderNo;

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

    public String getUnderName() {
        return underName;
    }

    public void setUnderName(String underName) {
        this.underName = underName;
    }

    public String getUnderKey() {
        return underKey;
    }

    public void setUnderKey(String underKey) {
        this.underKey = underKey;
    }

    public String getCheckMonth() {
        return checkMonth;
    }

    public void setCheckMonth(String checkMonth) {
        this.checkMonth = checkMonth;
    }

    public String getCheckDay() {
        return checkDay;
    }

    public void setCheckDay(String checkDay) {
        this.checkDay = checkDay;
    }

    public String getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(String checkTime) {
        this.checkTime = checkTime;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}
