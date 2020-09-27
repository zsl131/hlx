package com.zslin.multi.model;

import lombok.Data;

import javax.persistence.*;

/**
 * 店铺
 */
@Entity
@Table(name = "m_store")
@Data
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** 用于客户端请求的标识 */
    private String sn;

    private String name;

    private String address;

    private String phone;
}
