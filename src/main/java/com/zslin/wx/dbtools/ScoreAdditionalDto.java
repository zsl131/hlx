package com.zslin.wx.dbtools;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/2 9:03.
 * 添加积分推送信息的DTO对象，其实就是一个键值对
 */
public class ScoreAdditionalDto {

    private String name;

    private String value;

    public ScoreAdditionalDto(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
