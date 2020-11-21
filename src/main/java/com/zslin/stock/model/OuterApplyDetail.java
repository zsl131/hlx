package com.zslin.stock.model;

import javax.persistence.*;

/**
 * Created by zsl on 2018/5/27.
 * 出库申请物品详情
 */
@Entity
@Table(name = "s_outer_apply_detail")
public class OuterApplyDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** 申请批次编辑 */
    @Column(name = "batch_no")
    private String batchNo;

    private String no;

    /** 物品名称 */
    private String name;

    /** 名称缩写 */
    @Column(name = "name_short")
    private String nameShort;

    /** 物品Id */
    @Column(name = "goods_id")
    private Integer goodsId;

    /** 名称全拼 */
    @Column(name = "name_full")
    private String nameFull;

    @Column(name = "cate_id")
    private Integer cateId;

    @Column(name = "cate_name")
    private String cateName;

    /** 申请出库的物品数量 */
    private Integer amount=0;

    /** 实际出库数量 */
    @Column(name = "amount_true")
    private Integer amountTrue=0;

    /** 位置类型，1-冻库；2-仓库；3-店内 */
    @Column(name = "location_type")
    private String locationType;

    /** 计量单位，如：件、桶等 */
    private String unit;

    /** 店铺ID */
    private Integer storeId;

    /** 店铺名称 */
    private String storeName;

    /** 店铺SN */
    private String storeSn;

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreSn() {
        return storeSn;
    }

    public void setStoreSn(String storeSn) {
        this.storeSn = storeSn;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameShort() {
        return nameShort;
    }

    public void setNameShort(String nameShort) {
        this.nameShort = nameShort;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public String getNameFull() {
        return nameFull;
    }

    public void setNameFull(String nameFull) {
        this.nameFull = nameFull;
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

    public Integer getAmountTrue() {
        return amountTrue;
    }

    public void setAmountTrue(Integer amountTrue) {
        this.amountTrue = amountTrue;
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
