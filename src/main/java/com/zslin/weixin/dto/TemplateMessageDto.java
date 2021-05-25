package com.zslin.weixin.dto;

/**
 * Created by zsl on 2018/8/28.
 */
public class TemplateMessageDto {

    private String name;

    private String rules;

    public String toString() {
        return name + "：" + rules;
    }

    public TemplateMessageDto() {
    }

    public TemplateMessageDto(String name, String rules) {
        this.name = name;
        this.rules = rules;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }
}
