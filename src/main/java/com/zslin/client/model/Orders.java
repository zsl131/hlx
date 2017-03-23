package com.zslin.client.model;

import javax.persistence.*;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/13 15:24.
 * 订单
 */
@Entity
@Table(name = "t_orders")
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /** 订单编号 */
    private String no;

    /** 收银员姓名 */
    @Column(name = "cashier_name")
    private String cashierName;

    /** 收银员电话 */
    @Column(name = "cashier_phone")
    private String cashierPhone;

    /** 创建时间，yyyy-MM-dd HH:mm:ss */
    @Column(name = "create_time")
    private String createTime;

    /** 创建时间，Long类型 */
    @Column(name = "create_long")
    private Long createLong;

    /** 创建日期，yyyy-MM-dd */
    @Column(name = "create_day")
    private String createDay;

    /** 收费成人数量 */
    @Column(name = "people_count")
    private Integer peopleCount;

    /** 半价人数 */
    @Column(name = "half_count")
    private Integer halfCount;

    /** 免费儿童人数 */
    @Column(name = "child_count")
    private Integer childCount;

    /** 压金，金额 */
    @Column(name = "bond_money")
    private Float bondMoney;

    /**
     * 状态，要考虑美团、微信和到店下单
     * 0-已下单
     * 1-已付款（只有微信下单存在）
     * 2-就餐中（微信、美团下单的到店后交压金进场即为此状态）
     * 3-美团确认（中有美团下单存在）
     * 4-压金全退（无浪费）
     * 5-退部份压金（有浪费）
     * 6-老板确认，可以付款
     * -1-已退票（收银员操作）
     * -2-已取消
     * -3-被老板驳回
     */
    private String status;

    /** 结束时间，yyyy-MM-dd HH:mm:ss，退票或退压金都表示结束 */
    @Column(name = "end_time")
    private String endTime;

    /** 结束时间，Long类型 */
    @Column(name = "end_long")
    private Long endLong;

    /** 剩余的压金，如果有扣 */
    @Column(name = "surplus_bond")
    private Float surplusBond;

    /** 类型，1-收银员下单；2-微信下单；3-美团下单；4-友情价下单；5-会员订单；6-卡券订单 */
    private String type;

    /** 只针对微信下单订单 */
    private String openid;

    /** 美团编号 */
    @Column(name = "meituan_num")
    private String meituanNum;

    /** 消费者的手机号码，主要用于当有折扣时推送给管理员 */
    private String phone;

    /** 消费者的姓名，主要用于当有折扣时推送给管理员 */
    private String name;

    /** 只针对微信下单订单 */
    @Column(name = "account_id")
    private Integer accountId;

    /** 优惠金额 */
    @Column(name = "discount_money")
    private Float discountMoney=0f;

    /** 优惠原因，如果是友情价，则是股东电话号码(必须) */
    @Column(name = "discount_reason")
    private String discountReason;

    /** 优惠类型；0-无优惠；1-积分抵价；2-友情价；3-抵价券 */
    @Column(name = "discount_type")
    private String discountType;

    /** 付款方式，只有到店下单才会有此值；1-现金；2-刷卡；3-微信支付；4-支付宝支付 */
    @Column(name = "pay_type")
    private String payType;

    /** 级别，1-晚餐；2-午餐 */
    private String level;

    /** 收银金额，只是餐费，不包含压金 */
    @Column(name = "total_money")
    private Float totalMoney;

    /** 入场时间，格式：yyyy-MM-dd HH:mm:ss */
    @Column(name = "entry_time")
    private String entryTime;

    /** 入场时间，Long类型 */
    @Column(name = "entry_long")
    private Long entryLong;

    /** 退票人员（收银员） */
    @Column(name = "retreat_name")
    private String retreatName;

    /** 退票时间，yyyy-MM-dd HH:mm:ss */
    @Column(name = "retreat_time")
    private String retreatTime;

    /** 退票原因 */
    @Column(name = "retreatReason")
    private String retreatReason;

    /** 票价 */
    private Float price;

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Integer getHalfCount() {
        return halfCount;
    }

    public void setHalfCount(Integer halfCount) {
        this.halfCount = halfCount;
    }

    public String getRetreatReason() {
        return retreatReason;
    }

    public void setRetreatReason(String retreatReason) {
        this.retreatReason = retreatReason;
    }

    public String getRetreatName() {
        return retreatName;
    }

    public void setRetreatName(String retreatName) {
        this.retreatName = retreatName;
    }

    public String getRetreatTime() {
        return retreatTime;
    }

    public void setRetreatTime(String retreatTime) {
        this.retreatTime = retreatTime;
    }

    public String getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(String entryTime) {
        this.entryTime = entryTime;
    }

    public Long getEntryLong() {
        return entryLong;
    }

    public void setEntryLong(Long entryLong) {
        this.entryLong = entryLong;
    }

    public Float getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(Float totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getCreateDay() {
        return createDay;
    }

    public void setCreateDay(String createDay) {
        this.createDay = createDay;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getMeituanNum() {
        return meituanNum;
    }

    public void setMeituanNum(String meituanNum) {
        this.meituanNum = meituanNum;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public Float getDiscountMoney() {
        return discountMoney;
    }

    public void setDiscountMoney(Float discountMoney) {
        this.discountMoney = discountMoney;
    }

    public String getDiscountReason() {
        return discountReason;
    }

    public void setDiscountReason(String discountReason) {
        this.discountReason = discountReason;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getCashierName() {
        return cashierName;
    }

    public void setCashierName(String cashierName) {
        this.cashierName = cashierName;
    }

    public String getCashierPhone() {
        return cashierPhone;
    }

    public void setCashierPhone(String cashierPhone) {
        this.cashierPhone = cashierPhone;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Long getCreateLong() {
        return createLong;
    }

    public void setCreateLong(Long createLong) {
        this.createLong = createLong;
    }

    public Integer getPeopleCount() {
        return peopleCount;
    }

    public void setPeopleCount(Integer peopleCount) {
        this.peopleCount = peopleCount;
    }

    public Integer getChildCount() {
        return childCount;
    }

    public void setChildCount(Integer childCount) {
        this.childCount = childCount;
    }

    public Float getBondMoney() {
        return bondMoney;
    }

    public void setBondMoney(Float bondMoney) {
        this.bondMoney = bondMoney;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Long getEndLong() {
        return endLong;
    }

    public void setEndLong(Long endLong) {
        this.endLong = endLong;
    }

    public Float getSurplusBond() {
        return surplusBond;
    }

    public void setSurplusBond(Float surplusBond) {
        this.surplusBond = surplusBond;
    }
}
