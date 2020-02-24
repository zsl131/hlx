package com.zslin.web.model;

import javax.persistence.*;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/26 16:02.
 * 微信端画廊
 */
@Entity
@Table(name = "wx_gallery")
public class Gallery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "pic_path")
    private String picPath;

    /** 状态 */
    private String status="0";

    /** 链接地址 */
    private String url;

    @Column(name = "order_no")
    private Integer orderNo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }
}
