#include <jni.h>

#include "des3_utils.h"

#include <android/log.h>

#include <string.h>

#define TAG "xx" // 这个是自定义的LOG的标识
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,TAG ,__VA_ARGS__) // 定义LOGD类型
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,TAG ,__VA_ARGS__) // 定义LOGI类型
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN,TAG ,__VA_ARGS__) // 定义LOGW类型
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,TAG ,__VA_ARGS__) // 定义LOGE类型
#define LOGF(...) __android_log_print(ANDROID_LOG_FATAL,TAG ,__VA_ARGS__) // 定义LOGF类型

extern "C" {

JNIEXPORT jstring JNICALL
Java_com_kv_des3_Des3NativeLib_getEncodeDesHexStr__Ljava_lang_String_2Ljava_lang_String_2(JNIEnv *env, jclass type, jstring source_, jstring key_) {
    const char *source = env->GetStringUTFChars(source_, 0);
    const char *key = env->GetStringUTFChars(key_, 0);

    char msgArray[16]={0};
    char keyArray[16]={0};
    char enCodeHexStr[16]={0};

    strcpy(msgArray, source);
    strcpy(keyArray, key);

    DesEncodeHex(keyArray, msgArray, enCodeHexStr);



    env->ReleaseStringUTFChars(source_, source);
    env->ReleaseStringUTFChars(key_, key);


    return env->NewStringUTF(enCodeHexStr);
}

}
