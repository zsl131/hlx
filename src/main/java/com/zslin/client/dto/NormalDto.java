package com.zslin.client.dto;

/**
 * Created by zsl on 2018/5/17.
 */
public class NormalDto {

    private String key;

    private String value;

    public NormalDto() {
    }

    public NormalDto(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
