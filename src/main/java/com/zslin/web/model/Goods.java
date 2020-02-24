package com.zslin.web.model;

import javax.persistence.*;

/**
 * Created by 钟述林 393156105@qq.com on 2017/8/29 16:42.
 * 物品-用于库存
 */
@Entity
@Table(name = "t_goods")
public class Goods {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** 物品名称 */
    private String name;

    /** 首字母 */
    @Column(name = "first_spell")
    private String firstSpell;

    /** 全拼 */
    private String spell;

    /** 单位 */
    private String unit;

    private String remark;

    /** 供货商 */
    private String supplier;

    /** 供货商电话 */
    @Column(name = "supplier_phone")
    private String supplierPhone;

    /** 类别，1-食材；2-礼品 */
    private String type;

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

    public String getFirstSpell() {
        return firstSpell;
    }

    public void setFirstSpell(String firstSpell) {
        this.firstSpell = firstSpell;
    }

    public String getSpell() {
        return spell;
    }

    public void setSpell(String spell) {
        this.spell = spell;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getSupplierPhone() {
        return supplierPhone;
    }

    public void setSupplierPhone(String supplierPhone) {
        this.supplierPhone = supplierPhone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
