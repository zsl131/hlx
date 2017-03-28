package com.zslin.web.model;

import javax.persistence.*;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/1 14:03.
 * 积分规则
 */
@Entity
@Table(name = "t_score_rule")
public class ScoreRule {
    //对食品点赞
    public static final String GOOD_FOOD = "GOOD-FOOD";
    //初次关注
    public static final String INIT = "INIT";
    //评论食品
    public static final String COMMENT_FOOD = "COMMENT-FOOD";
    //发消息
    public static final String SEND_MESSAGE = "SEND-MESSAGE";
    //分享平台
    public static final String SHARE = "SHARE";
    //分享给朋友
    public static final String SHARE_FRIEND = "SHARE-FRIEND";
    //拉取用户
    public static final String PULL_USER = "PULL-USER";
    //绑定手机号码
    public static final String BIND_PHONE = "BIND-PHONE";

    //优秀评论
    public static final String EXCELLET_COMMENT = "EXCELLET-COMMENT";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    //编码
    private String code;
    //名称，如分享文章得分
    private String name;
    //得分数量，如1
    private Integer score=1;
    //每天最高单项得分次数
    private Integer amount=1;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
