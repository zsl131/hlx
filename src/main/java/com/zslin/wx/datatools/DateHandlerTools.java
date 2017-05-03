package com.zslin.wx.datatools;

import com.zslin.basic.tools.DateTools;
import com.zslin.basic.tools.NormalTools;
import com.zslin.wx.datadto.DateDto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by 钟述林 393156105@qq.com on 2017/4/21 15:52.
 * 处理获取日期的对象
 */
public class DateHandlerTools {

    /**
     * 生成开始日期和结束日期
     * @param amount 1-今天，2-昨天，3-过去7天内，4-本月，5-上个月
     * @return
     */
    public static DateDto buildDate(String amount) {
        if("1".equalsIgnoreCase(amount)) {
            String curDate = NormalTools.curDate("yyyy-MM-dd");
            return new DateDto(curDate, curDate);
        } else if("2".equalsIgnoreCase(amount)) {
            String yestoday = DateTools.plusDay(-1, "yyyy-MM-dd");
            return new DateDto(yestoday, yestoday);
        } else if("3".equalsIgnoreCase(amount)) {
            String yestoday = DateTools.plusDay(-1, "yyyy-MM-dd");
            String before = DateTools.plusDay(-7, "yyyy-MM-dd");
            return new DateDto(before, yestoday);
        } else if("4".equalsIgnoreCase(amount)) {
            return new DateDto(getFirstDay(), DateTools.plusDay(-1, "yyyy-MM-dd"));
        } else if("5".equalsIgnoreCase(amount)) {
            return new DateDto(getBeforeFirstDay(), getBeforeEndDay());
        }

        return getDefault();
    }

    //默认获取昨天的数据
    private static DateDto getDefault() {
        String yestoday = DateTools.plusDay(-1, "yyyy-MM-dd");
        return new DateDto(yestoday, yestoday);
    }

    private static String getBeforeFirstDay() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        String firstDay = new SimpleDateFormat("yyyy-MM").format(cal.getTime())+"-01";
        return firstDay;
    }

    private static String getBeforeEndDay() {
        try {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date d = sdf.parse(getFirstDay());
            cal.setTime(d);
            cal.add(Calendar.DAY_OF_MONTH, -1);
            return sdf.format(cal.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    private static String getFirstDay() {
        String month = new SimpleDateFormat("yyyy-MM").format(new Date());
        String day = month + "-01";
        return day;
    }
}
