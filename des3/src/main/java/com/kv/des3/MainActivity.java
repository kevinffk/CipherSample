package com.kv.des3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tv1 = findViewById(R.id.tv1);
        String inHexStr = "0102030405060708";
        String keyHexStr = "1111111111111111";
        byte [] inArray = MyUtils.hexStringToBytes(inHexStr);
        byte [] keyArray = MyUtils.hexStringToBytes(keyHexStr);
        try {
            byte [] outArray = MyUtils.encrypt(inArray, keyArray);
            String outStr = MyUtils.bytesToHexString(outArray);
            Log.e("xx", "java out=" + outStr);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String cOutHexStr = Des3NativeLib.getEncodeDesHexStr("0102030405060708", "1111111111111111");
        Log.e("xx", "c out=" + cOutHexStr);

        String cMsgHexStr = Des3NativeLib.getDecodeDesHexStr("0102030405060708", "1111111111111111");
        Log.e("xx", "d out=" + cMsgHexStr);

        inHexStr = "0102030405060708";
        keyHexStr = "1111111111111111";
        inArray = MyUtils.hexStringToBytes(inHexStr);
        keyArray = MyUtils.hexStringToBytes(keyHexStr);
        try {
            byte [] outArray = MyUtils.decrypt(inArray, keyArray);
            String outStr = MyUtils.bytesToHexString(outArray);
            Log.e("xx", "java d out=" + outStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
