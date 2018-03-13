package com.wcl.unity.call;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wcl on 2018/3/10.
 */

public class UnityCallAndroidBinder {
    private static Map<String, UnityCallAndroidInject> callBackInjectMap = new HashMap<>();
    public static void inject(Object host){
        String name = host.getClass().getName();
        UnityCallAndroidInject unityCallAndroidInject = callBackInjectMap.get(name);
        if(unityCallAndroidInject == null){
            try {
                Class<?> aClass = Class.forName(name + "$$" + "BindUnityCallAndroidInject");
                unityCallAndroidInject = (UnityCallAndroidInject)(aClass.newInstance());
                unityCallAndroidInject.inject(host);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
    }

    public static void unInject(Object host){
        String name = host.getClass().getName();
        UnityCallAndroidInject unityCallAndroidInject = callBackInjectMap.get(name);
        if(unityCallAndroidInject != null){
            unityCallAndroidInject.unInject(host);
        }
    }
}
