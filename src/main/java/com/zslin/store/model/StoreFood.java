package com.zslin.store.model;

import lombok.Data;

import javax.persistence.*;

/**
 * 用于在微信上展示店铺的菜品
 */
@Entity
@Table(name = "s_store_food")
@Data
public class StoreFood {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private Integer storeId;

    private String storeName;

    private String storeSn;

    private String name;

    private String picPath;

    private Float price;

    /** 点赞次数，或想吃欲望 */
    private Integer goodCount=0;

    /** 点击次数，进入到详情页算一次点击 */
    private Integer showCount=0;

    private String remark;

    /** 1-显示；0-隐藏 */
    private String status = "1";
}
