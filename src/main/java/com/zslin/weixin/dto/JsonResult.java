package com.zslin.weixin.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zsl on 2018/7/3.
 */
@JsonInclude(value= JsonInclude.Include.NON_NULL)
@Data
public class JsonResult {

    public static final String SUCCESS_REASON = "请求成功";
    public static final String SUCCESS_CODE = "0";
    public static final String BUSINESS_ERR_CODE = "10001"; //业务错误代码

    private String reason = SUCCESS_REASON;

    /**
     * 错误代码
     * 0-成功返回
     */
    private String errCode = SUCCESS_CODE;

   /* @ApiModelProperty(value = "登陆标记，0-不需要登陆，1-需要登陆")
    private String needLogin = "0";*/

    private Map<String, Object> result;

    /**
     * 标记
     * 1-请求无异常，数据也正常
     * 0-请求无异常，数据不正常
     */
    /*@ApiModelProperty(value = "结果标记，1-请求和结果数据无异常；0-请求无异常，数据结果有异常")
    private String flag = "1";*/

    @Override
    public String toString() {
        return "JsonResult{" +
                "reason='" + reason + '\'' +
                ", errCode='" + errCode + '\'' +
                ", result=" + result +
                '}';
    }

    public static JsonResult getInstance() {
        return new JsonResult();
    }

    public static JsonResult getInstance(String sucMsg) {
        return new JsonResult(sucMsg);
    }

    private JsonResult(String sucMsg) {
        this.reason = SUCCESS_REASON;
        this.errCode = SUCCESS_CODE;
        this.result = new HashMap<>();
        this.result.put("message", sucMsg);
    }

    public static JsonResult error(String errMsg) {
        JsonResult that = getInstance().fail(errMsg);
        return that;
    }

    public static JsonResult error(String errCode, String errMsg) {
        return getInstance().fail(errCode, errMsg);
    }

    public static JsonResult success() {
        return success(SUCCESS_REASON);
    }

    public static JsonResult success(String sucMsg) {
        JsonResult that = getInstance(sucMsg);
        return that;
    }

    public static JsonResult succ(Object obj) {
        JsonResult that = getInstance().set("obj", obj);
        return that;
    }

    public JsonResult failFlag(String msg) {
        this.reason = SUCCESS_REASON;
        this.errCode = SUCCESS_CODE;
        if(this.result==null) {
            this.result = new HashMap<>();
        }
        this.result.put("message", msg);
//        this.flag = "0";
        this.result.put("flag", "0");
        return this;
    }

    public JsonResult failFlag(String errCode, String msg, Object obj) {
        this.reason = msg;
        this.errCode = errCode;
        if(this.result==null) {this.result = new HashMap<>();}
        this.result.put("message", msg);
        this.result.put("errors", obj);
        return this;
    }

    public JsonResult failLogin(String msg) {
        this.reason = SUCCESS_REASON;
        this.errCode = SUCCESS_CODE;
        if(this.result==null) {
            this.result = new HashMap<>();
        }
        this.result.put("message", msg);
//        this.needLogin = "1";
        this.result.put("needLogin", "1");
        return this;
    }

    public JsonResult ok(String result) {
        this.reason = SUCCESS_REASON;
        this.errCode = SUCCESS_CODE;
        this.result.put("message", result);
        return this;
    }

    public JsonResult fail(String errCode, String errMsg) {
        this.reason = errMsg;
        this.errCode = errCode;
        this.result.put("message", errMsg);
        return this;
    }

    public JsonResult fail(String errMsg) {
        return fail(BUSINESS_ERR_CODE, errMsg);
    }

    public JsonResult set(String key, Object data) {
        result.put(key, data);
        return this;
    }

    private JsonResult(){
        result = new HashMap<>();
    }

}
