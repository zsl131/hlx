package com.zslin.kaoqin.model;

import javax.persistence.*;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/28 0:45.
 * 打卡记录
 */
@Entity
@Table(name = "k_clockin")
public class Clockin {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "worker_id")
    private Integer workerId;

    @Column(name = "worker_name")
    private String workerName;

    private String time;

    @Column(name = "dep_id")
    private Integer depId;

    /** 1:正常，0：迟到或早退 */
    private Integer flag;

    /**
     * 阶段
     * 1-上午上班
     * 2-上午下班
     * 3-下午上班
     * 4-下午下班
     * 5-晚上上班
     * 6-晚上下班
     */
    private String step;

    /** 打卡验证方式，0-密码；1-指纹；2-刷卡；15-人脸 */
    private Integer verify;

    /** 日期 */
    @Column(name = "cur_day")
    private String curDay;

    /** 星期几 */
    private String weekday;

    public String getCurDay() {
        return curDay;
    }

    public void setCurDay(String curDay) {
        this.curDay = curDay;
    }

    public String getWeekday() {
        return weekday;
    }

    public void setWeekday(String weekday) {
        this.weekday = weekday;
    }

    public Integer getVerify() {
        return verify;
    }

    public void setVerify(Integer verify) {
        this.verify = verify;
    }

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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getDepId() {
        return depId;
    }

    public void setDepId(Integer depId) {
        this.depId = depId;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }
}
