package com.zslin.business.model;

import lombok.Data;

import javax.persistence.*;

/**
 * 商家
 */
@Data
@Entity
@Table(name = "business_store")
public class BusinessStore {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;

    private String phone;

    private String address;

    /** 是否签约 */
    private String status;
}
