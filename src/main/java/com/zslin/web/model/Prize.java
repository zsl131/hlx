package com.zslin.web.model;

import com.zslin.web.vo.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * Created by 钟述林 393156105@qq.com on 2017/1/23 11:58.
 * 礼物-还在库中未送出去的叫礼物，送出去的叫礼品
 */
@Entity
@Table(name = "t_prize")
public class Prize extends BaseEntity {

    /** 名称 */
    private String name;

    /** 图片 */
    @Column(name = "pic_path")
    private String picPath;

    /** 说明 */
    @Lob
    private String remark;

    /**
     * 类型
     * 1-现金券
     * 2-就餐券
     * 3-礼品
     */
    private String type;

    /**
     * 状态，1-可以使用；0-不可用
     */
    private String status;

    /** 数量 */
    private Integer amount = 0;

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
