package com.zslin.admin.dto;

/**
 * Created by 钟述林 393156105@qq.com on 2017/6/12 9:49.
 */
public class MyTimeDto {

    private String day;

    private String startTimeAM;

    private String endTimeAM;

    private String startTimePM;

    private String endTimePM;

    public MyTimeDto(String day) {
        this.day = day;
        this.startTimeAM = day + " 08:00";
        this.endTimeAM = day + " 15:30";

        this.startTimePM = day + " 15:31";
        this.endTimePM = day + " 23:00";
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getStartTimeAM() {
        return startTimeAM;
    }

    public void setStartTimeAM(String startTimeAM) {
        this.startTimeAM = startTimeAM;
    }

    public String getEndTimeAM() {
        return endTimeAM;
    }

    public void setEndTimeAM(String endTimeAM) {
        this.endTimeAM = endTimeAM;
    }

    public String getStartTimePM() {
        return startTimePM;
    }

    public void setStartTimePM(String startTimePM) {
        this.startTimePM = startTimePM;
    }

    public String getEndTimePM() {
        return endTimePM;
    }

    public void setEndTimePM(String endTimePM) {
        this.endTimePM = endTimePM;
    }
}
