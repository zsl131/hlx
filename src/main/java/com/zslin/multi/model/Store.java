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

    /** 头像 */
    private String logoPath;

    /** 全称 */
    private String longName;

    /** 备注信息，用于显示在APP上 */
    private String remark;

    /** 外卖开启标识，0-未开启；1-开启 */
    private String outFoodFlag="0";
}
