package com.wcl.unity.annotation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.wcl.unity.annotation.callUnity.CallUnityManager;
import com.wcl.unity.annotation.unityannotation.R;
import com.wcl.unity.call.UnityCallAndroidBinder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class UnityActivity extends AppCompatActivity implements View.OnClickListener{

    @BindUnityCallAndroid
    Object functionUnityManage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.unity_layout);
        UnityCallAndroidBinder.inject(this);

        findViewById(R.id.button_android_call_unity).setOnClickListener(this);
        findViewById(R.id.button_unity_call_android).setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        UnityCallAndroidBinder.unInject(this);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.button_unity_call_android){
            try {
                Method startAR1 = functionUnityManage.getClass().getDeclaredMethod("callAndroid11", String.class);
                startAR1.setAccessible(true);
                startAR1.invoke(functionUnityManage,  "data");
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        else if(v.getId() == R.id.button_android_call_unity){
            CallUnityManager.callUnity11("callUnity");
        }
    }
}
