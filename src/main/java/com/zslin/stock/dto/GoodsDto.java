package com.zslin.stock.dto;

/**
 * Created by zsl on 2018/5/25.
 */
public class GoodsDto {

    private Integer id;

    private Integer amount;

    public GoodsDto() {}

    public GoodsDto(Integer id, Integer amount) {
        this.id = id;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "GoodsDto{" +
                "id=" + id +
                ", amount=" + amount +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
