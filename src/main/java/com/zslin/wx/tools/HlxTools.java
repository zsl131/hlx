package com.zslin.wx.tools;

import com.zslin.web.service.IBuffetOrderService;
import com.zslin.web.service.IIncomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by zsl on 2018/5/14.
 */
@Component
public class HlxTools {

    @Autowired
    private IBuffetOrderService buffetOrderService;

    @Autowired
    private IIncomeService incomeService;

    public String queryFinance(String content) {
        if(content!=null && content.length()==6) {
            StringBuffer sb = new StringBuffer();
            Integer days = getDaysOfMonth(content);
            Double sum = buffetOrderService.sumByMonth(formatMonthStr(content));
            sum = sum==null?0:sum;
            Double sumMoney = incomeService.totalMoney(content);
            sumMoney = sumMoney==null?0:sumMoney;
            String spe = "\n";
            sb.append("查询月份：").append(content).append(spe);
            sb.append("当月天数：").append(days).append(" 天").append(spe);
            sb.append("消费人次：").append(sum).append(" 人次").append(spe);
            sb.append("平均每天：").append(formatValue(sum/days, 2)).append(" 人次").append(spe);
            sb.append("当月营收：").append(formatValue(sumMoney, 2)).append(" 元").append(spe)
                    .append("平均每日：").append(formatValue(incomeService.average(content), 2)).append(" 元").append(spe)
                    .append("超过两万：").append(incomeService.moreThan(content, 20000d)).append(" 天");
            return sb.toString();
        }
        return "查询失败，数据格式出错【yyyyMM】";
    }

    /**
     * 获取某个月有几天
     * @param monthStr 月份字符串，格式如：201805
     * @return
     */
    public int getDaysOfMonth(String monthStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Calendar cal = Calendar.getInstance();
            String curMonth = sdf.format(cal.getTime()).substring(0, 6);
            Integer day = cal.get(Calendar.DAY_OF_MONTH);
            Integer hour = cal.get(Calendar.HOUR_OF_DAY); //时
            cal.setTime(sdf.parse(monthStr+"01"));
            String theMonth = sdf.format(cal.getTime()).substring(0, 6);
            if(curMonth.equals(theMonth)) {
                System.out.printf("current day : "+ day);
                if(hour<21) { //晚上9点之前
                    return day - 1;
                } else {
                    return day;
                }
            } else {
                return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
            }
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * 格式化月份，格式化为可以让数据库识别的
     * @param monthStr 月份字符串，格式如：201805
     * @return
     */
    public static String formatMonthStr(String monthStr) {
        if(monthStr!=null && monthStr.length()==6) {
            return monthStr.substring(0, 4) + "-" + monthStr.substring(4, 6)+"-%";
        }
        return "";
    }

    /**
     * 保留几位有效数
     * @param value 数值
     * @param len 保留几位,默认2位
     * @return
     */
    public String formatValue(Double value, Integer len) {
        if(value==null) {return "-";}
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(len==null?2:len, RoundingMode.HALF_UP);
        return bd.toString();
    }
}
