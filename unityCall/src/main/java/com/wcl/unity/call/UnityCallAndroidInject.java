package com.wcl.unity.call;

/**
 * Created by wcl on 2018/3/10.
 */

public interface UnityCallAndroidInject<T> {
    void inject(T host);
    void unInject(T host);
}
