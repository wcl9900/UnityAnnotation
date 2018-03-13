package com.wcl.unity.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Unity回调Android接口拦截器，方法参数必须满足(Context context, String method, String args)
 * Created by wangchunlong on 2018/3/11.
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface UnityCallAndroidIntercept {
}
