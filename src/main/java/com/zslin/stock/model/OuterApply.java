package com.zslin.stock.model;

import com.zslin.web.vo.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * Created by zsl on 2018/5/27.
 * 出库申请
 */
@Entity
@Table(name = "s_outer_apply")
public class OuterApply extends BaseEntity {

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

    /** 状态，0-出库申请中；1-核对完成 */
    private String status;

    /** 核对人电话 */
    @Column(name = "check_phone")
    private String checkPhone;

    /** 核对人openid */
    @Column(name = "check_openid")
    private String checkOpenid;

    /** 核对人姓名 */
    @Column(name = "check_name")
    private String checkName;

    /** 申购数据，特定格式 */
    @Lob
    @Column(name = "check_datas")
    private String checkDatas;

    /** 单次申购数量 */
    @Column(name = "check_amount")
    private Integer checkAmount;

    /** 备注 */
    @Column(name = "check_remark")
    private String checkRemark;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCheckPhone() {
        return checkPhone;
    }

    public void setCheckPhone(String checkPhone) {
        this.checkPhone = checkPhone;
    }

    public String getCheckOpenid() {
        return checkOpenid;
    }

    public void setCheckOpenid(String checkOpenid) {
        this.checkOpenid = checkOpenid;
    }

    public String getCheckName() {
        return checkName;
    }

    public void setCheckName(String checkName) {
        this.checkName = checkName;
    }

    public String getCheckDatas() {
        return checkDatas;
    }

    public void setCheckDatas(String checkDatas) {
        this.checkDatas = checkDatas;
    }

    public Integer getCheckAmount() {
        return checkAmount;
    }

    public void setCheckAmount(Integer checkAmount) {
        this.checkAmount = checkAmount;
    }

    public String getCheckRemark() {
        return checkRemark;
    }

    public void setCheckRemark(String checkRemark) {
        this.checkRemark = checkRemark;
    }
}
