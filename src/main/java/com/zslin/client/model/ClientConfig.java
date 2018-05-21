package com.zslin.client.model;

import javax.persistence.*;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/10 10:18.
 * 客户端配置管理
 */
@Entity
@Table(name = "c_client_config")
public class ClientConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /** 公司简称 */
    private String name;

    /** 公司全称 */
    @Column(name = "long_name")
    private String longName;

    /** 上传数据时间间隔，单位秒 */
    @Column(name = "upload_time")
    private Integer uploadTime;

    /** 下载数据时间间隔，单位秒 */
    @Column(name = "download_time")
    private Integer downloadTime;

    /** token识别客户端的唯一标识 */
    private String token;

    /** 状态 */
    private String status="";

    @Column(name = "client_version")
    private String clientVersion;

    /** 用餐时长，单位分钟 */
    @Column(name = "have_time")
    private Integer haveTime;

    /** 平均用餐时间，主要用于告知用户等待时间 */
    @Column(name = "average_have_time")
    private Integer averageHaveTime;

    /** 联系电话，主要显示在小票上 */
    private String phone;

    /** 地址，主要显示在小票上 */
    private String address;

    /** 桌子数量 */
    @Column(name = "desk_count")
    private Integer deskCount;

    /** 坐位数量 */
    @Column(name = "site_count")
    private Integer siteCount;

    /** 新版本下载地址 */
    @Column(name = "version_url")
    private String versionUrl;

    public String getVersionUrl() {
        return versionUrl;
    }

    public void setVersionUrl(String versionUrl) {
        this.versionUrl = versionUrl;
    }

    public String getClientVersion() {
        return clientVersion;
    }

    public void setClientVersion(String clientVersion) {
        this.clientVersion = clientVersion;
    }

    public Integer getDeskCount() {
        return deskCount;
    }

    public void setDeskCount(Integer deskCount) {
        this.deskCount = deskCount;
    }

    public Integer getSiteCount() {
        return siteCount;
    }

    public void setSiteCount(Integer siteCount) {
        this.siteCount = siteCount;
    }

    public Integer getHaveTime() {
        return haveTime;
    }

    public void setHaveTime(Integer haveTime) {
        this.haveTime = haveTime;
    }

    public Integer getAverageHaveTime() {
        return averageHaveTime;
    }

    public void setAverageHaveTime(Integer averageHaveTime) {
        this.averageHaveTime = averageHaveTime;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public Integer getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Integer uploadTime) {
        this.uploadTime = uploadTime;
    }

    public Integer getDownloadTime() {
        return downloadTime;
    }

    public void setDownloadTime(Integer downloadTime) {
        this.downloadTime = downloadTime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
