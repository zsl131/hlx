package com.zslin.kaoqin.dto;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 钟述林 393156105@qq.com on 2017/4/20 10:22.
 * 考勤展示天DTO对象
 */
public class DayDto {

    /** 天 */
    private Integer day;

    /** 考勤结果 ,阶段:结果*/
    private Map<String, String> res;

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(day).append(":");
        for(String key : res.keySet()) {
            sb.append(key).append("-").append(res.get(key)).append(";");
        }
        return sb.toString();
    }

    public DayDto() {
        res = new HashMap<>();
    }

    public DayDto(Integer day) {
        this.day = day;
        res = new HashMap<>();
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Map<String, String> getRes() {
        return res;
    }

    public void setRes(Map<String, String> res) {
        this.res = res;
    }
}
