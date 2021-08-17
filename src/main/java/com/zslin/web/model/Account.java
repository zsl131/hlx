package com.zslin.web.model;

import com.zslin.web.vo.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by 钟述林 393156105@qq.com on 2017/1/23 11:31.
 */
@Entity
@Table(name = "t_account")
public class Account extends BaseEntity {

    private String nickname;

    private String name;

    private String headimgurl;

    private String sex;

    private String phone;

    private String identity;

    private String birthday;

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    @Column(name = "bind_phone")
    private String bindPhone="0";

    /** 状态：0-取消关注；1-关注中 */
    private String status;

    /**
     * 类型
     * 0-顾客；
     * 1-员工；
     * 2-部门经理；
     * 5-股东；
     * 10-超级管理人员
     */
    private String type;

    /**
     * 被拉取用户的id，即是被哪个用户拉进来的
     */
    @Column(name = "follow_user_id")
    private Integer followUserId=0;

    /**
     * 被摘取用户的昵称，即是被哪个用户摘取进来的
     */
    @Column(name = "follow_user_name")
    private String followUserName;

    /** 是否为办卡用户,0-非会员；1-会员；2-会员过期，即账户余额为0 */
    @Column(name = "has_card")
    private String hasCard="0";

    /** 关注日期 */
    @Column(name = "follow_date")
    private Date followDate;

    private String unionid;

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public String getBindPhone() {
        return bindPhone;
    }

    public void setBindPhone(String bindPhone) {
        this.bindPhone = bindPhone;
    }

    public Date getFollowDate() {
        return followDate;
    }

    public void setFollowDate(Date followDate) {
        this.followDate = followDate;
    }

    public String getHasCard() {
        return hasCard;
    }

    public void setHasCard(String hasCard) {
        this.hasCard = hasCard;
    }

    public String getFollowUserName() {
        return followUserName;
    }

    public void setFollowUserName(String followUserName) {
        this.followUserName = followUserName;
    }

    public Integer getFollowUserId() {
        return followUserId;
    }

    public void setFollowUserId(Integer followUserId) {
        this.followUserId = followUserId;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    private String openid;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
