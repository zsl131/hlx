package com.zslin.finance.model;

import lombok.Data;

import javax.persistence.*;

/** 财务明细
 * 操作流程
 * 1、报账人员填报账申请
 *      status:
 *          -1-已取消
 *          0-填写了报账信息，还未提交审核
 *          1-提交待审核（等待老板审核）
 *          2-审核通过
 *          3-申请被驳回（verifyReason填驳回原因）
 *
 * 2、当status为2时，进入确认收货阶段
 *      confirmStatus:
 *          0-未指定人员确认
 *          1-已指定人员，待确认
 *          2-确认收货
 *          3-确认未收货（confirmReason填原因，可不填）
 * 3、当confirmStatus为2时，进入财务检查阶段（status和confirmStatus均为2时，才进入此阶段）
 *      voucherStatus:
 *          0-初始状态
 *          1-可以开始审核
 *          2-审核通过
 *          3-驳回（voucherReason填原因）
 */
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

    /** 指定财务日期 */
    private String targetDay;

    private String targetYear;

    private String targetMonth;

    private String createDay;

    private String createTime;

    private Long createLong;

    private String verifyOpenid;

    private String verifyName;

    private String verifyPhone;

    private String verifySignPath;

    /** 财务对应日期，格式：yyyy-MM-dd */
    private String finDay;

    /**
     * 状态，-1-取消；0-未提交审核；1-审核中；2-审核通过；3-驳回；
     * 流程：
     * 0-添加报账，还未上传凭证，也未提交审核
     * 1-已经提交审核，此时由老板审核
     * 2-审核通过；
     * 3-审核被驳回；1和4状态都可直接到3
     */
    private String status = "0";

    /** 以下属于老板审核 */
    @Lob
    private String verifyReason;

    private String verifyDay;

    private String verifyTime;

    private Long verifyLong;
    /** 以上属于老板审核 */

    /**
     * 收货确认状态，0-未指定确认；1-已指定，待确认；2-已确认收货；3-确认未收货
     */
    private String confirmStatus = "0";

    /** 以下是属于收货确认所需信息 */
    @Lob
    private String confirmReason;

    private String confirmDay;

    private String confirmTime;

    private Long confirmLong;

    private String confirmOpenid;

    private String confirmName;

    private String confirmSign;
    /** 以上是属于收货确认所需信息 */

    /**
     * 财务审核状态
     * 0-初始状态，还不需要审核
     * 1-待审核
     * 2-通过
     * 3-驳回
     */
    private String voucherStatus = "0";

    /** 以下属于财务审核信息 */
    private String voucherDay;

    @Lob
    private String voucherReason;

    private String voucherTime;

    private Long voucherLong;

    private String voucherOpenid;

    private String voucherName;

    private String voucherSign;
    /** 以上属于财务审核信息 */

    /** 标记，1-进账；-1-报账 */
    private String flag="-1";

    private Double price;

    /** 数量，有可能有小数，如称重产品 */
    private Float amount;

    private Double totalMoney;

    /** 提交标记 */
    private String stepFlag = "0";

    /** 打印标记 */
    private String printFlag = "0";
}
