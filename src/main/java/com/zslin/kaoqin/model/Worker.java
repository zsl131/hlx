package com.zslin.kaoqin.model;

import javax.persistence.*;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/26 23:36.
 * 员工
 */
@Entity
@Table(name = "k_worker")
public class Worker {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;

    private String password;

    @Column(name = "dep_id")
    private Integer depId;

    private Integer auth;

    @Lob
    private String finger1;

    @Lob
    private String finger2;

    @Lob
    private String face1;

    @Lob
    private String face2;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getDepId() {
        return depId;
    }

    public void setDepId(Integer depId) {
        this.depId = depId;
    }

    public Integer getAuth() {
        return auth;
    }

    public void setAuth(Integer auth) {
        this.auth = auth;
    }

    public String getFinger1() {
        return finger1;
    }

    public void setFinger1(String finger1) {
        this.finger1 = finger1;
    }

    public String getFinger2() {
        return finger2;
    }

    public void setFinger2(String finger2) {
        this.finger2 = finger2;
    }

    public String getFace1() {
        return face1;
    }

    public void setFace1(String face1) {
        this.face1 = face1;
    }

    public String getFace2() {
        return face2;
    }

    public void setFace2(String face2) {
        this.face2 = face2;
    }
}
