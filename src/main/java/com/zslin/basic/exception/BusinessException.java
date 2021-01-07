package com.zslin.basic.exception;

import lombok.Data;

/**
 * 业务异常
 */
@Data
public class BusinessException extends RuntimeException {

    private String code;

    private String msg;

    public BusinessException() {
        super();
    }

    public BusinessException(String msg) {super(msg); this.msg = msg;}

    public BusinessException(String code, String msg) {super(msg); this.code = code; this.msg = msg;}

    public class Code {
        /** 默认异常代码 */
        public static final String DEFAULT_ERR_CODE = "10000";

        /** 参数为空 */
        public static final String PARAM_NULL = "10001";

        /** 系统配置为空 */
        public static final String CONFIG_NULL = "10002";

        /** 表单验证异常 */
        public static final String VALIDATE_ERR = "10003";

        /** 有子元素异常 */
        public static final String HAVE_SUBELEMENT = "10004";

        /** 数据已存在 */
        public static final String HAS_EXISTS = "10005";

        /** 状态异常 */
        public static final String STATUS_ERROR = "10006";

        /** 权限异常 */
        public static final String AUTH_ERROR = "10007";

        /** 未找到接口 */
        public static final String NO_SUCH_METHOD = "20001";

        /** 无法调用private方法 */
        public static final String ILLEGAL_ACCESS = "20002";

        /** 字符编码异常 */
        public static final String ENCODING = "20003";

        /** 未找到Bean */
        public static final String NO_BEAN_DEF = "20004";

        /** 接口格式错误 */
        public static final String API_ERR_FORMAT = "20005";
    }

    public class Message {
        /** 默认异常代码 */
        public static final String DEFAULT_ERR_CODE = "出现异常";

        /** 参数为空 */
        public static final String PARAM_NULL = "参数为空";

        /** 系统配置为空 */
        public static final String CONFIG_NULL = "系统配置为空";

        /** 表单验证异常 */
        public static final String VALIDATE_ERR = "表单验证异常";

        public static final String HAVE_SUBELEMENT = "存在子元素";

        public static final String HAS_EXISTS = "数据已存在";


        /** 未找到接口 */
        public static final String NO_SUCH_METHOD = "接口不存在";

        /** 无法调用private方法 */
        public static final String ILLEGAL_ACCESS = "接口私有化不可调用";

        /** 字符编码异常 */
        public static final String ENCODING = "字符编码异常";

        /** 未找到Bean */
        public static final String NO_BEAN_DEF = "接口前缀不存在";

        /** 接口格式错误 */
        public static final String API_ERR_FORMAT = "接口格式错误，必须有且只有一个“.”";
    }
}
