package com.zslin.web.dto;

import lombok.Data;

/**
 * 收入DTO对象
 */
@Data
public class IncomeDto {

    private Double totalMoney=0d;

    private Long days=0l;

    public IncomeDto(Double totalMoney, Long days) {
        this.totalMoney = totalMoney;
        this.days = days;
    }
}
