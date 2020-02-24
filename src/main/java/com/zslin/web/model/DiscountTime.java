package com.zslin.web.model;

import javax.persistence.*;

/**
 * Created by 钟述林 393156105@qq.com on 2017/8/28 10:10.
 * 优惠时段
 */
@Entity
@Table(name = "t_discount_time")
public class DiscountTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** 开始时间点，如：1700 */
    @Column(name = "start_time")
    private Integer startTime;

    /** 结束时间点，如：1730 */
    @Column(name = "end_time")
    private Integer endTime;

    /** 开始时间，如：17:00 */
    @Column(name = "start_clock")
    private String startClock;

    /** 结束时间，如：17:30 */
    @Column(name = "end_clock")
    private String endClock;

    /** 状态，0-停用；1-启用 */
    private String status;

    /** 优惠金额 */
    @Column(name = "discount_money")
    private Float discountMoney;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStartTime() {
        return startTime;
    }

    public void setStartTime(Integer startTime) {
        this.startTime = startTime;
    }

    public Integer getEndTime() {
        return endTime;
    }

    public void setEndTime(Integer endTime) {
        this.endTime = endTime;
    }

    public String getStartClock() {
        return startClock;
    }

    public void setStartClock(String startClock) {
        this.startClock = startClock;
    }

    public String getEndClock() {
        return endClock;
    }

    public void setEndClock(String endClock) {
        this.endClock = endClock;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Float getDiscountMoney() {
        return discountMoney;
    }

    public void setDiscountMoney(Float discountMoney) {
        this.discountMoney = discountMoney;
    }
}
