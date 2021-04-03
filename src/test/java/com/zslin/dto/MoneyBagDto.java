package com.zslin.dto;

import lombok.Data;

/**
 * 钱包DTO对象
 */
@Data
public class MoneyBagDto {

    private String storeName;
    private String name;
    private String phone;
    private Integer money;

    public MoneyBagDto() {
    }

    public MoneyBagDto(String storeName, String name, String phone, Integer money) {
        this.storeName = storeName;
        this.name = name;
        this.phone = phone;
        this.money = money;
    }
}
