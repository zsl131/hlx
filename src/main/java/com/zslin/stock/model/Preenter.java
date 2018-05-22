package com.zslin.stock.model;

import com.zslin.web.vo.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
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
