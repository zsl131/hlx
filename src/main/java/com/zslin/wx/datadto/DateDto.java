package com.zslin.wx.datadto;

/**
 * Created by 钟述林 393156105@qq.com on 2017/4/21 15:50.
 */
public class DateDto {

    /** 开始日期，yyyy-MM-dd */
    private String startDay;

    /** 结束日期，yyyy-MM-dd */
    private String endDay;

    public DateDto() {}

    @Override
    public String toString() {
        return startDay+" - "+endDay;
    }

    public DateDto(String startDay, String endDay) {
        this.startDay = startDay;
        this.endDay = endDay;
    }

    public String getStartDay() {
        return startDay;
    }

    public void setStartDay(String startDay) {
        this.startDay = startDay;
    }

    public String getEndDay() {
        return endDay;
    }

    public void setEndDay(String endDay) {
        this.endDay = endDay;
    }
}
