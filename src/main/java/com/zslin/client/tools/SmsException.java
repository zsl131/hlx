package com.zslin.client.tools;

/**
 * 系统异常
 * @author zslin.com 20160514
 *
 */
public class SmsException extends RuntimeException {

    private static final long serialVersionUID = -4555331337009026323L;

    private String err;

    public String getErr() {
        return err;
    }

    public void setErr(String err) {
        this.err = err;
    }

    public SmsException() {
        super();
    }

    public SmsException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

    public SmsException(String msg) {
        super(msg);
    }

    public SmsException(String msg, String err) {
        super(msg);
        this.err = err;
    }

    public SmsException(Throwable throwable) {
        super(throwable);
    }
}
