package com.zslin.multi.model;

import lombok.Data;

import javax.persistence.*;

/**
 * 餐桌
 */
@Entity
@Table(name = "m_dining_table")
@Data
public class DiningTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer storeId;

    private String storeName;

    private String storeSn;

    private String name;

    private Integer orderNo;

    private String remark;
}
