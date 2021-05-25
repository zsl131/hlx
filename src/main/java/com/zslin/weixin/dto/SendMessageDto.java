package com.zslin.weixin.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SendMessageDto implements Serializable {

    private String templateName;
    private String toUser;
    private String url;
    private String first;
    private List<String> keyValues;
    private List<String> users;

    @Override
    public String toString() {
        return "SendMessageDto{" +
                "templateName='" + templateName + '\'' +
                ", toUser='" + toUser + '\'' +
                ", url='" + url + '\'' +
                ", first='" + first + '\'' +
                ", keyValues=" + keyValues +
                ", users=" + users +
                '}';
    }

    public SendMessageDto(String templateName, String toUser, String url, String first, String... keyValues) {
        this.templateName = templateName;
        this.toUser = toUser;
        this.url = url;
        this.first = first;
        List<String> kvs = new ArrayList<>();
        for(String k : keyValues) {
            kvs.add(k);
        }
        this.keyValues = kvs;
    }

    public SendMessageDto(String templateName, List<String> users, String url, String first, String... keyValues) {
        this.templateName = templateName;
        this.users = users;
        this.url = url;
        this.first = first;
        List<String> kvs = new ArrayList<>();
        for(String k : keyValues) {
            kvs.add(k);
        }
        this.keyValues = kvs;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public List<String> getKeyValues() {
        return keyValues;
    }

    public void setKeyValues(List<String> keyValues) {
        this.keyValues = keyValues;
    }
}
