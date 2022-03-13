package com.zslin.business.tools;

import com.zslin.basic.tools.DateTools;
import com.zslin.basic.tools.NormalTools;
import com.zslin.business.dto.BusinessDto;
import com.zslin.finance.dao.IFinanceDetailDao;
import com.zslin.finance.model.FinanceDetail;
import com.zslin.web.model.Income;
import com.zslin.web.service.IIncomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BusinessTools {

    @Autowired
    private IIncomeService incomeService;

    @Autowired
    private IFinanceDetailDao financeDetailDao;

    /**
     *
     * @param storeSn 店铺Sn
     * @param month 格式如：yyyyMM
     * @param lastSurplusMoney 上月结余
     * @return
     */
    public List<BusinessDto> buildDto(String storeSn, String month, double lastSurplusMoney) {
        String year = month.substring(0, 4);
        List<BusinessDto> resultList = new ArrayList<>();
        int lastDay = DateTools.queryCurrentMonthLength(month);

        List<Income> incomeList = incomeService.findByMonth(storeSn, month);
        List<FinanceDetail> financeDetailList = financeDetailDao.findDetailByStoreSn(storeSn, month);

        for(int i=1;i<=lastDay; i++) {
            BusinessDto dto = new BusinessDto();
//            dto.setDay(buildDay(month, i));
            dto.setJustDay(buildDay(i));
            dto.setDay(month+dto.getJustDay());
            dto.setYear(year);
            dto.setMonth(month);
            buildIncome(dto, incomeList);
            buildIncomeMoney(dto);

            buildFinance(dto, financeDetailList);
            buildFinanceMoney(dto);

            dto.setSurplusMoney(NormalTools.retain2Decimal(lastSurplusMoney + dto.getGotMoney() - dto.getPaidMoney()));
            lastSurplusMoney = dto.getSurplusMoney(); //重新赋值剩余金额
            resultList.add(dto);
        }

        /*for(BusinessDto dto: resultList) {
            dto.setSurplusMoney(lastSurplusMoney + dto.getGotMoney() - dto.getPaidMoney());
            lastSurplusMoney = dto.getSurplusMoney(); //重新赋值剩余金额
        }*/
        return resultList;
    }

    /** 构建收入明细 */
    private void buildIncome(BusinessDto dto, List<Income> incomeList) {
        String dtoDay = dto.getDay();
        for(Income income: incomeList) {
            if(income.getComeDay().equals(dtoDay)) {
                dto.setGotFlag("1");
                dto.getIncomeList().add(income);
            }
        }
    }

    /** 构建收入金额 */
    private void buildIncomeMoney(BusinessDto dto) {
        List<Income> list = dto.getIncomeList();
        double total = 0d;
        for(Income in: list) {
            total += in.getTotalMoney();
        }
        dto.setGotMoney(NormalTools.retain2Decimal(total));
    }

    /** 构建支出明细 */
    private void buildFinance(BusinessDto dto, List<FinanceDetail> financeDetailList) {
        String dtoDay = dto.getDay();
        for(FinanceDetail fd: financeDetailList) {
            if(fd.getTargetDay().equals(dtoDay)) {
                dto.getFinanceList().add(fd);
            }
        }
    }

    /** 构建支出金额 */
    private void buildFinanceMoney(BusinessDto dto) {
        List<FinanceDetail> detailList = dto.getFinanceList();
        double total = 0d;
        for(FinanceDetail fd: detailList) {
            total += fd.getTotalMoney();
        }
        dto.setPaidMoney(NormalTools.retain2Decimal(total));
    }

    private String buildDay(String month, int day) {
        return month + (day<10?"0":"") + day;
    }

    private String buildDay(int day) {
        return (day<10?"0":"") + day;
    }
}
