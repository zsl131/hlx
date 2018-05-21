package com.zslin.wx.tools;

import com.zslin.basic.tools.DateTools;
import com.zslin.web.model.BuffetOrder;
import com.zslin.web.service.IBuffetOrderService;
import com.zslin.web.service.IIncomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by zsl on 2018/5/14.
 */
@Component
public class HlxTools {

    @Autowired
    private IBuffetOrderService buffetOrderService;

    @Autowired
    private IIncomeService incomeService;

    /**
     * 定时器自动查询的营业信息
     * @return
     */
    public String queryFinanceByTimer() {
        String spe = "\\n";
        String lastMonth = getLastMonth(-1); //上一个月
        String lastMonth2 = getLastMonth(-2); //上两个月

        Integer days1 = getDaysOfMonth(lastMonth);
        Double sum1 = buffetOrderService.sumByMonth(formatMonthStr(lastMonth));
        sum1 = sum1==null?0:sum1;

        Integer days2 = getDaysOfMonth(lastMonth2);
        Double sum2 = buffetOrderService.sumByMonth(formatMonthStr(lastMonth2));
        sum2 = sum2==null?0:sum2;

        String res = queryFinance(lastMonth, spe);
        StringBuffer sb = new StringBuffer(res);
        sb.append(spe).append("======与").append(lastMonth2).append("相比======").append(spe)
            .append("上月消费：").append(formatValue(sum2, 0)).append(" 人次，").append((sum1>sum2)?"上升":"下降").append(cal(sum1, sum2)).append(spe)
            .append("上月平均：").append(formatValue(sum2/days2, 2)).append(" 人次/天，").append((sum1>sum2)?"上升":"下降").append(cal(sum1/days1, sum2/days2)).append(spe);
        return sb.toString();
    }

    public String calDay() {
        return calDay(0);
    }

    /**
     * 修改成这个方法主要为了方便测试
     * @param days days天前的数据
     * @return
     */
    public String calDay(int days) {
        String spe = "\\n";
        String lastDay = getLastDay(0-days);
        Integer sum = buffetOrderService.sumByDay(lastDay); //消费总数
        sum = sum==null?0:sum;
        List<BuffetOrder> list = buffetOrderService.findMeiTuanByDay(lastDay);
//        Integer sumMt = buffetOrderService.sumByDay(lastDay, "3"); //美团人数
        Integer sumMt = buildMeituanAmount(list); //美团人数
        sumMt = sumMt==null?0:sumMt;

        String lastDay2 = getLastDay(0-days-1);
        Integer sum2 = buffetOrderService.sumByDay(lastDay2);
        sum2 = sum2==null?0:sum2;

        Double ticketDiscountMoney = buffetOrderService.sumDiscountMoney("3", lastDay); //卡券抵扣
        ticketDiscountMoney = ticketDiscountMoney==null?0:ticketDiscountMoney;
        Double scoreDiscountMoney = buffetOrderService.sumDiscountMoney("1", lastDay); //积分抵扣
        scoreDiscountMoney = scoreDiscountMoney==null?0:scoreDiscountMoney;

        StringBuffer sb = new StringBuffer();
        sb.append("查询日期：").append(lastDay).append(spe)
                .append("消费人次：").append(sum).append(" 人次").append(spe)
                .append("美团人数：").append(sumMt).append(" 人次").append(sum>0?("，占比："+formatValue(sumMt*1.0/sum*100, 2)+"%"):"").append(spe)
                .append("卡券抵扣：").append(ticketDiscountMoney).append(" 元").append(spe)
                .append("积分抵扣：").append(scoreDiscountMoney).append(" 元").append(spe)
                .append("======与").append(lastDay2).append("相比======").append(spe)
                .append("昨天消费：").append(sum2).append(" 人次，").append((sum>sum2)?"上升":"下降").append(cal(sum*1.0, sum2*1.0)).append(spe);
        return sb.toString();
    }

    private Integer buildMeituanAmount(List<BuffetOrder> list) {
        Integer res = 0;
        if(list==null) {return res;}
        for(BuffetOrder order : list) {
            String datas = order.getDiscountReason();
            String [] array = datas.split(",");
            for(String d : array) {
                if(d!=null && d.length()==12) {
                    res ++;
                }
            }
        }
        return res;
    }

    /**
     * 计算变化比率
     * @param a 上一个月数据
     * @param b 上两个月数据
     * @return
     */
    private String cal(Double a, Double b) {
        Double res = Math.abs(a - b);
        if(b<=0) {
            return "100%";
        }
        return formatValue(res/b*100, 2)+"%";
    }

    /**
     * 查询营业信息
     * @param month 月份，如：201805
     * @param spe 分隔
     * @return
     */
    public String queryFinance(String month, String spe) {
        if(month!=null && month.length()==6) {
            StringBuffer sb = new StringBuffer();
            Integer days = getDaysOfMonth(month);
            Double sum = buffetOrderService.sumByMonth(formatMonthStr(month));
            sum = sum==null?0:sum;
            Double sumMoney = incomeService.totalMoney(month);
            sumMoney = sumMoney==null?0:sumMoney;

            Double ticketDiscountMoney = buffetOrderService.sumDiscountMoney("3", formatMonthStr(month)); //卡券抵扣
            ticketDiscountMoney = ticketDiscountMoney==null?0:ticketDiscountMoney;
            Double scoreDiscountMoney = buffetOrderService.sumDiscountMoney("1", formatMonthStr(month)); //积分抵扣
            scoreDiscountMoney = scoreDiscountMoney==null?0:scoreDiscountMoney;

            sb.append("查询月份：").append(month).append(spe);
            sb.append("当月天数：").append(days).append(" 天").append(spe);
            sb.append("消费人次：").append(formatValue(sum, 0)).append(" 人").append(spe);
            sb.append("平均每天：").append(formatValue(sum/days, 2)).append(" 人").append(spe);
            sb.append("当月营收：").append(formatValue(sumMoney, 2)).append(" 元").append(spe)
                    .append("平均每日：").append(formatValue(incomeService.average(month), 2)).append(" 元").append(spe)
                    .append("超过两万：").append(incomeService.moreThan(month, 20000d)).append(" 天").append(spe)
                    .append("卡券抵扣：").append(ticketDiscountMoney).append(" 元").append(spe)
                    .append("积分抵扣：").append(scoreDiscountMoney).append(" 元");
            return sb.toString();
        }
        return "查询失败，数据格式出错【yyyyMM】";
    }

    public String queryFinance(String content) {
        return queryFinance(content, "\n");
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

    private String getLastMonth(int amount) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        cal.add(Calendar.MONTH, amount);
        return sdf.format(cal.getTime());
    }

    private String getLastDay(int amount) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        cal.add(Calendar.DAY_OF_MONTH, amount);
        return sdf.format(cal.getTime());
    }

}
