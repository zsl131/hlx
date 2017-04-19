package com.zslin.kaoqin.tools;

import com.zslin.kaoqin.model.Clockin;
import com.zslin.kaoqin.model.Workday;
import com.zslin.kaoqin.model.Worker;
import com.zslin.kaoqin.service.IClockinService;
import com.zslin.kaoqin.service.IWorkdayService;
import com.zslin.kaoqin.service.IWorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by 钟述林 393156105@qq.com on 2017/4/17 23:23.
 * 考勤工具类
 */
@Component
public class ClockinTools {

    private static final Integer RANGE_MIN = 25; //在多少分钟范围内属于此时段

    @Autowired
    private IWorkdayService workdayService;

    @Autowired
    private IWorkerService workerService;

    @Autowired
    private IClockinService clockinService;

    public void clockin(Integer workerId, String clockTime, Integer verify) {
        List<String> resList = calClock(workerId, clockTime);
        if(resList!=null && resList.size()>=2) {
            Worker w = workerService.findOne(workerId);
            Clockin c = new Clockin();
            c.setWorkerName(w.getName());
            c.setDepId(w.getDepId());
            c.setWorkerId(w.getId());
            c.setTime(clockTime);
            c.setStep(resList.get(0));
            c.setVerify(verify);
            c.setFlag(Integer.parseInt(resList.get(1)));
            c.setCurDay(clockTime.split(" ")[0]);
            c.setWeekday(getWeekday(clockTime));

            clockinService.save(c);
        }
    }

    private String getWeekday(String clockTime) {
        try {
            String[] weekDays = {"日", "一", "二", "三", "四", "五", "六"};
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            cal.setTime(sdf.parse(clockTime));
            int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
            if (w < 0)
                w = 0;
            return weekDays[w];
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 计算打卡阶段
     * 1-上午上班
     * 2-上午下班
     * 3-下午上班
     * 4-下午下班
     * 5-晚上上班
     * 6-晚上下班
     * @param workerId 员工Id
     * @param clockTime 打卡时间
     * @return
     */
    private List<String> calClock(Integer workerId, String clockTime) {
        List<String> list = new ArrayList<>();
        Workday w = workdayService.findByWorkerId(workerId);
        String step = calStep(w, clockTime);
        if(step==null || "".equals(step)) {return list;}
        String time = "";
        if("1".equals(step)) {time = w.getSbsj1();}
        else if("2".equals(step)) {time = w.getXbsj1();}
        else if("3".equals(step)) {time = w.getSbsj2();}
        else if("4".equals(step)) {time = w.getXbsj2();}
        else if("5".equals(step)) {time = w.getSbsj3();}
        else if("6".equals(step)) {time = w.getXbsj3();}
        Boolean b = isOk(step, time, clockTime);
        list.add(step);
        list.add(b?"1":"0");
        return list;
    }

    private String calStep(Workday w, String clockTime) {
        String step = null;
        if(w!=null) {
            if(isThisStep(w.getSbsj1(), clockTime)) {step = "1";}
            else if(isThisStep(w.getXbsj1(), clockTime)) {step = "2";}
            else if(isThisStep(w.getSbsj2(), clockTime)) {step = "3";}
            else if(isThisStep(w.getXbsj2(), clockTime)) {step = "4";}
            else if(isThisStep(w.getSbsj3(), clockTime)) {step = "5";}
            else if(isThisStep(w.getXbsj3(), clockTime)) {step = "6";}
        }
        return step;
    }

    private boolean isOk(String step, String time, String clockTime) {
        try {
            String timeStr = clockTime.split(" ")[0]+" "+time;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse(timeStr));

            Date clockDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(clockTime);
            Calendar clockCal = Calendar.getInstance();
            clockCal.setTime(clockDate);
            if("1".equals(step) || "3".equals(step) || "5".equals(step)) {
                return !clockCal.after(cal);
            } else {
                return !clockCal.before(cal);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean isThisStep(String time, String clockTime) {
        try {
            String timeStr = clockTime.split(" ")[0]+" "+time;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date stepDate = sdf.parse(timeStr);
            Date clockDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(clockTime);
            Calendar startCal = Calendar.getInstance();
            startCal.setTime(stepDate);
            startCal.add(Calendar.MINUTE, 0-RANGE_MIN);

            Calendar endCal = Calendar.getInstance();
            endCal.setTime(stepDate);
            endCal.add(Calendar.MINUTE, RANGE_MIN);

            Calendar clockCal = Calendar.getInstance();
            clockCal.setTime(clockDate);

            boolean res = (clockCal.after(startCal)) && (clockCal.before(endCal));
//            System.out.println(clockTime+"======"+time+"===="+res);
            return res;
        } catch (Exception e) {
            //e.printStackTrace();
            return false;
        }
    }
}
