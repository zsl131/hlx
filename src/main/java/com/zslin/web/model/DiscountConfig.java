package com.zslin.web.model;

import javax.persistence.*;

/**
 * 折扣配置
 *  - 2019年涨价功能
 * Created by zsl on 2019/3/12.
 */
@Entity
@Table(name = "r_discount_config")
public class DiscountConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /** 状态，0-停用；1-启用 */
    private String status;

    /** 上午减免金额 */
    @Column(name = "discount_am")
    private Float discountAM;

    /** 下午减免金额 */
    @Column(name = "discount_pm")
    private Float discountPM;

    /** 上午半票减免金额 */
    @Column(name = "discount_half_am")
    private Float discountHalfAM;

    /** 下午半票减免金额 */
    @Column(name = "discount_half_pm")
    private Float discountHalfPM;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Float getDiscountAM() {
        return discountAM;
    }

    public void setDiscountAM(Float discountAM) {
        this.discountAM = discountAM;
    }

    public Float getDiscountPM() {
        return discountPM;
    }

    public void setDiscountPM(Float discountPM) {
        this.discountPM = discountPM;
    }

    public Float getDiscountHalfAM() {
        return discountHalfAM;
    }

    public void setDiscountHalfAM(Float discountHalfAM) {
        this.discountHalfAM = discountHalfAM;
    }

    public Float getDiscountHalfPM() {
        return discountHalfPM;
    }

    public void setDiscountHalfPM(Float discountHalfPM) {
        this.discountHalfPM = discountHalfPM;
    }
}
