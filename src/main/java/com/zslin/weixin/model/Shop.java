package com.zslin.weixin.model;

import javax.persistence.*;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/21 23:57.
 * 微信门店
 */
@Entity
@Table(name = "t_shop")
public class Shop {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /** 对应微信门店的ShopId */
    @Column(name = "shop_id")
    private Integer shopId;

    /** 对应微信门店的ShopName */
    @Column(name = "shop_name")
    private String shopName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
}
