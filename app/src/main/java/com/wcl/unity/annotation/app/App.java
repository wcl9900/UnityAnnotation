package com.wcl.unity.annotation.app;

import android.app.Application;

/**
 * Created by wangchunlong on 2018/3/12.
 */

public class App extends Application {
    private static Application instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static Application getInstance() {
        return instance;
    }
}
