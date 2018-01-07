#include "my_utils.h"

/**
 * 十六进制字符串 -> 字节流数组
 * @param dst
 * @param src
 * @return
 */
int hexStr2byte(char *dst, const char *src) {
    while (*src) {
        if (' ' == *src) {
            src++;
            continue;
        }
        sscanf(src, "%02X", dst);
        src += 2;
        dst++;
    }
    return 0;
}

/**
 * 字节流数组 -> 十六进制字符串
 * @param hexOut
 * @param byteIn
 * @param byteLen
 */
void byte2HexStr(char * hexOut, char * byteIn, int byteLen) {
    int i = 0;
    for (i= 0; i <byteLen * 2; i++) {
        if (i % 2 == 0) {
            hexOut[i] = (byteIn[i / 2] >> 4) & 0x0f;
        } else {
            hexOut[i] = byteIn[i / 2] & 0x0f;
        }

        if (hexOut[i] >= 0 && hexOut[i] <= 9) { // 0- 9
            hexOut[i] += '0';
        } else { //A-F
            hexOut[i] += 'a' - 10;
        }
    }
}

/**
 * 二进制数组 -> 十六进制字符串
 * @param hexOut
 * @param bitIn
 * @param bitLen
 */
void bit2HexStr(char *hexOut, int *bitIn, int bitLen) {
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
}

/**
 * 十六进制字符串 -> 二进制数组
 * @param bitOut
 * @param hexOut
 * @param hexLen
 */
void hexStr2Bit(int *bitOut, const char *hexIn, int hexLen) {
    int i = 0;
    for (i = 0; i < hexLen * 4; i++) {
        bitOut[i] = 0;
    }
    for (i = 0; i < hexLen * 4; i++) {
        if (hexIn[i / 4] >= 'a' && hexIn[i / 4] <= 'f') { //a-f
            bitOut[i] = ((hexIn[i / 4] - 'a' + 10) >> (3 - (i % 4))) & 0x01;
        } else if (hexIn[i / 4] >= 'A' && hexIn[i / 4] <= 'F') { //A-F
            bitOut[i] = ((hexIn[i / 4] - 'A' + 10) >> (3 - (i % 4))) & 0x01;
        } else { //0-9
            bitOut[i] = ((hexIn[i / 4] - '0') >> (3 - (i % 4))) & 0x01;
        }
    }
}