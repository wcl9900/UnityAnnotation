package com.wcl.unity.annotation.callUnity;

import android.widget.Toast;

import com.wcl.unity.annotation.AndroidCallUnityIntercept;
import com.wcl.unity.annotation.app.App;

/**
 * Created by wcl on 2018/3/11.
 */

public class AndroidCallUnityInterceptTest {
    @AndroidCallUnityIntercept
    public static void intercept(String gameObject, String method, String args){
        Toast.makeText(App.getInstance(), "CallUnity拦截"+ " method:"+method + " args:"+args, Toast.LENGTH_SHORT).show();
    }
}
