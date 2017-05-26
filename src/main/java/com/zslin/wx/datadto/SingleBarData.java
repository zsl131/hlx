package com.zslin.wx.datadto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 钟述林 393156105@qq.com on 2017/4/28 10:50.
 */
public class SingleBarData {

    private String name;

    private List<String> values;

    public SingleBarData(String name, String... values) {
        this.name = name;
        if(this.values==null) {this.values = new ArrayList<>();}
        for(String v : values) {
            this.values.add(v);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }
}
