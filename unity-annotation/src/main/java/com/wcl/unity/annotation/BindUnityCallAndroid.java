package com.wcl.unity.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Unity回调接口对象自动注入,该注释作用于属性字段，被绑定的属性类型需为{@link Object}
 * Created by wangchunlong on 2018/3/10.
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
public @interface BindUnityCallAndroid {
}
