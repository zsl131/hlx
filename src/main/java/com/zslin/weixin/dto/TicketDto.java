package com.zslin.weixin.dto;

import lombok.Data;

/**
 * 电子券DTO对象
 */
@Data
public class TicketDto {

    private Float worth;

    private String sn;

    private String orderNo;
}
