package com.wcl.unity.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Android调用Unity接口注释，方法必须满足（Object data）
 * Created by wangchunlong on 2018/3/10.
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface AndroidCallUnity {
    /**
     * define method in unity, method will set be android method if value is empty
     * @return
     */
    String UnityMethod() default "";
}
