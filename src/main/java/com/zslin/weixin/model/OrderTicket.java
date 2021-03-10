package com.zslin.weixin.model;

import lombok.Data;

import javax.persistence.*;

/**
 * 汉丽轩活动领券登记
 */
@Entity
@Table(name = "wx_order_ticket")
@Data
public class OrderTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String orderNo;

    //生成二维码时为0，领券后为1
    private String status = "0";

    private String storeSn;
}
