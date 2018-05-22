package com.zslin.stock.model;

import com.zslin.web.vo.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by zsl on 2018/5/21.
 * 申请购买物品
 * 由店内管理人申请，由老板审核
 */
@Entity
@Table(name = "s_goods_apply")
public class GoodsApply extends BaseEntity {

    /** 批次 */
    @Column(name = "batch_no")
    private String batchNo;

    /** 申请人电话 */
    @Column(name = "apply_phone")
    private String applyPhone;

    /** 申请人openid */
    @Column(name = "apply_openid")
    private String applyOpenid;

    /** 申请人姓名 */
    @Column(name = "apply_name")
    private String applyName;

    /** 状态，-1-驳回申请；0-申请状态；1-批准；2-结束 */
    private String status;

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
