package com.zslin.wx.datadto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 钟述林 393156105@qq.com on 2017/4/21 15:40.
 * 用于图形显示的
 */
public class BarDto {

    private String title;

    private String subTitle;

    private List<String> cateList;

    private List<String> legend;

    private List<SingleBarData> valList;

    public BarDto(String title, String subTitle) {
        this.title = title;
        this.subTitle = subTitle;
        if(cateList==null) {this.cateList = new ArrayList<>();}
        if(valList==null) {this.valList = new ArrayList<>();}
        if(legend==null) {this.legend = new ArrayList<>();}
    }

    public BarDto addValues(String name, String... values) {
        this.valList.add(new SingleBarData(name, values));
        return this;
    }

    public BarDto addCate(String... cates) {
        for(String s : cates) {
            this.cateList.add(s);
        }
        return this;
    }

    public BarDto addLegend(String... values) {
        for(String s : values) {
            this.legend.add(s);
        }
        return this;
    }

    public List<String> getCateList() {
        return cateList;
    }

    public void setCateList(List<String> cateList) {
        this.cateList = cateList;
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

    public List<String> getLegend() {
        return legend;
    }

    public void setLegend(List<String> legend) {
        this.legend = legend;
    }

    public List<SingleBarData> getValList() {
        return valList;
    }

    public void setValList(List<SingleBarData> valList) {
        this.valList = valList;
    }
}
