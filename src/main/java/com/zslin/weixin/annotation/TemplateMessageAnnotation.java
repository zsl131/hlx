package com.zslin.weixin.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by zsl on 2018/8/25.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TemplateMessageAnnotation {

    /** 模板名称 */
    String name();

    /** 键，多个键用“-”隔开 */
    String keys();
}
