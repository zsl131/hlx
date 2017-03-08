package com.zslin.web.model;

import javax.persistence.*;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/7 10:30.
 * 会员等级
 */
@Entity
@Table(name = "t_member_level")
public class MemberLevel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /** 等级 */
    private Integer level=1;

    /** 名称 */
    private String name="";

    /** 折扣，未除以100的折扣率，正整数 */
    private Integer discount=100;

    /** 充值金额，单位：元 */
    @Column(name = "charge_money")
    private Integer chargeMoney=0;

    /** 赠送金额，单位，元 */
    @Column(name = "give_money")
    private Integer giveMoney=0;

    /** 状态，是否启用，1-启用；0-停用 */
    private String status="0";

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public Integer getChargeMoney() {
        return chargeMoney;
    }

    public void setChargeMoney(Integer chargeMoney) {
        this.chargeMoney = chargeMoney;
    }

    public Integer getGiveMoney() {
        return giveMoney;
    }

    public void setGiveMoney(Integer giveMoney) {
        this.giveMoney = giveMoney;
    }
}
