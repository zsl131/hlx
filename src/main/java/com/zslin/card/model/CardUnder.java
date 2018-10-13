package com.zslin.card.model;

import javax.persistence.*;

/**
 * 卡券归属
 * Created by zsl on 2018/10/13.
 */
@Entity
@Table(name = "c_card_under")
public class CardUnder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /** 卡券类型 */
    @Column(name = "card_type")
    private String cardType;

    /** 卡券编号 */
    @Column(name = "card_no")
    private Integer cardNo;

    /** 归属姓名 */
    @Column(name = "under_name")
    private String underName;

    /** 归属身份，如Openid */
    @Column(name = "under_key")
    private String underKey;

    /** 操作日期 */
    @Column(name = "create_time")
    private String createTime;

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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
