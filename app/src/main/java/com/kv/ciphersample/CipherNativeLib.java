package com.kv.ciphersample;

/**
 * Author:  Kevin Feng
 * Email:   597415099@qq.com
 * Date:    2018-01-02 17:23
 * Description:
 */
public class CipherNativeLib {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("cipher_native_lib");
    }


    public static native String stringFromJNI();

    public static native String cryptMd5(String source);
}
