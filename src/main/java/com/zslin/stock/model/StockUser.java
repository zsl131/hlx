package com.zslin.stock.model;

import lombok.Data;

import javax.persistence.*;

/**
 * 仓库管理员
 */
@Data
@Entity
@Table(name = "s_stock_user")
public class StockUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String phone;

    private String name;

    private String status = "0";

    private String storeSns = "";
}
