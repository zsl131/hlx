package com.zslin.multi.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "m_moneybag_detail")
@Data
public class MoneybagDetail {

    public static final String FLAG_IN = "1"; //入账
    public static final String FLAG_OUT = "2"; //出账

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "store_sn")
    private String storeSn;

    @Column(name = "store_id")
    private Integer storeId;

    @Column(name = "store_name")
    private String storeName;

    @Column(name = "bag_id")
    private Integer bagId;

    private String name;

    private String phone;

    /** 操作金额 */
    private Float money=0f;

    /** 当前剩余金额 */
    private Float surplus = 0f;

    @Column(name = "create_day")
    private String createDay;

    /** 格式 ：yyyyMMdd */
    @Column(name = "create_date")
    private String createDate;

    @Column(name = "create_time")
    private String createTime;

    @Column(name = "create_long")
    private Long createLong;

    /** 标记；1-入账；2-出账 */
    private String flag;

    /** 操作店铺ID */
    @Column(name = "opt_store_id")
    private Integer optStoreId;

    @Column(name = "opt_store_sn")
    private String optStoreSn;

    @Column(name = "opt_store_name")
    private String optStoreName;

    @Lob
    private String reason;

    /** 冻结标记 */
    private String freezeFlag = "0";

    /** 消费应付金额 */
    private Float dealMoney = 0f;
}
