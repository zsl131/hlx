package com.zslin.basic.tools;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 * Created by zsl-pc on 2016/9/21.
 */
public class DateTools {

    public static Date plusDay(Integer days) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, days);
        return cal.getTime();
    }

    public static Date plusMonth(Integer month) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, month);
        return cal.getTime();
    }

    /**
     *
     * @param days
     * @param pattern
     * @return
     */
    public static String plusDay(Integer days, String pattern) {
        Date date = plusDay(days);
        if(pattern==null || "".equalsIgnoreCase(pattern)) {pattern = "yyyy-MM-dd";}
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    public static String plusMonth(Integer month, String pattern) {
        Date date = plusMonth(month);
        if(pattern==null || "".equalsIgnoreCase(pattern)) {pattern = "yyyy-MM";}
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    public static Long plusDayByLong(Integer days) {
        Date date = plusDay(days);
        return date.getTime();
    }

    /**
     * 判断是否逾期
     * @param startDate 开始日期
     * @param amount 出租天数
     * @return
     */
    public static boolean isOverdue(Date startDate, Integer amount) {
        Calendar needCal = Calendar.getInstance();
        needCal.setTime(startDate);
        needCal.add(Calendar.DAY_OF_MONTH, amount);

        Calendar nowCal = Calendar.getInstance();

        return !needCal.getTime().after(nowCal.getTime());
    }

    public static String date2Str(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    public static String date2Str(Date date) {
        return date2Str(date, "yyyy-MM-dd");
    }

    public static String formatDate(Date date) {
        return date2Str(date, "yyyy-MM-dd HH:mm:ss") ;
    }

    /**
     * 计算一个月有多少天
     * @param month 格式如：yyyyMM
     * @return
     */
    public static int queryDayOfMoney(String month) {
        int year = Integer.parseInt(month.substring(0, 4));
        int m = Integer.parseInt(month.substring(4, 6));
        LocalDate localDate = LocalDate.of(year, m, 1);
        //System.out.println(localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        return localDate.lengthOfMonth();
    }

    /**
     * 计算当月已过的天数
     * @param month 月份，格式如：yyyyMM
     * @return
     */
    public static int queryCurrentMonthLength(String month) {
        LocalDate now = LocalDate.now();
        String curMonth = now.format(DateTimeFormatter.ofPattern("yyyyMM"));
        if(month.equals(curMonth)) { //如果当前月
            return now.getDayOfMonth();
        } else {
            return queryDayOfMoney(month);
        }
    }
}
