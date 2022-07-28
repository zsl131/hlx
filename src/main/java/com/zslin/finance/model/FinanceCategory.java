package com.zslin.finance.model;

import lombok.Data;

import javax.persistence.*;

/** 分类，这个分类属于三店通用 */
@Entity
@Table(name = "fin_finance_category")
@Data
public class FinanceCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** 适用于哪些店铺 */
    private String storeSns;

    private String name;

    private String remark;

    private String firstSpell;

    private String longSpell;
}
