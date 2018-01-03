#include "my_utils.h"

#include "md5.h"

void cryptMd5(const char *source, char * target, short targetLen, unsigned char zeroIsUpper) {
    int sourceLen = strlen(source);

    unsigned char encrypt[sourceLen], decrypt[targetLen/2];

    for (int i = 0; i < sourceLen; i++) {
        encrypt[i] = *source;
        source++;
    }

    MD5_CTX md5;
    MD5Init(&md5);
    MD5Update(&md5, encrypt, sourceLen);
    MD5Final(&md5, decrypt);

    ByteToHexStr(decrypt, target, targetLen/2, zeroIsUpper);

    LOGE("加密前:%s\n加密后:%s", encrypt, target);

    return;
}

void ByteToHexStr(const unsigned char* source, char* dest, int sourceLen, int zeroIsUpper) {
    short i,wordLen;
    unsigned char highByte, lowByte;

    wordLen = zeroIsUpper == 0 ? 0x07 : 0x27;

    for (i = 0; i < sourceLen; i++) {
        highByte = source[i] >> 4;
        lowByte = source[i] & 0x0f ;

        highByte += 0x30; //ascii code 0

        if (highByte > 0x39)  //ascii code 9
            dest[i * 2] = highByte + wordLen;
        else
            dest[i * 2] = highByte;

        lowByte += 0x30;
        if (lowByte > 0x39)
            dest[i * 2 + 1] = lowByte + wordLen;
        else
            dest[i * 2 + 1] = lowByte;
    }
    return ;
}