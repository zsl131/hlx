package com.zslin.web.model;

import com.zslin.web.vo.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/7 11:30.
 * 事件推送记录
 */
@Entity
@Table(name = "t_event_record")
public class EventRecord extends BaseEntity {

    private String title;

    @Column(name = "title_remark")
    private String titleRemark;

    @Column(name = "event_date")
    private String eventDate;

    private String remark;

    private String openid;

    private String url;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleRemark() {
        return titleRemark;
    }

    public void setTitleRemark(String titleRemark) {
        this.titleRemark = titleRemark;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
