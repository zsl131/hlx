package com.zslin.web.model;

import com.zslin.web.vo.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by 钟述林 393156105@qq.com on 2017/1/23 11:34.
 */
@Entity
@Table(name = "t_category")
public class Category extends BaseEntity {

    private String name;

    @Column(name = "order_no")
    private Integer orderNo = 1;

    private String remark;

    @Column(name = "pic_path")
    private String picPath;

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public String getPicPath() {
        return picPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
