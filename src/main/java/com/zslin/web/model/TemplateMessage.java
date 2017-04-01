package com.zslin.web.model;

import javax.persistence.*;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/20 15:27.
 * 微信模板消息
 */
@Entity
@Table(name = "t_template_message")
public class TemplateMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /** 模板标题 */
    private String title;

    /** 参数长度，除去first的，最少3个：keyword1、keyword2、remark */
    @Column(name = "par_len")
    private Integer parLen=3;

    /** 模板Id，微信提供 */
    @Column(name = "temp_id")
    private String tempId;

    /** 具体内容 */
    private String content;

    /** 唯一标识 */
    private String code;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getParLen() {
        return parLen;
    }

    public void setParLen(Integer parLen) {
        this.parLen = parLen;
    }

    public String getTempId() {
        return tempId;
    }

    public void setTempId(String tempId) {
        this.tempId = tempId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
