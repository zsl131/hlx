package com.zslin.web.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/7 9:43.
 * 价格表
 */
@Entity
@Table(name = "t_price")
public class Price implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "store_sn")
    private String storeSn;

    @Column(name = "store_name")
    private String storeName;

    @Column(name = "store_id")
    private Integer storeId;

    /** 早餐价格，单位：元 */
    @Column(name = "breakfast_price")
    private Float breakfastPrice;

    /** 晚餐价格，单位：元 */
    @Column(name = "dinner_price")
    private Float dinnerPrice;

    /** 友情价的早餐价格 */
    @Column(name = "friend_breakfast_price")
    private Float friendBreakfastPrice;

    /** 友情价的晚上价格 */
    @Column(name = "friend_dinner_price")
    private Float friendDinnerPrice;

    /** 友情价折扣率 */
    @Column(name = "friend_rate")
    private Float friendRate;

    /** 压金金额 */
    @Column(name = "bond_money")
    private Float bondMoney;

    /** 特殊商品价格，不开火 */
    @Column(name = "spe_money")
    private Float speMoney;

    /** 简餐半票 */
    @Column(name = "spe_money_half")
    private Float speMoneyHalf;

    public String getStoreSn() {
        return storeSn;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public void setStoreSn(String storeSn) {
        this.storeSn = storeSn;
    }

    public Float getFriendRate() {
        return friendRate;
    }

    public void setFriendRate(Float friendRate) {
        this.friendRate = friendRate;
    }

    public Float getSpeMoneyHalf() {
        return speMoneyHalf;
    }

    public void setSpeMoneyHalf(Float speMoneyHalf) {
        this.speMoneyHalf = speMoneyHalf;
    }

    public Float getSpeMoney() {
        return speMoney;
    }

    public void setSpeMoney(Float speMoney) {
        this.speMoney = speMoney;
    }

    public Float getFriendBreakfastPrice() {
        return friendBreakfastPrice;
    }

    public void setFriendBreakfastPrice(Float friendBreakfastPrice) {
        this.friendBreakfastPrice = friendBreakfastPrice;
    }

    public Float getFriendDinnerPrice() {
        return friendDinnerPrice;
    }

    public void setFriendDinnerPrice(Float friendDinnerPrice) {
        this.friendDinnerPrice = friendDinnerPrice;
    }

    public Float getBondMoney() {
        return bondMoney;
    }

    public void setBondMoney(Float bondMoney) {
        this.bondMoney = bondMoney;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Float getBreakfastPrice() {
        return breakfastPrice;
    }

    public void setBreakfastPrice(Float breakfastPrice) {
        this.breakfastPrice = breakfastPrice;
    }

    public Float getDinnerPrice() {
        return dinnerPrice;
    }

    public void setDinnerPrice(Float dinnerPrice) {
        this.dinnerPrice = dinnerPrice;
    }
}
