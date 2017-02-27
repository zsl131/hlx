package com.zslin.basic.repository;

/**
 * Created by 钟述林 393156105@qq.com on 2017/1/6 17:22.
 */
public class SpecificationOperator {

    /**
     * 对应数据表字段
     */
    private String key;

    /**
     * 对应值
     */
    private Object value;

    /**
     * 操作符
     */
    private String oper;

    /**
     * 关联方式，如：and、or
     */
    private String join;

    public SpecificationOperator(String key, String oper, Object value, String join) {
        this.key = key;
        this.oper = oper;
        this.value = value;
        this.join = join;
    }

    public SpecificationOperator(String key, String oper, Object value) {
        this(key, oper, value, "and");
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getOper() {
        return oper;
    }

    public void setOper(String oper) {
        this.oper = oper;
    }

    public String getJoin() {
        return join;
    }

    public void setJoin(String join) {
        this.join = join;
    }
}
