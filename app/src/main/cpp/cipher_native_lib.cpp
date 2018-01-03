#include <jni.h>
#include "my_utils.h"


extern "C" {

/**
 * 生成32位md5
 * @param env
 * @param type
 * @param source_  需要加密的文字
 * @param isUpper  转换是否需要大写
 * @return
 */
JNIEXPORT jstring JNICALL
Java_com_kv_ciphersample_CipherNativeLib_cryptMd5By32(JNIEnv *env, jclass type, jstring source_, jboolean isUpper) {
    const char *source = env->GetStringUTFChars(source_, 0);

    char target[32];
    cryptMd5(source, target, 32, isUpper == JNI_TRUE ? 0 : 1);

    env->ReleaseStringUTFChars(source_, source);
    return env->NewStringUTF(target);
}

/**
 * 生成16位md5
 * @param env
 * @param type
 * @param source_ 需要加密的文字
 * @param isUpper 转换是否需要大写
 * @return
 */
JNIEXPORT jstring JNICALL
Java_com_kv_ciphersample_CipherNativeLib_cryptMd5By16(JNIEnv *env, jclass type, jstring source_, jboolean isUpper) {
    const char *source = env->GetStringUTFChars(source_, 0);

    char target[16];
    cryptMd5(source, target, 16, isUpper == JNI_TRUE ? 0 : 1);

    env->ReleaseStringUTFChars(source_, source);
    return env->NewStringUTF(target);
}

}
