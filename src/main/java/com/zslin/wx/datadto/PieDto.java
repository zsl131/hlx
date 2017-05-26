package com.zslin.wx.datadto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 钟述林 393156105@qq.com on 2017/4/21 15:40.
 * 用于图形显示的
 */
public class PieDto {

    private String title;

    private String subTitle;

    //formatter: '{b} : {c} ({d}%)' //广告联盟 : 123(55%)
    private String formatter;

    private List<SingleData> data;

    public PieDto(String title, String subTitle, String formatter, SingleData... datas) {
        this.title = title;
        this.subTitle = subTitle;
        this.formatter = formatter;
        if(data==null) {this.data = new ArrayList<>();}
        for(SingleData d : datas) {
            data.add(d);
        }
    }

    public PieDto() {
        data = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getFormatter() {
        return formatter;
    }

    public void setFormatter(String formatter) {
        this.formatter = formatter;
    }

    public List<SingleData> getData() {
        return data;
    }

    public void setData(List<SingleData> data) {
        this.data = data;
    }
}
