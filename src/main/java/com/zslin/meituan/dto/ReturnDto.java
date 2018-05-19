package com.zslin.meituan.dto;

/**
 * Created by 钟述林 393156105@qq.com on 2017/7/5 16:49.
 * -
 */
public class ReturnDto {

    private Integer code;

    private String msg;

    private Object data;

    public ReturnDto(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public ReturnDto() {}

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
