package com.zslin.business.dto;

import lombok.Data;

/**
 * 收入凭证检测DTO对象
 * 检测是否已上传凭证
 */
@Data
public class IncomeTicketDto {

    private String storeSn;

    private String targetDay;

    /**
     * 状态 0-未上传；-1-无法访问；-2-未登记收入；1-正常
     */
    private String status = "1";

    public IncomeTicketDto() {
    }

    public IncomeTicketDto(String storeSn, String targetDay, String status) {
        this.storeSn = storeSn;
        this.targetDay = targetDay;
        this.status = status;
    }
}
