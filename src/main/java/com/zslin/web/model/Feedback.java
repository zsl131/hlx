package com.zslin.web.model;

import com.zslin.web.vo.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by 钟述林 393156105@qq.com on 2017/1/23 11:39.
 */
@Entity
@Table(name = "t_feedback")
public class Feedback extends BaseEntity {

    /** 反馈者openid */
    private String openid;

    /** 反馈者Id */
    @Column(name = "account_id")
    private Integer accountId;

    /** 反馈者昵称 */
    private String nickname;

    /** 反馈者头像 */
    private String headimgurl;

    /** 反馈内容 */
    @Lob
    private String content;

    /** 状态，1-显示；0-隐藏 */
    private String status;

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

    /** 类型，text、image */
    private String type;

    /** 图片地址，如果type为image时 */
    @Column(name = "pic_url")
    private String picUrl;

    /** 媒体Id，如果type为image时 */
    @Column(name = "media_id")
    private String mediaId;

    /** 本地图片路径 */
    @Column(name = "file_path")
    private String filePath;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
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
