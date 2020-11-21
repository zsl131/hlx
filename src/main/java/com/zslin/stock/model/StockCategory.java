package com.zslin.stock.model;

import javax.persistence.*;

/**
 * Created by zsl on 2018/5/21.
 * 库存分类，如：鸡副、鸭副等
 */
@Entity
@Table(name = "s_stock_category")
public class StockCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** 名称 */
    private String name;

    /** 名称缩写 */
    @Column(name = "name_short")
    private String nameShort;

    /** 名称全拼 */
    @Column(name = "name_full")
    private String nameFull;

    /** 位置类型，1-冻库；2-仓库；3-店内 */
    @Column(name = "location_type")
    private String locationType;


    /** 店铺ID */
    private Integer storeId;

    /** 店铺名称 */
    private String storeName;

    /** 店铺SN */
    private String storeSn;

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreSn() {
        return storeSn;
    }

    public void setStoreSn(String storeSn) {
        this.storeSn = storeSn;
    }

    public String getNameShort() {
        return nameShort;
    }

    public void setNameShort(String nameShort) {
        this.nameShort = nameShort;
    }

    public String getNameFull() {
        return nameFull;
    }

    public void setNameFull(String nameFull) {
        this.nameFull = nameFull;
    }

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

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }
}
