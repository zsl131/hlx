package com.zslin.admin.dto;

/**
 * Created by 钟述林 393156105@qq.com on 2017/6/12 10:25.
 */
public class MyTicketDto {

    private Integer prizeId;

    private String prizeName;

    public MyTicketDto(Integer prizeId, String prizeName) {
        this.prizeId = prizeId;
        this.prizeName = prizeName;
    }

    @Override
    public int hashCode() {
        return this.prizeId;
    }

    @Override
    public boolean equals(Object obj) {
        MyTicketDto d = (MyTicketDto)obj;
        System.out.println(d.getPrizeId()+"==="+this.getPrizeId()+"=="+d.getPrizeId().equals(this.getPrizeId()));
        return d.getPrizeId()==(this.getPrizeId());
    }

    public Integer getPrizeId() {
        return prizeId;
    }

    public void setPrizeId(Integer prizeId) {
        this.prizeId = prizeId;
    }

    public String getPrizeName() {
        return prizeName;
    }

    public void setPrizeName(String prizeName) {
        this.prizeName = prizeName;
    }
}
