package com.zslin.wx.tools;

import com.zslin.client.tools.ClientFileTools;
import com.zslin.web.model.Income;
import com.zslin.web.service.IIncomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by 钟述林 393156105@qq.com on 2017/8/30 17:03.
 */
@Component
public class IncomeNoticeTools {

    @Autowired
    private EventTools eventTools;

    @Autowired
    private AccountTools accountTools;

    @Autowired
    private IIncomeService incomeService;

    public void notice(Income income) {
        String month = income.getComeMonth();
        String storeSn = income.getStoreSn();
        storeSn = (storeSn==null || "".equals(storeSn.trim()))?ClientFileTools.HLX_SN:storeSn;
        Double avg = incomeService.average(storeSn, month);
        Integer moreThan = incomeService.moreThan(storeSn, month, 20000d);
        Double totalMoney = incomeService.totalMoney(storeSn, month);

        List<String> openids = ClientFileTools.HLX_SN.equals(storeSn)?
                accountTools.getOpenid(AccountTools.ADMIN, AccountTools.PARTNER):AccountTools.defaultAdmins(); //获取管理员
        StringBuffer sb = new StringBuffer();

        sb.append("店铺名称：").append(ClientFileTools.BUILD_STORE_NAME(storeSn)).append("\\n").
                append("当天收入：").append(new BigDecimal(income.getTotalMoney()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()).append(" 元\\n").
                append("平均每天：").append(avg==null?0:new BigDecimal(avg).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()).append(" 元\\n").
                append("超过两万：").append(moreThan==null?0:moreThan).append(" 天\\n").
                append("当月收入：").append(totalMoney==null?0:new BigDecimal(totalMoney).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()).append(" 元");
        eventTools.eventRemind(openids, income.getComeDay()+"入账通知", "1".equals(income.getType())?"营业收入":"其他收入", income.getComeDay(), sb.toString(), "/wx/income/show?id="+income.getId());
    }
}
