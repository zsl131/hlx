package com.zslin.business.dto;

import com.zslin.basic.tools.NormalTools;
import lombok.Data;

@Data
public class BusinessMoneyDto {

    private double gotMoney = 0d;

    private double paidMoney = 0d;

    public BusinessMoneyDto() {}

    public BusinessMoneyDto(double gotMoney, double paidMoney) {
        this.gotMoney = NormalTools.retain2Decimal(gotMoney);
        this.paidMoney = NormalTools.retain2Decimal(paidMoney);
    }
}
