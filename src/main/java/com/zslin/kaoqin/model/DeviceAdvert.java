package com.zslin.kaoqin.model;

import javax.persistence.*;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/27 22:22.
 * 设备的广告图片
 */
@Entity
@Table(name = "k_device_advert")
public class DeviceAdvert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "order_no")
    private Integer orderNo;

    @Column(name = "pic_path")
    private String picPath;

    /** 是否使用 */
    private String status="1";

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }
}
