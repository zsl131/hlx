package com.zslin.meituan.model;

import javax.persistence.*;

/**
 * Created by 钟述林 393156105@qq.com on 2017/7/5 14:22.
 */
@Entity
@Table(name = "m_meituan_shop")
public class MeituanShop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Column(name = "poi_id")
    private String poiId;

    private String token;

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

    public String getPoiId() {
        return poiId;
    }

    public void setPoiId(String poiId) {
        this.poiId = poiId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
