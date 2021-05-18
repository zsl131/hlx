package com.zslin.wx.dto;

import lombok.Data;

/**
 * 上传结果DTO
 */
@Data
public class UploadResult {

    private String code;

    private String msg;

    public UploadResult() {
    }

    public UploadResult(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
