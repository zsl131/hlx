package com.zslin.card.model;

import javax.persistence.*;

/**
 * 卡券申请原因
 * Created by zsl on 2018/10/12.
 */
@Entity
@Table(name = "c_apply_reason")
public class ApplyReason {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** 原因 */
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
