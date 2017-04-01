package com.zslin.web.model;

import com.zslin.web.vo.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/2 23:40.
 * 文章
 */
@Entity
@Table(name = "t_article")
public class Article extends BaseEntity {

    private String title;

    private String guide;

    /** markdown的内容 */
    @Lob
    @Column(name = "md_content")
    private String mdContent;

    @Lob
    private String content;

    @Column(name = "order_no")
    private Integer orderNo;

    /** 是否当用户关注时推送 */
    @Column(name = "is_first")
    private Integer isFirst=0;

    /** 图标，用于在推送时显示 */
    @Column(name = "pic_path")
    private String picPath;

    @Column(name = "read_count")
    private Integer readCount=0;

    /** 是否显示 */
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getReadCount() {
        return readCount;
    }

    public void setReadCount(Integer readCount) {
        this.readCount = readCount;
    }

    public String getMdContent() {
        return mdContent;
    }

    public void setMdContent(String mdContent) {
        this.mdContent = mdContent;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGuide() {
        return guide;
    }

    public void setGuide(String guide) {
        this.guide = guide;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getIsFirst() {
        return isFirst;
    }

    public void setIsFirst(Integer isFirst) {
        this.isFirst = isFirst;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }
}
