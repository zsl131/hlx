package com.zslin.web.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/9 9:15.
 * 其他一些需要配置的东西
 */
@Entity
@Table(name = "t_rules")
public class Rules implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "store_sn")
    private String storeSn;

    @Column(name = "store_name")
    private String storeName;

    @Column(name = "store_id")
    private Integer storeId;

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
//    @Column(name = "friend_percent")
//    private Integer friendPercent;

    /**
     * 午餐晚餐的分隔时间点
     * 格式：HH:mm
     */
    private String spe;

    /** 入场多长时间后可退款，单位分钟 */
    @Column(name = "refund_min")
    private Integer refundMin;

    /** 几个积分兑换一元 */
    @Column(name = "score_money")
    private Integer scoreMoney;

    /** 积分抵扣比率，存整数，使用时除以100 */
    @Column(name = "score_deductible")
    private Integer scoreDeductible;

    /** 特殊开关 */
    @Column(name = "switch_flag")
    private String switchFlag = "0";

    public String getSwitchFlag() {
        return switchFlag;
    }

    public void setSwitchFlag(String switchFlag) {
        this.switchFlag = switchFlag;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public String getStoreSn() {
        return storeSn;
    }

    public void setStoreSn(String storeSn) {
        this.storeSn = storeSn;
    }

    public Integer getScoreDeductible() {
        return scoreDeductible;
    }

    public void setScoreDeductible(Integer scoreDeductible) {
        this.scoreDeductible = scoreDeductible;
    }

    public Integer getScoreMoney() {
        return scoreMoney;
    }

    public void setScoreMoney(Integer scoreMoney) {
        this.scoreMoney = scoreMoney;
    }

    public Integer getRefundMin() {
        return refundMin;
    }

    public void setRefundMin(Integer refundMin) {
        this.refundMin = refundMin;
    }

    public String getSpe() {
        return spe;
    }

    public void setSpe(String spe) {
        this.spe = spe;
    }

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

    /*public Integer getFriendPercent() {
        return friendPercent;
    }

    public void setFriendPercent(Integer friendPercent) {
        this.friendPercent = friendPercent;
    }*/
}
