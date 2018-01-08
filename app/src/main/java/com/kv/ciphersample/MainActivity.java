package com.kv.ciphersample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv = (TextView) findViewById(R.id.sample_text);

        String cOutHexStr = CipherNativeLib.getEncodeDesHexStr("0102030405060708", "1111111111111111");
        Log.e("xx", "c out=" + cOutHexStr);

        String cMsgHexStr = CipherNativeLib.getDecodeDesHexStr("0102030405060708", "1111111111111111");
        Log.e("xx", "d out=" + cMsgHexStr);
    }


}
