#include <jni.h>

#include "des3_utils.h"

#include <android/log.h>

#include <string.h>

#include <malloc.h>


#define TAG "xx" // 这个是自定义的LOG的标识
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,TAG ,__VA_ARGS__) // 定义LOGD类型
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,TAG ,__VA_ARGS__) // 定义LOGI类型
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN,TAG ,__VA_ARGS__) // 定义LOGW类型
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,TAG ,__VA_ARGS__) // 定义LOGE类型
#define LOGF(...) __android_log_print(ANDROID_LOG_FATAL,TAG ,__VA_ARGS__) // 定义LOGF类型

extern "C" {



JNIEXPORT jstring JNICALL
Java_com_kv_des3_Des3NativeLib_getEncodeDesHexStr__Ljava_lang_String_2Ljava_lang_String_2(
        JNIEnv *env, jclass type, jstring source_, jstring key_) {
    const char *source = env->GetStringUTFChars(source_, 0);
    const char *key = env->GetStringUTFChars(key_, 0);

    char * inHex = (char *) malloc(16 * sizeof(char));
    char * keyHex = (char *) malloc(16 * sizeof(char));
    char * inArray = (char *) malloc(8 * sizeof(char));
    char * keyArray = (char *) malloc(8 * sizeof(char));
    char outArray[16];

    //常量指针复制到数组
    strcpy(inHex, source);
    strcpy(keyHex, source);

    //hex string 转byte
    hex2byte(inArray, inHex);
    hex2byte(keyArray, keyHex);

    DesEncode(keyArray, inArray, outArray);

    //free
    free(inHex);
    free(keyHex);
    free(inArray);
    free(keyArray);

    env -> ReleaseStringUTFChars(source_, source);
    env -> ReleaseStringUTFChars(key_, key);

    return env -> NewStringUTF(outArray);

}



JNIEXPORT void JNICALL
Java_com_kv_des3_Des3NativeLib_testByte(JNIEnv *env, jclass type, jbyteArray myBytes_) {
    jbyte *myBytes = env->GetByteArrayElements(myBytes_, NULL);
    jsize size = env->GetArrayLength(myBytes_);

    LOGE("xx=%d",size);
    for (int i = 0; i < size; i++) {
        LOGE("xx=%d",myBytes[i]);
    }

    env->ReleaseByteArrayElements(myBytes_, myBytes, 0);
}

}
