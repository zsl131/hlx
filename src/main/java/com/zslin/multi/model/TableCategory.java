package com.zslin.multi.model;

import lombok.Data;

import javax.persistence.*;

/**
 * 餐桌类型，如：大桌、中桌、小桌
 */
@Data
@Entity
@Table(name = "m_table_category")
public class TableCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** 名称 */
    private String name;

    /** 备注 */
    private String remark;

    /** 标记，如A、B、C */
    private String flag;

    private String storeSn;

    private String storeName;
}
