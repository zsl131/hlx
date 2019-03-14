package com.zslin.web.tools;

import com.zslin.client.tools.ClientFileTools;
import com.zslin.client.tools.ClientJsonTools;
import com.zslin.web.model.DiscountDay;
import com.zslin.web.service.IDiscountDayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;

/**
 * Created by zsl on 2018/1/13.
 */
@Component
public class DiscountDayTools {

    @Autowired
    private IDiscountDayService discountDayService;

    @Autowired
    private ClientFileTools clientFileTools;

    /**
     * 设置折扣日，格式 yyyyMMdd_x，x表示是否为折扣日，1-折扣日；0-非折扣日
     * @param data
     */
    public String setDiscountDay(String data) {
        if(data!=null && data.length()==10 && data.indexOf("-")==8) {
            try {
                String [] array = data.split("-");
                setDiscountDay(array[0], array[1]);
                return "设置成功！已将【"+array[0]+"】设置为："+(array[1].equals("0")?"非折扣日":(array[1].equals("1")?"折扣日":"自动"));
            } catch (Exception e) {
//                e.printStackTrace();
                return "设置失败，"+e.getMessage();
            }
        }
        return "设置失败，数据格式出错";
    }

    /**
     * 设置折扣日
     * @param day 日期 yyyyMMdd
     * @param isWorkday 是否为折扣日，1-是；0-否
     */
    public void setDiscountDay(String day, String isWorkday) {
        if(day!=null && day.length()==8) {
            DiscountDay dd = discountDayService.findByYearMonth(getYearMonth(day));
            String days;
            if (dd == null) {
                days = buildDays(day);
                dd = new DiscountDay();
                dd.setYearMonth(day.substring(0, 6));
                dd.setYear(Integer.parseInt(day.substring(0, 4)));
                dd.setMonth(Integer.parseInt(day.substring(4, 6)));
            } else {
                days = dd.getDays();
            }
            if("0".equalsIgnoreCase(isWorkday) || "1".equalsIgnoreCase(isWorkday)) {
                days = modifyDays(days, day, isWorkday);
            } else {
                days = buildDays(day);
            }
            dd.setDays(days);
            discountDayService.save(dd);
            send2Client(dd);
        }
    }

    private String modifyDays(String days, String day, String isWorkday) {
        StringBuffer sb = new StringBuffer();
        String [] days_array = days.split(",");
        for(String singleDay : days_array) {
            if(singleDay!=null && singleDay.indexOf("_")>=0) {
                if(Integer.parseInt(day.substring(6,8)) == Integer.parseInt(singleDay.split("_")[0])) {
                    sb.append(Integer.parseInt(day.substring(6,8))).append("_").append(isWorkday).append(",");
                } else {
                    sb.append(singleDay).append(",");
                }
            }
        }
        String res = sb.toString();
        return res.endsWith(",")?res.substring(0, res.length()-1):res;
    }

    private String buildDays(String day) {
        StringBuffer sb = new StringBuffer();
        if(day!=null && day.length()==8) {
            Integer year = Integer.parseInt(day.substring(0, 4)); //年
            Integer month = Integer.parseInt(day.substring(4, 6)); //月
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month-1);
            for(int i=1; i<=cal.getActualMaximum(Calendar.DATE); i++) {
                cal.set(Calendar.DAY_OF_MONTH, i);
                int week = cal.get(Calendar.DAY_OF_WEEK);
                sb.append(i).append("_").append((week==1 || week==7 || week==6)?"0":"1").append(","); //1-星期天；6-星期五；7-星期六
            }
        }
        String res = sb.toString();
        return res.endsWith(",")?res.substring(0, res.length()-1):res;
    }

    private String getYearMonth(String day) {
        if(day!=null && day.length()==8) {
            return day.substring(0, 6);
        }
        return "";
    }

    private void send2Client(DiscountDay dd) {
        String content = ClientJsonTools.buildDataJson(ClientJsonTools.buildDiscountDay(dd));
        clientFileTools.setChangeContext(content, true);
    }
}
