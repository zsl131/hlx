package com.zslin.web.model;

import com.zslin.web.vo.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * Created by 钟述林 393156105@qq.com on 2017/1/23 14:18.
 * 活动
 */
@Entity
@Table(name = "t_activity")
public class Activity extends BaseEntity {

    /** 活动标题 */
    private String title;

    /** 奖品描述 */
    @Column(name = "prize_desc")
    @Lob
    private String prizeDesc;

    /** 活动描述 */
    @Lob
    private String remark;

    /** 活动日期始，格式：yyyy-MM-dd HH:mm:ss */
    @Column(name = "start_time")
    private String startTime;

    /** 活动日期止，格式：yyyy-MM-dd HH:mm:ss */
    private String endTime;

    /** 活动总天数 */
    private Integer days;

    /** 状态，0-未开始；1-进行中；2-已结束 */
    private String status = "0";

    /** 参与条件 */
    private String conditions;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrizeDesc() {
        return prizeDesc;
    }

    public void setPrizeDesc(String prizeDesc) {
        this.prizeDesc = prizeDesc;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }
}
