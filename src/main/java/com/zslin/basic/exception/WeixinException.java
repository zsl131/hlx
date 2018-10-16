package com.zslin.basic.exception;

/**
 * 系统异常
 * @author zslin.com 20160514
 *
 */
public class WeixinException extends RuntimeException {

    private static final long serialVersionUID = -4555331337009026323L;

    private String code;

    public WeixinException() {
        super();
    }

    public WeixinException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

    public WeixinException(String msg) {
        super(msg);
    }

    public WeixinException(String code, String msg) {
        super(msg);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public WeixinException(Throwable throwable) {
        super(throwable);
    }
}
