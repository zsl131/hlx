package com.zslin.client.tools;

import com.zslin.client.model.Restday;
import com.zslin.client.service.IRestdayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;

/**
 * Created by zsl on 2018/1/13.
 */
@Component
public class RestdayTools {

    @Autowired
    private IRestdayService restdayService;

    @Autowired
    private ClientFileTools clientFileTools;

    /**
     * 设置工作日，格式 yyyyMMdd_x，x表示是否为工作日，1-工作日；0-休息日
     * @param data
     */
    public String setRestday(String data) {
        if(data!=null && data.length()==10 && data.indexOf("_")==8) {
            try {
                String [] array = data.split("_");
                setRestday(array[0], array[1]);
                return "设置成功！已将【"+array[0]+"】设置为："+(array[1].equals("0")?"休息日":(array[1].equals("1")?"工作日":"自动"));
            } catch (Exception e) {
//                e.printStackTrace();
                return "设置失败，"+e.getMessage();
            }
        }
        return "设置失败，数据格式出错";
    }

    /**
     * 设置工作日
     * @param day 日期 yyyyMMdd
     * @param isWorkday 是否为工作是，1-是；0-否
     */
    public void setRestday(String day, String isWorkday) {
        if(day!=null && day.length()==8) {
            Restday restday = restdayService.findByYearMonth(getYearMonth(day));
            String days;
            if (restday == null) {
                days = buildDays(day);
                restday = new Restday();
                restday.setYearMonth(day.substring(0, 6));
                restday.setYear(Integer.parseInt(day.substring(0, 4)));
                restday.setMonth(Integer.parseInt(day.substring(4, 6)));
            } else {
                days = restday.getDays();
            }
            if("0".equalsIgnoreCase(isWorkday) || "1".equalsIgnoreCase(isWorkday)) {
                days = modifyDays(days, day, isWorkday);
            } else {
                days = buildDays(day);
            }
            restday.setDays(days);
            restdayService.save(restday);
            send2Client(restday);
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
                sb.append(i).append("_").append((week==1 || week==7)?"0":"1").append(",");
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

    private void send2Client(Restday restday) {
        String content = ClientJsonTools.buildDataJson(ClientJsonTools.buildRestday(restday));
        clientFileTools.setChangeContext(content, true);
    }
}
