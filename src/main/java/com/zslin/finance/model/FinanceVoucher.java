package com.zslin.finance.model;

import lombok.Data;

import javax.persistence.*;

/**
 * 财务凭证
 */
@Entity
@Table(name = "fin_finance_voucher")
@Data
public class FinanceVoucher {

    public static final String TARGET_TYPE_FIN = "1";

    public static final String TARGET_TYPE_INCOME = "2";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** 对应的申请ID */
    private Integer detailId;

    private String targetToken;

    /** 类型； 1-财务；2-入账 */
    private String targetType;

    /** 对应的照片路径 */
    private String picPath;

    /** 打印标记，0-未打印；1-已打印 */
    private String printFlag = "0";

    private String createDay;

    private String createTime;

    private Long createLong;

    /** 文件md5 */
    private String fileMd5;
}
