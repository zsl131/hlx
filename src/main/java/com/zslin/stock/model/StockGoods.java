package com.zslin.stock.model;

import javax.persistence.*;

/**
 * Created by zsl on 2018/5/21.
 * 库存物品
 */
@Entity
@Table(name = "s_stock_goods")
public class StockGoods {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /** 物品名称 */
    private String name;

    /** 编码，由系统自动生成 */
    private String no;

    /** 对应no的int类型 */
    @Column(name = "order_no")
    private Integer orderNo;

    /** 名称缩写 */
    @Column(name = "name_short")
    private String nameShort;

    /** 名称全拼 */
    @Column(name = "name_full")
    private String nameFull;

    @Column(name = "cate_id")
    private Integer cateId;

    @Column(name = "cate_name")
    private String cateName;

    /** 物品数量 */
    private Integer amount;

    /**  预警底线 */
    @Column(name = "warn_amount")
    private Integer warnAmount;

    /** 位置类型，1-冻库；2-仓库；3-店内 */
    @Column(name = "location_type")
    private String locationType;

    /** 计量单位，如：件、桶等 */
    private String unit;

    /** 状态，1-可出入库；0-禁止入库，当status=0且amount=0时 也不可以出库 */
    private String status;

    /** 备注，如标明一件有几包 */
    private String remark;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getWarnAmount() {
        return warnAmount;
    }

    public void setWarnAmount(Integer warnAmount) {
        this.warnAmount = warnAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getNameShort() {
        return nameShort;
    }

    public void setNameShort(String nameShort) {
        this.nameShort = nameShort;
    }

    public String getNameFull() {
        return nameFull;
    }

    public void setNameFull(String nameFull) {
        this.nameFull = nameFull;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCateId() {
        return cateId;
    }

    public void setCateId(Integer cateId) {
        this.cateId = cateId;
    }

    public String getCateName() {
        return cateName;
    }

    public void setCateName(String cateName) {
        this.cateName = cateName;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
