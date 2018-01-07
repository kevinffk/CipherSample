#include "des3_native_lib.h"

#include "my_utils.h"

#include "des.h"

extern "C" {

JNIEXPORT jstring JNICALL
Java_com_kv_des3_Des3NativeLib_getEncodeDesHexStr__Ljava_lang_String_2Ljava_lang_String_2(
        JNIEnv *env, jclass type, jstring source_, jstring key_) {
    const char *source = env->GetStringUTFChars(source_, 0);
    const char *key = env->GetStringUTFChars(key_, 0);

    char * inArray = (char *) malloc(9 * sizeof(char));
    char * keyArray = (char *) malloc(9 * sizeof(char));
    int * output = (int *)malloc(65 * sizeof(int));
    char outHex[16];

    hexStr2byte(inArray, source);
    hexStr2byte(keyArray, key);

    DES_Efun(inArray,keyArray, output);

    bit2HexStr(outHex, output, 64);

    //free
    free(inArray);
    free(keyArray);
    free(output);

    env -> ReleaseStringUTFChars(source_, source);
    env -> ReleaseStringUTFChars(key_, key);

    return env -> NewStringUTF(outHex);

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

JNIEXPORT jstring JNICALL
Java_com_kv_des3_Des3NativeLib_getDecodeDesHexStr(JNIEnv *env, jclass type, jstring codeHex_, jstring keyHex_) {
    const char *codeHex = env->GetStringUTFChars(codeHex_, 0);
    const char *keyHex = env->GetStringUTFChars(keyHex_, 0);

    int * codeBit = (int *) malloc(65 * sizeof(int));
    char * keyArray = (char *) malloc(9 * sizeof(char));
    char * outArray = (char *) malloc(9 * sizeof(char));
    char outHex[16];

    hexStr2Bit(codeBit, codeHex, 16);

    hexStr2byte(keyArray, keyHex);

    DES_Dfun(codeBit, keyArray, outArray);

    byte2HexStr(outHex, outArray, 8);

    env->ReleaseStringUTFChars(codeHex_, codeHex);
    env->ReleaseStringUTFChars(keyHex_, keyHex);

    free(codeBit);
    free(keyArray);
    free(outArray);

    return env->NewStringUTF(outHex);
}

}