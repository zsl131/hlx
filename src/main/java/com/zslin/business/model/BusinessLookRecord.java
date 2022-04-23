package com.zslin.business.model;

import lombok.Data;

import javax.persistence.*;

/**
 * 浏览记录
 */
@Data
@Entity
@Table(name = "business_look_record")
public class BusinessLookRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String phone;

    private String targetYear;

    private String targetMonth;

    private String createDate;

    private String createTime;

    private Long createLong;

    private String storeSn;

    private String storeName;
}
