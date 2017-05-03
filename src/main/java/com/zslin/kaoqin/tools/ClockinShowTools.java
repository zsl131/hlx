package com.zslin.kaoqin.tools;

import com.zslin.kaoqin.dto.DayDto;
import com.zslin.kaoqin.dto.MonthDto;
import com.zslin.kaoqin.model.Clockin;
import com.zslin.kaoqin.service.IClockinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * Created by 钟述林 393156105@qq.com on 2017/4/20 10:35.
 * 显示考勤情况工具类
 */
@Component
public class ClockinShowTools {

    @Autowired
    private IClockinService clockinService;

    private static final Integer COUNT = 4; //每天打卡次数

    public MonthDto buildWorkerClockin(Integer year, Integer month, Integer workerId) {
        List<Clockin> datas = clockinService.findByWorker(year, month, workerId);
        if(datas==null || datas.size()<=0) {return null;}
        Clockin c = datas.get(0);
        Integer day = c.getDay();
        List<DayDto> res = initBefore(year, month, day);
        addDatas(res, datas);
        MonthDto dto = new MonthDto();
        dto.setMonth(c.getMonth());
        dto.setYear(c.getYear());
        dto.setWorkerId(c.getWorkerId());
        dto.setWorkerName(c.getWorkerName());
        dto.setUnmarkCount(calUnmarkCount(res));
        dto.setLateCount(calLateCount(res));
        dto.setList(res);
        return dto;
    }

    private Integer calLateCount(List<DayDto> datas) {
        Integer res = 0;
        for(DayDto dto : datas) {
            if(dto!=null) {
                Map<String, String> record = dto.getRes();
                for(String key : record.keySet()) {
                    if("0".equalsIgnoreCase(record.get(key))) {res ++;}
                }
            }
        }
        return res;
    }

    //计算未打卡次数
    private Integer calUnmarkCount(List<DayDto> datas) {
        Integer res = 0;
        for(DayDto dto : datas) {
            if(dto!=null) {
                Map<String, String> record = dto.getRes();
                Integer unmarkCount = COUNT-record.size();
                res += unmarkCount<=0?0:unmarkCount;
                for(String key : record.keySet()) {
                    if("-1".equalsIgnoreCase(record.get(key))) {res ++;}
                }
            }
        }
        return res;
    }

    private void addDatas(List<DayDto> res, List<Clockin> datas) {
        int day = 0;
        DayDto dayDto = null;
        for(Clockin c : datas) {
            if(day>0 && c.getDay()!=day) {
                res.add(checkDayDto(dayDto));
            }
            if(day==0 || c.getDay()!=day) {
                dayDto = new DayDto(c.getDay());
            }
            dayDto.getRes().put(c.getStep(), c.getFlag()+"");

            day = c.getDay();
        }
        res.add(checkDayDto(dayDto));
    }

    private DayDto checkDayDto(DayDto dto) {
        Map<String, String> res = dto.getRes();
        for(int i=1; i<=4;i++) {
            if(!res.containsKey(i+"")) {res.put(i+"", "-1");}
        }
        dto.setRes(res);
        return dto;
    }

    //在最开始添加空对象，优化显示效果
    private List<DayDto> initBefore(Integer year, Integer month, Integer day) {
        List<DayDto> res = new ArrayList<>();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse(year+"-"+month+"-01"));
            int amount = cal.get(Calendar.DAY_OF_WEEK)-1;
            for(int i=0;i<amount;i++) {
                res.add(null);
            }
            for(int i=0;i<day-1;i++) {
                res.add(new DayDto(i+1));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return res;
    }
}
