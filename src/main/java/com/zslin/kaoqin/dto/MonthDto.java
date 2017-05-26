package com.zslin.kaoqin.dto;

import java.util.List;

/**
 * Created by 钟述林 393156105@qq.com on 2017/4/20 10:23.
 */
public class MonthDto {

    private Integer year;

    private Integer month;

    private Integer workerId;

    private String workerName;

    private List<DayDto> list;

    /** 迟到早退次数 */
    private Integer lateCount;

    /** 未打卡次数 */
    private Integer unmarkCount;

    @Override
    public String toString() {
        return "MonthDto{" +
                "year=" + year +
                ", month=" + month +
                ", workerId=" + workerId +
                ", workerName='" + workerName + '\'' +
                ", list=" + list +
                ", lateCount=" + lateCount +
                ", unmarkCount=" + unmarkCount +
                '}';
    }

    public Integer getLateCount() {
        return lateCount;
    }

    public void setLateCount(Integer lateCount) {
        this.lateCount = lateCount;
    }

    public Integer getUnmarkCount() {
        return unmarkCount;
    }

    public void setUnmarkCount(Integer unmarkCount) {
        this.unmarkCount = unmarkCount;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getWorkerId() {
        return workerId;
    }

    public void setWorkerId(Integer workerId) {
        this.workerId = workerId;
    }

    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    public List<DayDto> getList() {
        return list;
    }

    public void setList(List<DayDto> list) {
        this.list = list;
    }
}
