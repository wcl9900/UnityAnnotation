package com.wcl.unity.annotation.callAndroid;

import android.content.Context;
import android.widget.Toast;

import com.wcl.unity.annotation.UnityCallAndroidIntercept;

/**
 * Created by wcl on 2018/3/11.
 */

public class UnityCallAndroidInterceptTest {
    @UnityCallAndroidIntercept
    public static void intercept(Context context, String method, String args){
        Toast.makeText(context, "CallAndroid拦截"+ " method:"+method + " args:"+args, Toast.LENGTH_SHORT).show();
    }
}
