package com.zslin.stock.model;

import com.zslin.web.vo.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * Created by zsl on 2018/5/27.
 * 物品入库登记
 */
@Entity
@Table(name = "s_goods_register")
public class GoodsRegister extends BaseEntity {

    /** 批次 */
    @Column(name = "batch_no")
    private String batchNo;

    /** 编号 */
    private Integer no;

    /** 申请人电话 */
    @Column(name = "apply_phone")
    private String applyPhone;

    /** 申请人openid */
    @Column(name = "apply_openid")
    private String applyOpenid;

    /** 申请人姓名 */
    @Column(name = "apply_name")
    private String applyName;

    /** 申购数据，特定格式 */
    @Lob
    @Column(name = "apply_datas")
    private String applyDatas;

    /** 单次申购数量 */
    @Column(name = "apply_amount")
    private Integer applyAmount;

    /** 备注 */
    @Column(name = "apply_remark")
    private String applyRemark;

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

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public Integer getNo() {
        return no;
    }

    public void setNo(Integer no) {
        this.no = no;
    }

    public String getApplyPhone() {
        return applyPhone;
    }

    public void setApplyPhone(String applyPhone) {
        this.applyPhone = applyPhone;
    }

    public String getApplyOpenid() {
        return applyOpenid;
    }

    public void setApplyOpenid(String applyOpenid) {
        this.applyOpenid = applyOpenid;
    }

    public String getApplyName() {
        return applyName;
    }

    public void setApplyName(String applyName) {
        this.applyName = applyName;
    }

    public String getApplyDatas() {
        return applyDatas;
    }

    public void setApplyDatas(String applyDatas) {
        this.applyDatas = applyDatas;
    }

    public Integer getApplyAmount() {
        return applyAmount;
    }

    public void setApplyAmount(Integer applyAmount) {
        this.applyAmount = applyAmount;
    }

    public String getApplyRemark() {
        return applyRemark;
    }

    public void setApplyRemark(String applyRemark) {
        this.applyRemark = applyRemark;
    }
}
