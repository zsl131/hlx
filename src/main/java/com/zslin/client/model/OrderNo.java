package com.zslin.client.model;

import javax.persistence.*;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/13 15:50.
 * 订单编号
 */
@Entity
@Table(name = "t_order_no")
public class OrderNo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** 日期，yyyyMMdd */
    private String days;

    @Column(name = "cur_no")
    private Integer curNo;

    /** 类型，1-店内订单；2-美团订单；3-微信订单；4-友情价订单 */
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public Integer getCurNo() {
        return curNo;
    }

    public void setCurNo(Integer curNo) {
        this.curNo = curNo;
    }
}
