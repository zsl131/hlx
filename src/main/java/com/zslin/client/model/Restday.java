package com.zslin.client.model;

import javax.persistence.*;

/**
 * Created by zsl on 2018/1/13.
 * 工作日，主要用于确定是否可使用简餐，简餐只能在工作日的中午使用
 */
@Entity
@Table(name = "t_restday")
public class Restday {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "c_year")
    private Integer year;

    @Column(name = "c_month")
    private Integer month;

    @Column(name = "y_month")
    private String yearMonth;

    /**
     * 日期，格式如：1_1,2_0,3_1，第一位表示日期，第二位表示是否为工作日，1表示是工作日，0表示休息
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
