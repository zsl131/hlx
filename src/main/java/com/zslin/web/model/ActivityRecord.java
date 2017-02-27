package com.zslin.web.model;

import com.zslin.web.vo.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by 钟述林 393156105@qq.com on 2017/1/23 14:26.
 * 活动记录
 */
@Entity
@Table(name = "t_active_record")
public class ActivityRecord extends BaseEntity {

    private String openid;

    /** 对应用户Id */
    @Column(name = "account_id")
    private Integer accountId;

    /** 对应用户名称 */
    @Column(name = "account_name")
    private String accountName;

    /** 对应用户头像 */
    private String headimgurl;

    /** 活动Id */
    @Column(name = "act_id")
    private Integer actId;

    /** 活动标题 */
    @Column(name = "act_title")
    private String actTitle;

    /** 得奖奖品Id */
    @Column(name = "prize_id")
    private Integer prizeId;

    /** 得奖奖品名称 */
    @Column(name = "prize_name")
    private String prizeName;

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

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public Integer getActId() {
        return actId;
    }

    public void setActId(Integer actId) {
        this.actId = actId;
    }

    public String getActTitle() {
        return actTitle;
    }

    public void setActTitle(String actTitle) {
        this.actTitle = actTitle;
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
}
