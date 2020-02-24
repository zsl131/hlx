package com.zslin.client.model;

import javax.persistence.*;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/6 9:40.
 */
@Entity
@Table(name = "c_code")
public class Code {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String c;

    @Column(name = "method_name")
    private String methodName;

    @Column(name = "service_name")
    private String serviceName;

    private String remark;

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
