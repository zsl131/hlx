package com.zslin.multi.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "m_moneybag_search_record")
@Data
public class MoneybagSearchRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String phone;

    private String name;

    private String createDay;

    private String createTime;

    private Long createLong;
}
