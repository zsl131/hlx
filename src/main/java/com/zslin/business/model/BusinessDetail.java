package com.zslin.business.model;

import lombok.Data;

import javax.persistence.*;

/**
 * 经营数据详情
 */
@Data
@Entity
@Table(name = "business_detail")
public class BusinessDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String targetYear;

    private String targetMonth;

    private String storeSn;

    private String storeName;

    /** 经营结果，1-盈利；-1-亏损；0-持平 */
    private String flag;

    private String createDay;

    private String createTime;

    private Long createLong;

    /** 收入金额 */
    private Double gotMoney=0d;

    /** 支出金额 */
    private Double paidMoney=0d;

    /** 结余金额 */
    private Double surplusMoney=0d;

    /** 上月结余 */
    private Double preMonthMoney=0d;

    /** 账面结余 */
    private Double accountMoney=0d;

    /** 本月分账金额 */
    private Double settleMoney=0d;

    /** 是否公开；0-不公开；1-公开 */
    private String status = "0";
}
