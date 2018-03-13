package wcl.com.unity.compiler.utils;

import com.squareup.javapoet.ClassName;

public class TypeUtils {
    public static final ClassName CONTEXT = ClassName.get("android.content", "Context");

    public static final ClassName ACTIVITY = ClassName.get("android.app", "Activity");

    public static final ClassName STRING = ClassName.get("java.lang", "String");

    public static final ClassName BIND_INJECT = ClassName.get("com.wcl.unity.call", "UnityCallAndroidInject");

    public static final ClassName UNITY_PLAYER = ClassName.get("com.unity3d.player", "UnityPlayer");
}