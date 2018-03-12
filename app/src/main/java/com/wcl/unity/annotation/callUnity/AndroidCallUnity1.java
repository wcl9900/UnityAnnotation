package com.wcl.unity.annotation.callUnity;

import android.widget.Toast;

import com.wcl.unity.annotation.AndroidCallUnity;
import com.wcl.unity.annotation.app.App;
import com.wcl.unity.annotation.entity.Student;

/**
 * Created by wcl on 2018/3/10.
 */

public class AndroidCallUnity1 {

    @AndroidCallUnity(UnityMethod = "AndroidCallUnity")
    public static String callUnity11(String msg){
        Toast.makeText(App.getInstance(), "callUnity11", Toast.LENGTH_SHORT).show();
        return msg;
    }

    @AndroidCallUnity
    public static String callUnity12(Student student){
        Toast.makeText(App.getInstance(), "callUnity12", Toast.LENGTH_SHORT).show();
        return student.toString();
    }
}
