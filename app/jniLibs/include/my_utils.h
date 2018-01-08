#include <stdio.h>

int hexStr2byte(char *dst, const char *src);

void bit2HexStr(char *hexOut, int *bitIn, int bitLen);

void hexStr2Bit(int *bitOut, const char *hexIn, int hexLen);

void byte2HexStr(char * hexOut, char * byteIn, int byteLen);