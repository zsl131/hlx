package com.zslin.web.dto;

/**
 * Created by 钟述林 393156105@qq.com on 2017/7/5 11:26.
 */
public class ReturnDto {

    private String data;

    public ReturnDto(String data) {
        this.data = data;
    }

    public ReturnDto(){}

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
