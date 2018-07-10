# UnityAnnotation

    Android与Unity接口交互自动注入框架。旨在通过注解apt的方式管理android与unity的交互接口。

#使用方式
##库引用
    repositories {
        maven { url 'https://dl.bintray.com/wangchunlong/maven' }
    }

    compile 'com.wcl.unity.interaction:unity-call:1.3'
    annotationProcessor "com.wcl.unity.interaction:unity-compiler:1.3"

##库使用

     1.Android调用Unity接口（简单3步）
        
         1).注释接口管理器
         @BindAndroidCallUnity(className = "CallUnityManager", gameObject = "UnityCall")
         public class AndroidCallUnityManager {
         }
            
        2).注入调用方法
         @AndroidCallUnity(UnityMethod = "AndroidCallUnity")
         public static String callUnity11(String msg){
             Toast.makeText(App.getInstance(), "callUnity11", Toast.LENGTH_SHORT).show();
             return msg;
         }
        
        3).调用
        CallUnityManager.callUnity11("callUnity");


      2.Unity调用Android接口(简单2步)
        
        1).在展示Unity场景的Activity中注入（变量名称自定义）
            @BindUnityCallAndroid
            Object functionUnityManage;
        
        2）.注释Unity回调Android的方法
            @UnityCallAndroid
            public static final void callAndroid11(Context context, String args){
                Toast.makeText(context, "callAndroid11", Toast.LENGTH_SHORT).show();
            }
        
    #更多使用方式可查看demo
        