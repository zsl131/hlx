package com.zslin.stock.model;

import com.zslin.web.vo.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * Created by zsl on 2018/5/21.
 * 物品预录入
 */
@Entity
@Table(name = "s_preenter")
public class Preenter extends BaseEntity {

    /** 操作者姓名 */
    @Column(name = "operator_name")
    private String operatorName;

    /** 操作者电话 */
    @Column(name = "operator_phone")
    private String operatorPhone;

    /** 操作者Openid */
    @Column(name = "operator_openid")
    private String operatorOpenid;

    /** 操作批次编号 */
    @Column(name = "batch_no")
    private String batchNo;

    /** 预计需要几天到货，时间到后系统自动通知 */
    @Column(name = "need_days")
    private Integer needDays;

    /** 状态，-1-逾期未处理；0-新增；1-完成 */
    private String status;

    private Integer no;

    @Lob
    private String datas;

    private Integer amount;

    @Column(name = "amount_true")
    private Integer amountTrue;

    private String remark;

    @Column(name = "check_remark")
    private String checkRemark;

    @Column(name = "check_datas")
    private String checkDatas;

    @Column(name = "check_name")
    private String checkName;

    @Column(name = "check_phone")
    private String checkPhone;

    @Column(name = "check_openid")
    private String checkOpenid;

    /** 预计到货日期 */
    @Column(name = "maybe_day")
    private String maybeDay;

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

    public String getMaybeDay() {
        return maybeDay;
    }

    public void setMaybeDay(String maybeDay) {
        this.maybeDay = maybeDay;
    }

    public String getCheckDatas() {
        return checkDatas;
    }

    public void setCheckDatas(String checkDatas) {
        this.checkDatas = checkDatas;
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

    public String getDatas() {
        return datas;
    }

    public void setDatas(String datas) {
        this.datas = datas;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCheckRemark() {
        return checkRemark;
    }

    public void setCheckRemark(String checkRemark) {
        this.checkRemark = checkRemark;
    }

    public Integer getNo() {
        return no;
    }

    public void setNo(Integer no) {
        this.no = no;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getOperatorPhone() {
        return operatorPhone;
    }

    public void setOperatorPhone(String operatorPhone) {
        this.operatorPhone = operatorPhone;
    }

    public String getOperatorOpenid() {
        return operatorOpenid;
    }

    public void setOperatorOpenid(String operatorOpenid) {
        this.operatorOpenid = operatorOpenid;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public Integer getNeedDays() {
        return needDays;
    }

    public void setNeedDays(Integer needDays) {
        this.needDays = needDays;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
