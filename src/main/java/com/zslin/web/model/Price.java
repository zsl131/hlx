package com.zslin.web.model;

import javax.persistence.*;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/7 9:43.
 * 价格表
 */
@Entity
@Table(name = "t_price")
public class Price {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /** 早餐价格，单位：元 */
    @Column(name = "breakfast_price")
    private Float breakfastPrice;

    /** 晚餐价格，单位：元 */
    @Column(name = "dinner_price")
    private Float dinnerPrice;

    /** 压金金额 */
    @Column(name = "bond_money")
    private Float bondMoney;

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
