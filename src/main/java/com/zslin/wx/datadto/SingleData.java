package com.zslin.wx.datadto;

/**
 * Created by 钟述林 393156105@qq.com on 2017/4/21 15:44.
 */
public class SingleData {

    private String name;

    private Double value;

    public SingleData(String name, Double value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
