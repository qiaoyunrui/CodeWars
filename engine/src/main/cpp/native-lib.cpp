/**
 * Created by Juhezi on 2017/12/28.
 */

#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring

JNICALL
Java_me_juhezi_module_engine_jni_Knife_stringFromJNI(
        JNIEnv *env,
        jobject /*this*/) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}