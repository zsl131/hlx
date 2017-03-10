package com.zslin.web.model;

import javax.persistence.*;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/9 9:15.
 * 其他一些需要配置的东西
 */
@Entity
@Table(name = "t_rules")
public class Rules {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /** 积分过期天数 */
    @Column(name = "score_overdue_days")
    private Integer scoreOverdueDays;

    /** 充值赠送积分比率，未除100的整数 */
    @Column(name = "charge_percent")
    private Integer chargePercent;

    /**
     * 亲情优惠比例，如果多个的话，中间用逗号隔开。
     * 值是未除100的整数
     */
    @Column(name = "friend_percent")
    private String friendPercent;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getScoreOverdueDays() {
        return scoreOverdueDays;
    }

    public void setScoreOverdueDays(Integer scoreOverdueDays) {
        this.scoreOverdueDays = scoreOverdueDays;
    }

    public Integer getChargePercent() {
        return chargePercent;
    }

    public void setChargePercent(Integer chargePercent) {
        this.chargePercent = chargePercent;
    }

    public String getFriendPercent() {
        return friendPercent;
    }

    public void setFriendPercent(String friendPercent) {
        this.friendPercent = friendPercent;
    }
}
