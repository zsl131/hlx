package com.zslin.stock.model;

import javax.persistence.*;

/**
 * Created by zsl on 2018/5/21.
 * 物品申请的详细清单
 */
@Entity
@Table(name = "s_goods_apply_detail")
public class GoodsApplyDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** 申请批次编辑 */
    @Column(name = "batch_no")
    private String batchNo;

    /** 物品名称 */
    private String name;

    /** 编码，由系统自动生成 */
    private String no;

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

    /** 申请采购的物品数量 */
    private Integer amount=0;

    /** 批准采购的物品数量 */
    private Integer allowAmount=0;

    /** 实际采购数量 */
    @Column(name = "amount_true")
    private Integer amountTrue=0;

    /** 状态，1-批准采购；0-申购中；-1-驳回采购 */
    private String status;

    /** 位置类型，1-冻库；2-仓库；3-店内 */
    @Column(name = "location_type")
    private String locationType;

    /** 计量单位，如：件、桶等 */
    private String unit;

    /** 是否审核，在未审核但直接通过时，允许采购数量应为申请数量 */
    @Column(name = "has_check")
    private String hasCheck="0";

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

    public String getHasCheck() {
        return hasCheck;
    }

    public void setHasCheck(String hasCheck) {
        this.hasCheck = hasCheck;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
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

    public Integer getAllowAmount() {
        return allowAmount;
    }

    public void setAllowAmount(Integer allowAmount) {
        this.allowAmount = allowAmount;
    }

    public Integer getAmountTrue() {
        return amountTrue;
    }

    public void setAmountTrue(Integer amountTrue) {
        this.amountTrue = amountTrue;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
