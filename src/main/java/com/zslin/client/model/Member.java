package com.zslin.client.model;

import javax.persistence.*;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/17 22:39.
 * 店内办理的会员，没有绑定微信平台的
 */
@Entity
@Table(name = "t_member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Column(name = "create_time")
    private String createTime;

    @Column(name = "create_long")
    private long createLong;

    @Column(name = "create_day")
    private String createDay;

    private String phone;

    private String password;

    /** 剩余金额，单位分 */
    private Integer surplus=0;

    /** 办理人员姓名 */
    @Column(name = "cachier_name")
    private String cachierName;

    /** 办理人员电话 */
    @Column(name = "cachier_phone")
    private String cachierPhone;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public long getCreateLong() {
        return createLong;
    }

    public void setCreateLong(long createLong) {
        this.createLong = createLong;
    }

    public String getCreateDay() {
        return createDay;
    }

    public void setCreateDay(String createDay) {
        this.createDay = createDay;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getSurplus() {
        return surplus;
    }

    public void setSurplus(Integer surplus) {
        this.surplus = surplus;
    }

    public String getCachierName() {
        return cachierName;
    }

    public void setCachierName(String cachierName) {
        this.cachierName = cachierName;
    }

    public String getCachierPhone() {
        return cachierPhone;
    }

    public void setCachierPhone(String cachierPhone) {
        this.cachierPhone = cachierPhone;
    }
}
