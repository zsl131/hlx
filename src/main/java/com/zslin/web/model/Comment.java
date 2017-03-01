package com.zslin.web.model;

import com.zslin.web.vo.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by 钟述林 393156105@qq.com on 2017/1/23 11:44.
 */
@Entity
@Table(name = "t_comment")
public class Comment extends BaseEntity {

    /** 被评菜品Id */
    @Column(name = "food_id")
    private Integer foodId;

    /** 被评菜品名称 */
    @Column(name = "food_name")
    private String foodName;

    /** 被评菜品图片 */
    @Column(name = "food_pic")
    private String foodPic;

    /** 点评者openid */
    private String openid;

    /** 点评者Id */
    @Column(name = "account_id")
    private Integer accountId;

    /** 点评者昵称 */
    private String nickname;

    /** 点评者头像 */
    private String headimgurl;

    /** 点评内容 */
    private String content;

    /** 状态，1-显示；0-隐藏 */
    private String status="1";

    /** 觉得赞，1-赞；0-默认 */
    @Column(name = "is_good")
    private Integer isGood;

    /** 回复 */
    @Lob
    private String reply;

    /** 回复日期 */
    @Column(name = "reply_date")
    private Date replyDate;

    /** 回复日期，Long类型 */
    @Column(name = "reply_long")
    private Long replyLong;

    /** 回复日期，yyyy-MM-dd HH:mm:ss */
    @Column(name = "reply_time")
    private String replyTime;

    public Integer getFoodId() {
        return foodId;
    }

    public void setFoodId(Integer foodId) {
        this.foodId = foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodPic() {
        return foodPic;
    }

    public void setFoodPic(String foodPic) {
        this.foodPic = foodPic;
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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getIsGood() {
        return isGood;
    }

    public void setIsGood(Integer isGood) {
        this.isGood = isGood;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public Date getReplyDate() {
        return replyDate;
    }

    public void setReplyDate(Date replyDate) {
        this.replyDate = replyDate;
    }

    public Long getReplyLong() {
        return replyLong;
    }

    public void setReplyLong(Long replyLong) {
        this.replyLong = replyLong;
    }

    public String getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(String replyTime) {
        this.replyTime = replyTime;
    }
}
