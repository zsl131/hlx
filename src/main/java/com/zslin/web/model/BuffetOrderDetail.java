package com.zslin.web.model;

import javax.persistence.*;

/**
 * Created by 钟述林 393156105@qq.com on 2017/4/2 17:31.
 * 自助餐订单详情
 */
@Entity
@Table(name = "t_buffet_order_detail")
public class BuffetOrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** 所属订单Id */
    @Column(name = "order_id")
    private Integer orderId;

    /** 所属订单编号 */
    private String orderNo;

    /** 商品编号 */
    @Column(name = "commodity_no")
    private String commodityNo;

    /** 商品Id */
    @Column(name = "commodity_id")
    private Integer commodityId;

    /** 商品名称 */
    @Column(name = "commodity_name")
    private String commodityName;

    /** 商品类型；1-早餐券；2-晚餐券；3-外卖单品 */
    @Column(name = "commodity_type")
    private String commodityType;

    /** 商品单价 */
    private Float price;

    /** 创建日期 */
    @Column(name = "create_day")
    private String createDay;

    /** 创建时间 */
    @Column(name = "create_time")
    private String createTime;

    /** 创建时间Long */
    @Column(name = "create_long")
    private Long createLong;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getCommodityNo() {
        return commodityNo;
    }

    public void setCommodityNo(String commodityNo) {
        this.commodityNo = commodityNo;
    }

    public Integer getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(Integer commodityId) {
        this.commodityId = commodityId;
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }

    public String getCommodityType() {
        return commodityType;
    }

    public void setCommodityType(String commodityType) {
        this.commodityType = commodityType;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
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

    public Long getCreateLong() {
        return createLong;
    }

    public void setCreateLong(Long createLong) {
        this.createLong = createLong;
    }
}
