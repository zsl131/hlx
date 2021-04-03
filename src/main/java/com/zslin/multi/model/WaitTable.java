package com.zslin.multi.model;

import lombok.Data;

import javax.persistence.*;

/**
 * 排队
 */
@Data
@Entity
@Table(name = "m_wait_table")
public class WaitTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String storeSn;

    private String storeName;

    /** 餐桌类型ID */
    private Integer cateId;

    /** 餐桌类型名称 */
    private String cateName;

    /** 餐桌类型标记，如A、B、C */
    private String cateFlag;

    private String phone;

    private Integer peopleCount=0;

    /** 等待编号 */
    private String waitNo;

    /**
     * 状态，0-排队中；1-已就餐；2-作废；3-顾客取消
     */
    private String status = "0";

    /** 处理时间 */
    private String processTime;

    /** 有人扫码时需要绑定 */
    private String openid;

    private String nickname;

    /** 格式yyyyMMdd */
    private String createDay;

    private String createTime;

    private Long createLong;
}
