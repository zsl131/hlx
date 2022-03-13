package com.zslin.business.dto;

import com.zslin.finance.model.FinanceDetail;
import com.zslin.web.model.Income;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 经营情况DTO对象
 */
@Data
public class BusinessDto {

    private String year;

    private String month;

    private String day;

    private String justDay;

    /** 收入金额 */
    private Double gotMoney = 0d;

    /** 是否存在收入登记 */
    private String gotFlag = "0";

    /** 支出金额 */
    private Double paidMoney = 0d;

    private Double surplusMoney = 0d;

    private List<Income> incomeList;

    private List<FinanceDetail> financeList;

    public BusinessDto() {
        incomeList = new ArrayList<>();
        financeList = new ArrayList<>();
    }
}
