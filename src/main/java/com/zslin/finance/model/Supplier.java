package com.zslin.finance.model;

import lombok.Data;

import javax.persistence.*;

/**
 * 供货商
 */
@Entity
@Table(name = "fin_supplier")
@Data
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /** 店铺名称 */
    private String shopName;

    /** 联系人姓名 */
    private String contactName;

    /** 供货名称，如：牛干巴 */
    private String supplyNames;

    private String phone1;

    private String phone2;

    private String address;

    /** 合作状态，1-在合作； 0-未合作 */
    private String status = "0";

    /** 是否配送，0-不配送； 1-配送 */
    private String isDelivery = "0";

    /** 备注 */
    @Lob
    private String remark;

    /** 价格备注 */
    @Lob
    private String priceRemark;

    private String createDay;

    private String createTime;

    private Long createLong;

    /** 操作人员 */
    private String optOpenid;

    private String optName;

    private String optPhone;
}
