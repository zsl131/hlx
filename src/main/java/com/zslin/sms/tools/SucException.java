package com.zslin.sms.tools;

/**
 * 系统异常
 * @author zslin.com 20160514
 *
 */
public class SucException extends RuntimeException {

    private static final long serialVersionUID = -4555331337009026323L;

    private String code;

    public SucException() {
        super();
    }

    public SucException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

    public SucException(String msg) {
        super(msg);
    }

    public SucException(String code, String msg) {
        super(msg);
        this.code = code;
    }

    public SucException(Throwable throwable) {
        super(throwable);
    }
}
