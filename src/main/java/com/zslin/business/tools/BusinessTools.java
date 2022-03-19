package com.zslin.business.tools;

import com.zslin.basic.tools.DateTools;
import com.zslin.basic.tools.NormalTools;
import com.zslin.business.dao.IBusinessDetailDao;
import com.zslin.business.dto.BusinessDto;
import com.zslin.business.dto.BusinessMoneyDto;
import com.zslin.business.model.BusinessDetail;
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

//    @Autowired
    private IIncomeService incomeService;

//    @Autowired
    private final IFinanceDetailDao financeDetailDao;

    private final IBusinessDetailDao businessDetailDao;

    @Autowired
    public BusinessTools(IIncomeService incomeService, IFinanceDetailDao financeDetailDao,
                         IBusinessDetailDao businessDetailDao) {
        this.incomeService = incomeService;
        this.financeDetailDao = financeDetailDao;
        this.businessDetailDao = businessDetailDao;
    }

    /**
     * 当month月报账发生变化时需要迭代往后面每个月进行修改
     * 当月剩余金额发生变化，后面所有生成的数据都可能有变化
     * @param storeSn 对应店铺
     * @param month 发生变化的月份
     */
    public void rebuildBusinessDetailForLoop(String storeSn, String month) {
//        System.out.println("===========BusinessTools.rebuildBusinessDetailForLoop===========");
//        System.out.println("storeSn: "+storeSn+"，month: "+month);
//        System.out.println("===========BusinessTools.rebuildBusinessDetailForLoop===========");
        //获取对应的Detail数据
        BusinessDetail detail = businessDetailDao.findByStoreSnAndTargetMonth(storeSn, month);
        int amount = 1;
        String nextMonth = NormalTools.otherMonthByMonth(month, amount);
        boolean isFirst = true;
        while(detail!=null) {
            rebuildBusinessDetailForLoop(detail, isFirst);
            detail = businessDetailDao.findByStoreSnAndTargetMonth(storeSn, nextMonth);
            nextMonth = NormalTools.otherMonthByMonth(nextMonth, amount);
            isFirst = false;
        }
    }

    private void rebuildBusinessDetailForLoop(BusinessDetail detail, boolean isFirst) {
        //如果为空表示没有没有对应数据，则不需要对后续数据进行操作
        if(detail!=null) {
            double preSurplusMoney = detail.getPreMonthMoney(); //上月结余
            if(!isFirst) {
                String preMonth = NormalTools.preMonthByMonth(detail.getTargetMonth()); //获取
                try {
                    preSurplusMoney = businessDetailDao.findPreMonthMoney(detail.getStoreSn(), preMonth);
                } catch (Exception e) {
                }
            }
            List<BusinessDto> businessDtoList = buildDto(detail.getStoreSn(), detail.getTargetMonth(), preSurplusMoney);
            BusinessMoneyDto moneyDto = buildMoney(businessDtoList);
            detail.setPreMonthMoney(preSurplusMoney);
            detail.setGotMoney(moneyDto.getGotMoney());
            detail.setPaidMoney(moneyDto.getPaidMoney());
            detail.setSurplusMoney(NormalTools.retain2Decimal(preSurplusMoney+moneyDto.getGotMoney()-moneyDto.getPaidMoney()));
            businessDetailDao.save(detail);
        }
    }

    /** 生成总金额 */
    public BusinessMoneyDto buildMoney(List<BusinessDto> dtoList) {
        double gotMoney = 0d, paidMoney = 0d;
        for(BusinessDto dto: dtoList) {
            gotMoney += dto.getGotMoney();
            paidMoney += dto.getPaidMoney();
        }
        return new BusinessMoneyDto(gotMoney, paidMoney);
    }

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
