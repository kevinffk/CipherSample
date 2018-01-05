package com.kv.des3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tv1 = findViewById(R.id.tv1);
        byte[] bytes = {0x10,0x11,0x12,0x13, (byte) 0xff, (byte) 128};
        Des3NativeLib.testByte(bytes);
//        tv1.setText(Des3NativeLib.getEncodeDesHexStr("10111213ff80", "1111111111111111"));
        tv1.setText(Des3NativeLib.getEncodeDesHexStr("1111111111111111", "1111111111111111"));
//
//        TextView tv2 = findViewById(R.id.tv2);
//        tv2.setText(encryptDes("1111111111111111", "1111111111111111"));
//        byte[] coB = null;
//        try {
//            coB = encrypt(hexStringToBytes("1111111111111111"),hexStringToBytes("1111111111111111"));
//            tv2.setText(bytesToHexString(coB));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        Log.e("xx", "t1=" + encryptDes("1111111111111111", "1111111111111111"));
//        Log.e("xx", "t2=" + bytesToHexString(coB));

//        String inStr = "1111111111111111";
//        String keyStr = "1111111111111111";
//
//        byte [] inByte = hexStringToBytes(inStr);
//        byte [] keyByte = hexStringToBytes(keyStr);
//
//        Log.e("xx", " inde=" + inByte.length + " key=" + keyByte.length);
//
//        byte [] outByte = CryptionControl.getInstance().encryptoECB(inByte, keyByte);
//        Log.e("xx", "msg=" + bytesToHexString(outByte));
//
//        try {
//            byte [] outByte2 = encrypt(inByte, keyByte);
//            Log.e("xx", "msg2=" + bytesToHexString(outByte2));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


    }


    public static byte[] encrypt(byte[] src, byte[] key) throws Exception {
        // DES算法要求有一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        // 从原始密匙数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);
        // 创建一个密匙工厂，然后用它把DESKeySpec转换成一个SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey securekey = keyFactory.generateSecret(dks);
        // 用密匙初始化Cipher对象 DES/ECB/NoPadding
        Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
//        // 用密匙初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);

        // 正式执行加密操作
        return cipher.doFinal(src);
    }

    public static String byte2String(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1)
                hs = hs + "0" + stmp;
            else
                hs = hs + stmp;
        }
        return hs.toUpperCase();
    }



    public static String encryptDes(String inHexStr, String pwdHexStr) {
        byte[] intByte = hexStringToBytes(inHexStr);
        byte[] pwdByte = hexStringToBytes(pwdHexStr);

        byte [] outByte= encrypt(intByte, new String(pwdByte).toString());
        return bytesToHexString(outByte);
    }

    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));

        }
        return d;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /**
     * Convert byte[] to hex string
     *
     * @param src byte[] data
     * @return hex string
     */
    public static String bytesToHexString(byte[] src){
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * 加密
     *
     * @param datasource byte[]
     * @param password   String
     * @return byte[]
     */
    public static byte[] encrypt(byte[] datasource, String password) {
        try {
            SecureRandom random = new SecureRandom();
            DESKeySpec desKey = new DESKeySpec(password.getBytes());
            //创建一个密匙工厂，然后用它把DESKeySpec转换成
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(desKey);
            //Cipher对象实际完成加密操作
            Cipher cipher = Cipher.getInstance("DES");
            //用密匙初始化Cipher对象
            cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
            //现在，获取数据并加密
            //正式执行加密操作
            return cipher.doFinal(datasource);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密
     *
     * @param src      byte[]
     * @param password String
     * @return byte[]
     * @throws Exception
     */
    public static byte[] decrypt(byte[] src, String password) throws Exception {
        // DES算法要求有一个可信任的随机数源
        SecureRandom random = new SecureRandom();
        // 创建一个DESKeySpec对象
        DESKeySpec desKey = new DESKeySpec(password.getBytes());
        // 创建一个密匙工厂
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        // 将DESKeySpec对象转换成SecretKey对象
        SecretKey securekey = keyFactory.generateSecret(desKey);
        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance("DES");
        // 用密匙初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey, random);
        // 真正开始解密操作
        return cipher.doFinal(src);
    }
}
