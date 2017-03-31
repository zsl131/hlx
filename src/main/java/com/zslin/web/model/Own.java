package com.zslin.web.model;

import com.zslin.web.vo.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by 钟述林 393156105@qq.com on 2017/1/23 14:04.
 * 我的物品
 */
@Entity
@Table(name = "t_own")
public class Own extends BaseEntity {

    /****/
    private String openid;

    /** 用户Id */
    @Column(name = "account_id")
    private Integer accountId;

    /** 用户名称 */
    @Column(name = "account_name")
    private String accountName;

    /** 礼品Id */
    @Column(name = "prize_id")
    private Integer prizeId;

    /** 礼品名称 */
    @Column(name = "prize_name")
    private String prizeName;

    /** 礼品类型，与Prize中的type一致 */
    @Column(name = "prize_type")
    private String prizeType;

    /** 状态 0-未使用，1-使用，2-过期 */
    private String status = "0";

    /** 有效期始，格式：yyyy-MM-dd */
    @Column(name = "start_time")
    private String startTime;

    /** 有效期止，格式：yyyy-MM-dd */
    @Column(name = "end_time")
    private String endTime;

    /** 有效期天数 */
    private Integer days;

    /** 来源，1-购买；2-活动 */
    private String source;

    /** 来源说明，如参加抽奖活动等 */
    @Column(name = "source_info")
    private String sourceInfo;

    /** 价值，如果是积分则是可抵积分数量；如果是抵价券，则是可抵现金金额，单位：分 */
    private Integer worth = 0;

    private String headimg;

    public String getPrizeType() {
        return prizeType;
    }

    public void setPrizeType(String prizeType) {
        this.prizeType = prizeType;
    }

    public String getHeadimg() {
        return headimg;
    }

    public void setHeadimg(String headimg) {
        this.headimg = headimg;
    }

    public Integer getWorth() {
        return worth;
    }

    public void setWorth(Integer worth) {
        this.worth = worth;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public Integer getPrizeId() {
        return prizeId;
    }

    public void setPrizeId(Integer prizeId) {
        this.prizeId = prizeId;
    }

    public String getPrizeName() {
        return prizeName;
    }

    public void setPrizeName(String prizeName) {
        this.prizeName = prizeName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSourceInfo() {
        return sourceInfo;
    }

    public void setSourceInfo(String sourceInfo) {
        this.sourceInfo = sourceInfo;
    }
}
