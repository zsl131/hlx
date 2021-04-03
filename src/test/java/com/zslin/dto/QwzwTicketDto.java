package com.zslin.dto;

import lombok.Data;

@Data
public class QwzwTicketDto {

    private String openid;

    private String nickname;

    private Integer amount;

    public QwzwTicketDto() {
    }

    public QwzwTicketDto(String openid, String nickname, Integer amount) {
        this.openid = openid;
        this.nickname = nickname;
        this.amount = amount;
    }
}
