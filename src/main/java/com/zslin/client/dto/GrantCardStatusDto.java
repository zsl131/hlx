package com.zslin.client.dto;

import java.util.List;

/**
 * 设置状态的DTO对象，用于传输到客户端
 * Created by zsl on 2018/10/15.
 */
public class GrantCardStatusDto {

    private String status;

    private List<Integer> nos;

    public GrantCardStatusDto(String status, List<Integer> nos) {
        this.status = status;
        this.nos = nos;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Integer> getNos() {
        return nos;
    }

    public void setNos(List<Integer> nos) {
        this.nos = nos;
    }
}
