package com.zslin.web.model;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by 钟述林 393156105@qq.com on 2017/8/30 8:43.
 * 收入
 */
@Entity
@Table(name = "t_income")
@Data
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

    private String createTime;

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
    private Integer peopleCount=0;

    /** 来源于客户端 */
    @Column(name = "from_client")
    private String fromClient;

    @Column(name = "desk_count")
    private Integer deskCount = 0;

    @Column(name = "store_id")
    private Integer storeId;

    @Column(name = "store_name")
    private String storeName;

    @Column(name = "store_sn")
    private String storeSn;

    private String openid;

    private String nickname;

    private String phone;

    private String token;

    /** 凭证地址 */
    private String ticketPath;
}
