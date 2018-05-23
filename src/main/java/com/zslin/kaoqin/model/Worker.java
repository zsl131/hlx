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

    private Integer auth=0;

    private String identity;

    private String phone;

    @Lob
    private String finger1;

    @Lob
    private String finger2;

    @Lob
    private String face1;

    @Lob
    private String face2;

    private String openid;

    /**
     * 是否为收银员
     * 当有顾客办卡等业务时将通知收银员（只有无当前收银员时才通知）
     */
    private String isCashier="0";

    /** 自己上传的头像 */
    @Column(name = "head_pic")
    private String headPic;

    @Column(name = "account_id")
    private Integer accountId;

    /** 微信端头像 */
    private String headimgurl;

    /** 状态，1-在职；2-离职 */
    private String status;

    /**
     *  操作权限，主要用来做库存管理的权限控制
     *  格式如：-1-2-3-4-，1-采购员；2-出库员；3-入库员；4-审核员；10-管理员
     *  一个人可拥有多种权限
     */
    private String operator;

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public String getIsCashier() {
        return isCashier;
    }

    public void setIsCashier(String isCashier) {
        this.isCashier = isCashier;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
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
