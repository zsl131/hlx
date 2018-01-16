package com.zslin.web.model;

import javax.persistence.*;

/**
 * Created by 钟述林 393156105@qq.com on 2017/4/1 9:39.
 * 商品，主要用于前台下单打单
 * 编号：88888为早餐券；99999为晚餐券
 */
@Entity
@Table(name = "t_commodity")
public class Commodity {

    //早餐券编号（全票）
    public static final String BREAKFAST_NO = "88888";
    //晚餐券编号（全票）
    public static final String DINNER_NO = "99999";

    //早餐券（半票）
    public static final String HALF_BREADFAST_NO = "66666";
    //晚餐券（半票）
    public static final String HALF_DINNER_NO = "77777";
    //免票券
    public static final String FREE_NO = "55555";

    //特殊的编号，不通电的消费
    public static final String SPE_NO = "33333";

    //简餐半票编号
    public static final String SPE_HALF_NO = "22222";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /** 商品名称 */
    private String name;

    /** 商品编号，用于下单输入查询，系统唯一 */
    private String no;

    /** 类型，1-早餐券；2-晚餐券；3-外卖单点（在微信上只展示此类型） */
    private String type;

    /** 商品图片，主要用于微信展示用 */
    @Column(name = "pic_path")
    private String picPath;

    /** 商品描述，也主要用于微信展示用 */
    @Lob
    private String remark;

    /** 单价 */
    private Float price;

    private String status;

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

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }
}
