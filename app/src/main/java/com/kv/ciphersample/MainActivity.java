package com.kv.ciphersample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv = (TextView) findViewById(R.id.sample_text);
//        tv.setText(CipherNativeLib.cryptMd5By32("admin", true)
//                + "\n"
//                + CipherNativeLib.cryptMd5By32("admin", false)
//                + "\n"
//                + CipherNativeLib.cryptMd5By16("admin", true)
//                + "\n"
//                + CipherNativeLib.cryptMd5By16("admin", false));

        startService(new Intent(getApplicationContext(), SystemService.class));
    }


}
