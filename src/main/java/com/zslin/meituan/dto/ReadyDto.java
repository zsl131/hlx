package com.zslin.meituan.dto;

/**
 * Created by 钟述林 393156105@qq.com on 2017/7/5 16:40.
 */
public class ReadyDto {

    //最大可验证的张数
    private Integer count;

    //券码
    private String couponCode;

    //项目Id
    private Integer dealId;

    //项目名称
    private String dealTitle;

    //操作状态，0表示操作成功
    private Integer result;

    //市场价
    private Double dealValue;

    //售价
    private Double dealPrice;

    @Override
    public String toString() {
        return "ReadyDto{" +
                "count=" + count +
                ", couponCode='" + couponCode + '\'' +
                ", dealId=" + dealId +
                ", dealTitle='" + dealTitle + '\'' +
                ", result=" + result +
                ", dealValue=" + dealValue +
                ", dealPrice=" + dealPrice +
                '}';
    }

    public Double getDealValue() {
        return dealValue;
    }

    public void setDealValue(Double dealValue) {
        this.dealValue = dealValue;
    }

    public Double getDealPrice() {
        return dealPrice;
    }

    public void setDealPrice(Double dealPrice) {
        this.dealPrice = dealPrice;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public Integer getDealId() {
        return dealId;
    }

    public void setDealId(Integer dealId) {
        this.dealId = dealId;
    }

    public String getDealTitle() {
        return dealTitle;
    }

    public void setDealTitle(String dealTitle) {
        this.dealTitle = dealTitle;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }
}
