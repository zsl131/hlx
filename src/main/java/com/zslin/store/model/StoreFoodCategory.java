package com.zslin.store.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "s_store_food_category")
@Data
public class StoreFoodCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private Integer storeId;

    private String storeName;

    private String storeSn;

    private String name;

    private Integer orderNo;

    /** 1-显示；0-隐藏 */
    private String status = "1";
}
