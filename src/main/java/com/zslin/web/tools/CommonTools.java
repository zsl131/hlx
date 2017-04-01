package com.zslin.web.tools;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/9 16:40.
 * 常用工具类
 */
public class CommonTools {

    /**
     * 比较两个日期相差的天数
     * @param fDate 开始
     * @param oDate 结束
     * @return
     */
    public static int daysOfTwo(Date fDate, Date oDate) {
        Calendar aCalendar = Calendar.getInstance();
        aCalendar.setTime(fDate);
        int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);
        aCalendar.setTime(oDate);
        int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);
        return day2 - day1;
    }

    public static int daysOfTwo(String startDay, String endDay, String pattern) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            Date date1 = sdf.parse(startDay);
            Date date2 = sdf.parse(endDay);
            return daysOfTwo(date1, date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int daysOfTwo(String startDay, String endDay) {
        return daysOfTwo(startDay, endDay, "yyyy-MM-dd");
    }

    public static Float keep2Point(Double d) {
        BigDecimal b = new BigDecimal(d);
        return b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
    }
}
