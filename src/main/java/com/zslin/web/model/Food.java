package com.zslin.web.model;

import com.zslin.web.vo.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by 钟述林 393156105@qq.com on 2017/1/23 11:35.
 */
@Entity
@Table(name = "t_food")
@Data
public class Food extends BaseEntity {

    @Column(name = "cate_id")
    private Integer cateId;

    @Column(name = "cate_name")
    private String cateName;

    /** 序号 */
    @Column(name = "order_no")
    private Integer orderNo;

    /** 点评数量 */
    @Column(name = "comment_count")
    private Integer commentCount=0;

    /** 点赞次数 */
    @Column(name = "good_count")
    private Integer goodCount=0;

    /** 图片地址 */
    @Column(name = "pic_path")
    private String picPath;

    /** 状态，1-可就餐；0-已下餐 */
    private String status;

    /** 名称 */
    private String name;

    @Column(name = "store_id")
    private Integer storeId;

    @Column(name = "store_name")
    private String storeName;

    @Column(name = "store_sn")
    private String storeSn;

    /** 备注 */
    private String remark;

    /** 单位名称 */
    @Column(name = "unit_name")
    private String unitName;

    private Float price;

    /** 售卖类型，1-堂食；2-外卖；3-均可 */
    @Column(name = "sale_mode")
    private String saleMode="3";

    /** 编号 */
    private String sn;

    /** 名称字母 */
    private String nameLetter;

    private Integer snNo;

    /** 打印标记，0-不打印；1-吧台；2-厨房；3-甜品店；all-全部 */
    @Column(name = "print_flag")
    private String printFlag;

    /** 外卖标识 */
    private String outFoodFlag = "0";
}
