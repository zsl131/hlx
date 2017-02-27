package com.zslin.web.vo;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by 钟述林 393156105@qq.com on 2017/1/16 16:08.
 * 基础基类,所有的vo都继承于该类,里面放了一些共有属性
 */
@MappedSuperclass
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /** 创建日期，Date类型 */
    @Column(name = "create_date")
    private Date createDate;

    /** 创建日期，Long类型 */
    @Column(name = "create_long")
    private Long createLong;

    /** 创建日期，格式 yyyy-MM-dd */
    @Column(name = "create_day")
    private String createDay;

    /** 创建日期，格式 yyyy-MM-dd HH:mm:ss */
    @Column(name = "create_time")
    private String createTime;

    private String reserve1;

    private String reserve2;

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getCreateLong() {
        return createLong;
    }

    public void setCreateLong(Long createLong) {
        this.createLong = createLong;
    }

    public String getCreateDay() {
        return createDay;
    }

    public void setCreateDay(String createDay) {
        this.createDay = createDay;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReserve1() {
        return reserve1;
    }

    public String getReserve2() {
        return reserve2;
    }

    public void setReserve1(String reserve1) {
        this.reserve1 = reserve1;
    }

    public void setReserve2(String reserve2) {
        this.reserve2 = reserve2;
    }
}
