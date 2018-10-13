package com.zslin.card.model;

import javax.persistence.*;

/**
 * 卡券
 * Created by zsl on 2018/10/12.
 */
@Entity
@Table(name = "c_card")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /** 编号 */
    private Integer no;

    /** 类型，1-10元；2-45元；3-55元 */
    private String type;

    /** 状态，0-可使用；1-已使用；2-作废 */
    private String status="0";

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNo() {
        return no;
    }

    public void setNo(Integer no) {
        this.no = no;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
