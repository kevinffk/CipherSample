#include <malloc.h>
#include <jni.h>

#include "md5.h"
#include "des.h"
#include "my_utils.h"
#include "cipher_native_lib.h"

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

void bit2HexStr1(char * hexOut, int *bitIn, int bitLen) {
    int i = 0;
    for (i = 0; i < bitLen / 4; i++) {
        hexOut[i] = 0;
    }
    for (i = 0; i < bitLen / 4; i++) {
        hexOut[i] = (bitIn[i * 4] << 3) + (bitIn[i * 4 + 1] << 2)
                    + (bitIn[i * 4 + 2] << 1) + bitIn[i * 4 + 3];
        if ((hexOut[i] % 16) > 9) {
            hexOut[i] = hexOut[i] % 16 + '7';//余数大于9时处理 10-15 to A-F
        } else {
            hexOut[i] = hexOut[i] % 16 + '0';//输出字符
        }
    }
    hexOut[i] = '\0';
}

JNIEXPORT jstring JNICALL
Java_com_kv_ciphersample_CipherNativeLib_getEncodeDesHexStr(JNIEnv *env, jclass type, jstring msgHex_, jstring keyHex_) {
    const char *msgHex = env->GetStringUTFChars(msgHex_, 0);
    const char *keyHex = env->GetStringUTFChars(keyHex_, 0);

    char * inArray = (char *) malloc(9 * sizeof(char));
    char * keyArray = (char *) malloc(9 * sizeof(char));
    int * output = (int *)malloc(64 * sizeof(int));
    char outHex[17];

    hexStr2byte(inArray, msgHex);
    hexStr2byte(keyArray, keyHex);

    DES_Efun(inArray,keyArray, output);

    bit2HexStr1(outHex, output, 64);
    //free
    free(inArray);
    free(keyArray);
    free(output);

    env -> ReleaseStringUTFChars(msgHex_, msgHex);
    env -> ReleaseStringUTFChars(keyHex_, keyHex);
    return env -> NewStringUTF(outHex);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_kv_ciphersample_CipherNativeLib_getDecodeDesHexStr(JNIEnv *env, jclass type,
                                                            jstring codeHex_, jstring keyHex_) {
    const char *codeHex = env->GetStringUTFChars(codeHex_, 0);
    const char *keyHex = env->GetStringUTFChars(keyHex_, 0);

    int * codeBit = (int *) malloc(65 * sizeof(int));
    char * keyArray = (char *) malloc(9 * sizeof(char));
    char * outArray = (char *) malloc(9 * sizeof(char));
    char outHex[17];

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