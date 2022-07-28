package com.zslin.basic.repository;

import lombok.Data;

/**
 * Created by 钟述林 393156105@qq.com on 2017/1/6 17:22.
 */
@Data
public class SpecificationOperator {

    private String function;

    private String [] funParams;

    private Class funReturnClass;

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

    public static String FUNCTION_FIELD_PRE = "FUNCTION_FIELD_";

    public static String FUNCTION_VALUE_PRE = "FUNCTION_VALUE_";

    /** 构建数据库字段的参数 */
    public static String buildFunctionFieldParam(String param) {
        return FUNCTION_FIELD_PRE + param;
    }

    /** 构建具体值的参数 */
    public static String buildFunctionValueParam(String param) {
        return FUNCTION_VALUE_PRE + param;
    }

    public SpecificationOperator(String oper, Object value, String function, Class funReturnClass, String ... funParams) {
        this(oper, value, "and", function, funReturnClass, funParams);
    }

    public SpecificationOperator(String oper, Object value, String join, String function, Class funReturnClass, String ... funParams) {
        this.oper = oper;
        this.value = value;
        this.join = join;
        this.function = function;
        this.funReturnClass = funReturnClass;
        this.funParams = funParams;
    }

    public SpecificationOperator(String key, String oper, Object value, String join) {
        this.key = key;
        this.oper = oper;
        this.value = value;
        this.join = join;
    }

    public SpecificationOperator(String key, String oper, Object value) {
        this(key, oper, value, "and");
    }
}
