#include <jni.h>

#include "des3_utils.h"

extern "C" {

JNIEXPORT jstring JNICALL
Java_com_kv_des3_Des3NativeLib_getEncodeDesHexStr__Ljava_lang_String_2Ljava_lang_String_2(
        JNIEnv *env, jclass type, jstring source_, jstring key_) {
    const char *source = env->GetStringUTFChars(source_, 0);
    const char *key = env->GetStringUTFChars(key_, 0);

    char enCodeHexStr[16];
    char msgArray[8],keyArray[8];

    for (int i = 0; i < 8; i++) {
        msgArray[i] = *source;
        keyArray[i] = *key;
        source++;
        key++;
    }

    DesEncode(msgArray, keyArray, enCodeHexStr);

    env->ReleaseStringUTFChars(source_, source);
    env->ReleaseStringUTFChars(key_, key);

    return env->NewStringUTF(enCodeHexStr);
}

}
