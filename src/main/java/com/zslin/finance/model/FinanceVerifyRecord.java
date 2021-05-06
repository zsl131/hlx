package com.zslin.finance.model;

import lombok.Data;

import javax.persistence.*;

/**
 * 审核记录
 */
@Entity
@Table(name = "fin_finance_verify_record")
@Data
public class FinanceVerifyRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer detailId;

    private String storeSn;

    private String storeName;

    private String createDay;

    private String createTime;

    private Long createLong;

    private String optName;

    private String optPhone;

    private String optOpenid;

    /**
     * 0-添加；1-老板审核；2-收货确认；3-财务审核
     */
    private String type;

    /** 类型名称 */
    private String typeName;

    @Lob
    private String reason;

    /** 刚添加 */
    public static final String TYPE_NOTHING = "0";

    /** 老板审核 */
    public static final String TYPE_BOSS = "1";

    /** 收货确认 */
    public static final String TYPE_CONFIRM = "2";

    /** 财务审核 */
    public static final String TYPE_VOUCHER = "3";
}
