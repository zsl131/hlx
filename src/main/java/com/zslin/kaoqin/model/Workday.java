package com.zslin.kaoqin.model;

import javax.persistence.*;

/**
 * Created by 钟述林 393156105@qq.com on 2017/4/17 15:45.
 * 员工作息时间，每个员工的上下班时间都不一样
 */
@Entity
@Table(name = "k_workday")
public class Workday {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** 员工ID */
    @Column(name = "worker_id")
    private Integer workerId;

    /** 员工姓名 */
    @Column(name = "worker_name")
    private String workerName;

    /** 员工身份证号 */
    @Column(name = "worker_identity")
    private String workerIdentity;

    /** 早上上班时间 */
    private String sbsj1;

    /** 早上下班时间 */
    private String xbsj1;

    /** 中午上班时间 */
    private String sbsj2;

    /** 中午下班时间 */
    private String xbsj2;

    /** 下午上班时间 */
    private String sbsj3;

    /** 下午下班时间 */
    private String xbsj3;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getWorkerId() {
        return workerId;
    }

    public void setWorkerId(Integer workerId) {
        this.workerId = workerId;
    }

    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    public String getWorkerIdentity() {
        return workerIdentity;
    }

    public void setWorkerIdentity(String workerIdentity) {
        this.workerIdentity = workerIdentity;
    }

    public String getSbsj1() {
        return sbsj1;
    }

    public void setSbsj1(String sbsj1) {
        this.sbsj1 = sbsj1;
    }

    public String getXbsj1() {
        return xbsj1;
    }

    public void setXbsj1(String xbsj1) {
        this.xbsj1 = xbsj1;
    }

    public String getSbsj2() {
        return sbsj2;
    }

    public void setSbsj2(String sbsj2) {
        this.sbsj2 = sbsj2;
    }

    public String getXbsj2() {
        return xbsj2;
    }

    public void setXbsj2(String xbsj2) {
        this.xbsj2 = xbsj2;
    }

    public String getSbsj3() {
        return sbsj3;
    }

    public void setSbsj3(String sbsj3) {
        this.sbsj3 = sbsj3;
    }

    public String getXbsj3() {
        return xbsj3;
    }

    public void setXbsj3(String xbsj3) {
        this.xbsj3 = xbsj3;
    }
}
