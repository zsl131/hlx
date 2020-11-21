package com.zslin.stock.model;

import com.zslin.web.vo.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * Created by zsl on 2018/5/28.
 * 库存盘点
 * 操作流程：
 *  - 由库管员发起盘点事件，选择一个复核员，
 *  - 复核员收到后开始填单，填单完成后再由发起人确认
 *  - 确认之后修改各物品库存
 */
@Entity
@Table(name = "s_stock_check")
public class StockCheck extends BaseEntity {

    @Column(name = "batch_no")
    private String batchNo;

    private Integer no;

    @Column(name = "check_name")
    private String checkName;

    @Column(name = "check_phone")
    private String checkPhone;

    @Column(name = "check_openid")
    private String checkOpenid;

    @Column(name = "verify_name")
    private String verifyName;

    @Column(name = "verify_phone")
    private String verifyPhone;

    @Column(name = "verify_openid")
    private String verifyOpenid;

    /** 状态，0-发起；1-复核员接单；2-复核员提交库存单；3-发起人确认 */
    private String status;

    @Lob
    private String datas;

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

    public String getCheckName() {
        return checkName;
    }

    public void setCheckName(String checkName) {
        this.checkName = checkName;
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

    public String getVerifyName() {
        return verifyName;
    }

    public void setVerifyName(String verifyName) {
        this.verifyName = verifyName;
    }

    public String getVerifyPhone() {
        return verifyPhone;
    }

    public void setVerifyPhone(String verifyPhone) {
        this.verifyPhone = verifyPhone;
    }

    public String getVerifyOpenid() {
        return verifyOpenid;
    }

    public void setVerifyOpenid(String verifyOpenid) {
        this.verifyOpenid = verifyOpenid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDatas() {
        return datas;
    }

    public void setDatas(String datas) {
        this.datas = datas;
    }
}
