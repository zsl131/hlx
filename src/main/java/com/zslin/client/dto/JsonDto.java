package com.zslin.client.dto;

import com.zslin.web.model.Price;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/11 22:27.
 */
public class JsonDto {

    //类型，如：config、work……
    private String type;

    //操作，如：update、delete
    private String action;

    //具体的操作对象
    private Object data;

    private Integer dataId;

    public JsonDto(String type, String action, Integer dataId, Object data) {
        this.type = type;
        this.action = action;
        this.dataId = dataId;
        this.data = data;
    }

    public void setDataId(Integer dataId) {
        this.dataId = dataId;
    }

    public Integer getDataId() {
        return dataId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
