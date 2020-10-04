package com.zslin.multi.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "m_discount_food")
@Data
public class DiscountFood {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer storeId;

    private String storeName;

    private String storeSn;

    @Column(name = "food_id")
    private Integer foodId;

    @Column(name = "food_name")
    private String foodName;
}
