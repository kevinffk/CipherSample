package com.kv.des3;

/**
 * Author:  Kevin Feng
 * Email:   597415099@qq.com
 * Date:    2018-01-03 22:25
 * Description:
 */
public class Des3NativeLib {

    static {
        System.loadLibrary("des3_native_lib");
    }

    public static native String getEncodeDesHexStr(String msgHex, String keyHex);

    public static native String getDecodeDesHexStr(String codeHex, String keyHex);
}
