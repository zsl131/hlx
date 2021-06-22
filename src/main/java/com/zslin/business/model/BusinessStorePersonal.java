package com.zslin.business.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "business_store_personal")
public class BusinessStorePersonal {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;

    private String phone;

    /** 小程序openid */
    private String openid;

    private String storeName;

    private Integer storeId;

    /** 类型：1-核销人员；2-管理人员（可收取转款） */
    private String type;
}
