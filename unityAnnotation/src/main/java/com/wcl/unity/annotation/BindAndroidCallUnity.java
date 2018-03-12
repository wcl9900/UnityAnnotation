package com.wcl.unity.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 此接口绑定于class级别，用来创建AndroidCallUnity的管理器类，
 * 如果{@link BindAndroidCallUnity#className()}不设定，则默认生成的管理器类名为“被绑定类名称_”，
 * 否则管理器类名为{@link BindAndroidCallUnity#className()}
 * Created by wangchunlong on 2018/3/10.
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface BindAndroidCallUnity {
    /**
     * 生成的管理器的类名
     * @return
     */
    String className() default "";

    /**
     * 要调用的Unity游戏对象GameObject的名称
     * @return
     */
    String gameObject() default "unityCall";
}
