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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String phone;

    private String openid;

    private String nickname;

    /** 1-报账人员；2-审核人员 */
    private String type;

    private Integer accountId;

    private String signPath;
}
