package wcl.com.unity.compiler.utils;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;

/**
 * Created by wangchunlong on 2018/3/10.
 */

public class MessageUtils {
    public static String TAG = "UnityAnnotation";

    public static Messager messager;

    public static void error(String msg, Object... args) {
        messager.printMessage(Diagnostic.Kind.ERROR, String.format(TAG + ">> "+msg, args));
    }

    public static void note(String msg, Object... args) {
        messager.printMessage(Diagnostic.Kind.NOTE, String.format(TAG + ">> "+msg, args));
    }
}
