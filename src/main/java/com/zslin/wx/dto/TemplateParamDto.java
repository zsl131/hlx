package com.zslin.wx.dto;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/19 23:55.
 */
public class TemplateParamDto {

    /** 参数名称 */
    private String paramName;

    /** 对应值 */
    private String value;

    /** 对应颜色 */
    private String color;

    public TemplateParamDto() {}

    public TemplateParamDto(String paramName, String value, String color) {
        super();
        this.paramName = paramName;
        this.value = value;
        this.color = color;
    }

    public TemplateParamDto(String paramName, String value) {
        this.paramName = paramName;
        this.value = value;
    }

    public TemplateParamDto(EventRemarkDto... dtos) {
        StringBuffer sb = new StringBuffer();
        for(EventRemarkDto dto : dtos) {
            sb.append(dto.getName()).append((dto.getName()==null || "".equals(dto.getName()))?"":"：").append(dto.getValue()).append("\\n");
        }
        this.value = sb.toString();
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
