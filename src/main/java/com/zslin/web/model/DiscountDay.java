package com.zslin.web.model;

import javax.persistence.*;

/**
 * 折扣日期
 * Created by zsl on 2019/3/12.
 */
@Entity
@Table(name = "r_discount_day")
public class DiscountDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "c_year")
    private Integer year;

    @Column(name = "c_month")
    private Integer month;

    @Column(name = "y_month")
    private String yearMonth;

    /**
     * 日期，格式如：1_1,2_0,3_1，第一位表示日期，第二位表示是否为折扣日，1表示是折扣日，0表示正常
     */
    @Lob
    private String days;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public String getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(String yearMonth) {
        this.yearMonth = yearMonth;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }
}
