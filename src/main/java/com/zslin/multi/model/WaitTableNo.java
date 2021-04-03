package com.zslin.multi.model;

import lombok.Data;

import javax.persistence.*;

/**
 * 等桌编号
 *  - 根据日期和cateFlag进行编排
 */
@Entity
@Table(name = "m_wait_table_no")
@Data
public class WaitTableNo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** 日期，格式如：yyyyMMdd */
    private String days;

    /** 对应TableCategory中的flag*/
    private String cateFlag;

    /** 当前编号数 */
    private Integer curNo;

    private String storeSn;

    private String storeName;
}
