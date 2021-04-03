package com.zslin.finance.model;

import lombok.Data;

import javax.persistence.*;

/** 财务明细 */
@Entity
@Table(name = "fin_finance_detail")
@Data
public class FinanceDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String storeSn;

    private String storeName;

    private String username;

    private String userOpenid;

    private String userPhone;

    private String userSignPath;

    /** 对应personal的ID */
    private Integer userId;

    private String cateName;

    private String cateId;

    /** 账目名称 */
    private String title;

    private String createDay;

    private String createTime;

    private Long createLong;

    private String verifyOpenid;

    private String verifyName;

    private String verifyPhone;

    private String verifySignPath;

    /**
     * 状态，-1-取消；0-未提交审核；1-审核中；2-审核通过；3-驳回
     */
    private String status = "0";

    @Lob
    private String verifyReason;

    private String verifyDay;

    private String verifyTime;

    private Long verifyLong;

    /** 标记，1-进账；-1-报账 */
    private String flag="-1";

    private Float price;

    /** 数量，有可能有小数，如称重产品 */
    private Float amount;

    private Float totalMoney;

    /** 提交标记 */
    private String stepFlag = "0";

    /** 打印标记 */
    private String printFlag = "0";
}
