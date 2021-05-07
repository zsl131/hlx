package com.zslin.basic.qiniu.dto;

/**
 * Created by zsl on 2018/12/1.
 */
public class MyPutRet {
    private String key;
    private String hash;
    private String bucket;
    private long fsize;
    private String mimeType;
    private String ext;
    private String timeLen;

    @Override
    public String toString() {
        return "MyPutRet{" +
                "key='" + key + '\'' +
                ", hash='" + hash + '\'' +
                ", bucket='" + bucket + '\'' +
                ", fsize=" + fsize +
                ", mimeType='" + mimeType + '\'' +
                ", ext='" + ext + '\'' +
                ", timeLen='" + timeLen + '\'' +
                '}';
    }

    public String getTimeLen() {
        return timeLen;
    }

    public void setTimeLen(String timeLen) {
        this.timeLen = timeLen;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public long getFsize() {
        return fsize;
    }

    public void setFsize(long fsize) {
        this.fsize = fsize;
    }
}
