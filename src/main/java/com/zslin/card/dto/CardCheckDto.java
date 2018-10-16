package com.zslin.card.dto;

/**
 * Created by zsl on 2018/10/16.
 */
public class CardCheckDto {

    private String name;

    private Long count;

    private String day;

    private String month;

    public CardCheckDto(String name, Long count, String day, String month) {
        this.name = name;
        this.count = count;
        this.day = day;
        this.month = month;
    }

    @Override
    public String toString() {
        return "CardCheckDto{" +
                "name='" + name + '\'' +
                ", count=" + count +
                ", day='" + day + '\'' +
                ", month='" + month + '\'' +
                '}';
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getName() {
        return name;
    }

    public Long getCount() {
        return count;
    }

    public String getDay() {
        return day;
    }

    public String getMonth() {
        return month;
    }
}
