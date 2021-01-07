package com.zslin.weixin.model;

import lombok.Data;

import javax.persistence.*;

/**
 * 关键字推送
 */
@Entity
@Table(name = "wx_key_push")
@Data
public class KeyPush {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String keyword;

    /** 类型：1-文本；2-图片 */
    private String flag;

    /** 如果是图片，则是图片地址，如果是文本则是文本内容 */
    private String content;

    /** 备注信息 */
    private String remark;

    /** 是否启用；0-停用；1-启用 */
    private String status = "0";
}
