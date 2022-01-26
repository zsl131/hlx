package com.zslin.finance.model;

import lombok.Data;

import javax.persistence.*;

/**
 * 财务人员
 */
@Entity
@Table(name = "fin_finance_personal")
@Data
public class FinancePersonal {

    //老板类型
    public static final String TYPE_BOSS = "2";

    //财务人员
    public static final String TYPE_VOUCHER = "3";

    //报账人员
    public static final String TYPE_NORMAL = "1";

    //收货人员
    public static final String TYPE_CONFIRM = "0";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String phone;

    private String openid;

    private String nickname;

    /** 0-未设置类型（或标记人员）；1-报账人员；2-审核人员；3-财务人员 */
    private String type;

    private Integer accountId;

    private String signPath;

    /** 是否是确认收货人员(标记人员) */
    private String markFlag = "0";

    private String storeSn;

    private String storeName;

    /** 是否股东，1-表示可以查看财务信息 */
    private String isPartner = "0";

    /** 股东所参与的店铺 */
    private String partStores = "";

    /** 是否收银员 */
    private String isCasher = "0";

    /** 可收银的店铺 */
    private String cashStores = "";
}
