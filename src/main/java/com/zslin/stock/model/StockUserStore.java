package com.zslin.stock.model;

import lombok.Data;

import javax.persistence.*;

/**
 * 仓库管理员与店铺对应关系
 */
@Data
@Entity
@Table(name = "s_stock_user_store")
public class StockUserStore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer userId;

    private String userPhone;

    private Integer storeId;

    private String storeSn;
}
