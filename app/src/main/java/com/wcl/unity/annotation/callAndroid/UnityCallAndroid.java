package com.wcl.unity.annotation.callAndroid;

import android.content.Context;
import android.widget.Toast;


/**
 * Created by wcl on 2018/3/10.
 */

public class UnityCallAndroid {
    @com.wcl.unity.annotation.UnityCallAndroid
    public static final void callAndroid11(Context context, String args){
        Toast.makeText(context, "callAndroid11", Toast.LENGTH_SHORT).show();
    }
    @com.wcl.unity.annotation.UnityCallAndroid
    public static final void callAndroid12(Context context, String args){

        Toast.makeText(context, "callAndroid12", Toast.LENGTH_SHORT).show();
    }
    @com.wcl.unity.annotation.UnityCallAndroid
    public static final void callAndroid13(Context context, String args){
        Toast.makeText(context, "callAndroid13", Toast.LENGTH_SHORT).show();
    }
}
