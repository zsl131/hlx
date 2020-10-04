package com.zslin.multi.model;

import lombok.Data;

import javax.persistence.*;

/**
 * 打折标记，用于是否开启打折
 */
@Data
@Entity
@Table(name = "m_discount_model")
public class DiscountModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer storeId;

    private String storeName;

    private String storeSn;

    /** 百分比 */
    private Float percent;

    /** 是否开启；0-关闭；1-开启 */
    private String status;

    /** 开始时间 */
    @Column(name = "start_time")
    private String startTime;
}
