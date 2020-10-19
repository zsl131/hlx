package com.zslin.multi.model;

import lombok.Data;

import javax.persistence.*;

/**
 * 钱包
 */
@Entity
@Table(name = "m_moneybag")
@Data
public class Moneybag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "store_sn")
    private String storeSn;

    @Column(name = "store_id")
    private Integer storeId;

    @Column(name = "store_name")
    private String storeName;

    private String name;

    private String phone;

    /** 剩余金额 */
    private Float money = 0f;

    private String password;

    @Column(name = "create_day")
    private String createDay;

    @Column(name = "create_time")
    private String createTime;

    @Column(name = "create_long")
    private Long createLong;
}
