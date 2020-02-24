package com.zslin.meituan.model;

import javax.persistence.*;

/**
 * Created by 钟述林 393156105@qq.com on 2017/7/5 14:03.
 */
@Entity
@Table(name = "m_meituan_config")
public class MeituanConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "dep_id")
    private String depId;

    @Column(name = "sign_key")
    private String signKey;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDepId() {
        return depId;
    }

    public void setDepId(String depId) {
        this.depId = depId;
    }

    public String getSignKey() {
        return signKey;
    }

    public void setSignKey(String signKey) {
        this.signKey = signKey;
    }
}
