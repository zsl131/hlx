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
}
