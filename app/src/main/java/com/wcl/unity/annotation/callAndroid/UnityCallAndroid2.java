package com.wcl.unity.annotation.callAndroid;

import android.content.Context;
import android.widget.Toast;

import com.wcl.unity.annotation.UnityCallAndroid;

/**
 * Created by wcl on 2018/3/10.
 */

public class UnityCallAndroid2 {

    @UnityCallAndroid
    public static void callAndroid21(Context context, String value){

        Toast.makeText(context, "callAndroid21", Toast.LENGTH_SHORT).show();
    }
    @UnityCallAndroid
    public static void callAndroid22(Context context, String value){
        Toast.makeText(context, "callAndroid22", Toast.LENGTH_SHORT).show();
    }
}
