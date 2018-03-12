package com.wcl.unity.annotation.callAndroid;

import android.content.Context;
import android.widget.Toast;

import com.wcl.unity.annotation.UnityCallAndroid;

/**
 * Created by wcl on 2018/3/10.
 */

public class UnityCallAndroid1 {
    @UnityCallAndroid
    public static final void callAndroid11(Context context, String args){

        Toast.makeText(context, "callAndroid11", Toast.LENGTH_SHORT).show();
    }
    @UnityCallAndroid
    public static final void callAndroid12(Context context, String args){

        Toast.makeText(context, "callAndroid12", Toast.LENGTH_SHORT).show();
    }
    @UnityCallAndroid
    public static final void callAndroid13(Context context, String args){

        Toast.makeText(context, "callAndroid13", Toast.LENGTH_SHORT).show();
    }
}
