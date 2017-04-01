package com.zslin.client.dto;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/6 11:54.
 * 客户端请求数据返回DTO对象
 */
public class ResDto {
    public static final String SUC = "0";
    public static final String ERR = "1";
    private String err;
    private String res;
    private String msg="";

    public ResDto(String err, String res, String msg) {
        this.res = res; this.err = err; this.msg = msg;
    }
    public ResDto(String err, String res) {
        this.res = res; this.err = err;
    }

    public String getErr() {
        return err;
    }

    public void setErr(String err) {
        this.err = err;
    }

    public String getRes() {
        return res;
    }

    public void setRes(String res) {
        this.res = res;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
