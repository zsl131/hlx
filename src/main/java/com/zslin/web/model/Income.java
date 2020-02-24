package com.zslin.web.model;

import javax.persistence.*;

/**
 * Created by 钟述林 393156105@qq.com on 2017/8/30 8:43.
 * 收入
 */
@Entity
@Table(name = "t_income")
public class Income {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** 收入日期，格式yyyyMMdd */
    @Column(name = "come_day")
    private String comeDay;

    @Column(name = "come_year")
    private String comeYear;

    @Column(name = "come_month")
    private String comeMonth;

    @Column(name = "create_day")
    private String createDay;

    //现金
    private Float cash=0f;

    //微信
    private Float weixin=0f;

    //支付宝
    private Float alipay=0f;

    //美团
    private Float meituan=0f;

    //商场
    private Float market=0f;

    //飞凡
    private Float ffan=0f;

    //会员
    private Float member=0f;

    //其他
    private Float other=0f;

    //午餐券
    @Column(name = "noon_ticket")
    private Integer noonTicket=0;

    //晚餐券
    @Column(name = "night_ticket")
    private Integer nightTicket=0;

    //10元券
    @Column(name = "ten_ticket")
    private Integer tenTicket=0;

    //合计金额
    @Column(name = "total_money")
    private Double totalMoney=0.0;

    //收入类别，1-营业收入；2-其他收入
    private String type="1";

    @Column(name = "people_count")
    private Integer peopleCount;

    /** 来源于客户端 */
    @Column(name = "from_client")
    private String fromClient;

    public Integer getPeopleCount() {
        return peopleCount;
    }

    public void setPeopleCount(Integer peopleCount) {
        this.peopleCount = peopleCount;
    }

    public String getFromClient() {
        return fromClient;
    }

    public void setFromClient(String fromClient) {
        this.fromClient = fromClient;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getComeDay() {
        return comeDay;
    }

    public void setComeDay(String comeDay) {
        this.comeDay = comeDay;
    }

    public String getComeYear() {
        return comeYear;
    }

    public void setComeYear(String comeYear) {
        this.comeYear = comeYear;
    }

    public String getComeMonth() {
        return comeMonth;
    }

    public void setComeMonth(String comeMonth) {
        this.comeMonth = comeMonth;
    }

    public String getCreateDay() {
        return createDay;
    }

    public void setCreateDay(String createDay) {
        this.createDay = createDay;
    }

    public Float getCash() {
        return cash;
    }

    public void setCash(Float cash) {
        this.cash = cash;
    }

    public Float getWeixin() {
        return weixin;
    }

    public void setWeixin(Float weixin) {
        this.weixin = weixin;
    }

    public Float getAlipay() {
        return alipay;
    }

    public void setAlipay(Float alipay) {
        this.alipay = alipay;
    }

    public Float getMeituan() {
        return meituan;
    }

    public void setMeituan(Float meituan) {
        this.meituan = meituan;
    }

    public Float getMarket() {
        return market;
    }

    public void setMarket(Float market) {
        this.market = market;
    }

    public Float getFfan() {
        return ffan;
    }

    public void setFfan(Float ffan) {
        this.ffan = ffan;
    }

    public Float getMember() {
        return member;
    }

    public void setMember(Float member) {
        this.member = member;
    }

    public Float getOther() {
        return other;
    }

    public void setOther(Float other) {
        this.other = other;
    }

    public Integer getNoonTicket() {
        return noonTicket;
    }

    public void setNoonTicket(Integer noonTicket) {
        this.noonTicket = noonTicket;
    }

    public Integer getNightTicket() {
        return nightTicket;
    }

    public void setNightTicket(Integer nightTicket) {
        this.nightTicket = nightTicket;
    }

    public Integer getTenTicket() {
        return tenTicket;
    }

    public void setTenTicket(Integer tenTicket) {
        this.tenTicket = tenTicket;
    }

    public Double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(Double totalMoney) {
        this.totalMoney = totalMoney;
    }
}
