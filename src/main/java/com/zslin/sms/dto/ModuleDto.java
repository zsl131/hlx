package com.zslin.sms.dto;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/14 15:57.
 * 接口方提供的短信模板DTO对象
 */
public class ModuleDto {

    private Integer id;

    private String status;

    private String sign;

    private String content;

    @Override
    public String toString() {
        return "ModuleDto{" +
                "id=" + id +
                ", status='" + status + '\'' +
                ", sign='" + sign + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
