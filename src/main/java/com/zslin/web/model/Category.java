package com.zslin.web.model;

import com.zslin.web.vo.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by 钟述林 393156105@qq.com on 2017/1/23 11:34.
 * 菜品分类
 */
@Entity
@Table(name = "t_category")
@Data
public class Category extends BaseEntity implements Serializable {

    private String name;

    @Column(name = "order_no")
    private Integer orderNo = 1;

    private String remark;

    @Column(name = "pic_path")
    private String picPath;

    @Column(name = "store_id")
    private Integer storeId;

    @Column(name = "store_name")
    private String storeName;

    @Column(name = "store_sn")
    private String storeSn;

    /** 外卖标识 */
    private String outFoodFlag = "0";
}
