package com.zslin.kaoqin.model;

import javax.persistence.*;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/26 23:01.
 * 公司配置信息
 */
@Entity
@Table(name = "k_company")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;
    private String company;
    /**
     * 表示无新数据请求服务器间隔，单位秒
     */
    private Integer delay=50;

    /**
     * 当请求服务器发生错误时，再次发送请求的间隔，单位秒
     */
    private Integer errdelay=10;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Integer getDelay() {
        return delay;
    }

    public void setDelay(Integer delay) {
        this.delay = delay;
    }

    public Integer getErrdelay() {
        return errdelay;
    }

    public void setErrdelay(Integer errdelay) {
        this.errdelay = errdelay;
    }
}
