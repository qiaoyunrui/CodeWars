package me.juhezi.module.engine.jni;

/**
 * Created by Juhezi[juhezix@163.com] on 2017/12/28.
 */

public class Knife {

    static {
        System.loadLibrary("native-lib");
    }

    public native String stringFromJNI();

}
