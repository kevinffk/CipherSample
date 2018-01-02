#include <jni.h>
#include <string>

#include "cipher_native_lib.h"

#include "md5.h"
#include "my_utils.h"
#include <string.h>


extern "C" {
JNIEXPORT jstring JNICALL
Java_com_kv_ciphersample_CipherNativeLib_stringFromJNI(JNIEnv *env, jclass type) {

    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

JNIEXPORT jstring JNICALL
Java_com_kv_ciphersample_CipherNativeLib_cryptMd5(JNIEnv *env, jclass type, jstring source_) {
    const char *source = env->GetStringUTFChars(source_, 0);

    int len = strlen(source);

    unsigned char encrypt[len];//21232f297a57a5a743894a0e4a801fc3
    unsigned char decrypt[16];

    for (int i = 0; i<len; i++) {
        encrypt[i]=*source;
        source++;
    }

    MD5_CTX md5;
    MD5Init(&md5);
    MD5Update(&md5,encrypt,len);
    MD5Final(&md5,decrypt);

    char target[32];

    ByteToHexStr(decrypt, target, 16);

    LOGE("加密前:%s\n加密后:%s",encrypt, target);

    env->ReleaseStringUTFChars(source_, source);

    return env->NewStringUTF(target);
}
}