package com.zslin.web.vo;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by 钟述林 393156105@qq.com on 2017/1/16 16:08.
 * 基础基类,所有的vo都继承于该类,里面放了一些共有属性
 */
@MappedSuperclass
@Data
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** 创建日期，Date类型 */
    @Column(name = "create_date")
    private Date createDate;

    /** 创建日期，Long类型 */
    @Column(name = "create_long")
    private Long createLong;

    /** 创建日期，格式 yyyy-MM-dd */
    @Column(name = "create_day")
    private String createDay;

    /** 创建日期，格式 yyyy-MM-dd HH:mm:ss */
    @Column(name = "create_time")
    private String createTime;

    private String reserve1;

    private String reserve2;
}
