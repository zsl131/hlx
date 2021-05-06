package com.zslin.finance.dto;

import lombok.Data;

/**
 * 报账汇总DTO对象
 */
@Data
public class FinanceDetailDto {

    private String name;

    private String openid;

    private Double money;

    public FinanceDetailDto() {
    }

    public FinanceDetailDto(String name, String openid, Double money) {
        this.name = name;
        this.openid = openid;
        this.money = money;
    }
}
