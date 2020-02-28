package com.zslin.kaoqin.model;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/26 23:36.
 * 员工
 */
@Entity
@Table(name = "k_worker")
@Data
public class Worker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    /** 是否可以发卡 */
    @Column(name = "can_send_card")
    private String canSendCard = "0";

    @Column(name = "store_id")
    private Integer storeId;

    @Column(name = "store_name")
    private String storeName;

    @Column(name = "store_sn")
    private String storeSn;
}
