package com.zslin.weixin.model;

import lombok.Data;

import javax.persistence.*;

/**
 * 用于客人到店门前扫码领券
 *  - 每一可以领一次
 */
@Table(name = "wx_hlx_ticket")
@Entity
@Data
public class HlxTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 类型：1-10元券；2-午餐券；3-晚餐券；10-电子券
     */
    private String type;

    /**
     * 使用方式
     * 1-正常使用，价值多少，抵扣多少
     * 2-满减
     */
    @Column(name = "use_type")
    private String useType;

    /** 满减才有值，即满多少钱才可以使用这张券 */
    @Column(name = "reach_money")
    private Integer reachMoney;

    private String phone;

    private String openid;

    private String nickname;

    @Column(name = "account_id")
    private Integer accountId;

    @Column(name = "create_day")
    private String createDay;

    @Column(name = "create_time")
    private String createTime;

    @Column(name = "create_long")
    private Long createLong;

    /** 卡券名称，如10元抵价券 */
    private String ticketName;

    /** 卡券价值，即具体抵多少钱，单位元 */
    private Integer ticketWorth;

    /** 卡券编号 */
    @Column(name = "ticket_no")
    private String ticketNo;

    /** 状态 0-领取；1-已使用 */
    private String status = "0";

    /** 使用日期 */
    @Column(name = "use_day")
    private String useDay;

    @Column(name = "use_time")
    private String useTime;

    @Column(name = "use_long")
    private Long useLong;
}
