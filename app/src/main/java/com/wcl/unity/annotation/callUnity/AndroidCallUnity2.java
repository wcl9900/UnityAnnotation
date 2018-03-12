package com.wcl.unity.annotation.callUnity;

import com.wcl.unity.annotation.AndroidCallUnity;
import com.wcl.unity.annotation.entity.Student;

/**
 * Created by wcl on 2018/3/11.
 */

public class AndroidCallUnity2 {
    @AndroidCallUnity
    public static String callUnity21(String msg){
        return msg;
    }

    @AndroidCallUnity
    public static String callUnity22(Student student){
        return student.toString();
    }
}
