package com.example.project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SettingActivity extends AppCompatActivity {
    public final String PREFERENCE = "com.studio572.samplesharepreference";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        /*Intent intent = getIntent();
        int money = intent.getIntExtra("moneyData", 0);
        Log.e("data111", "money " + money);*/

        Button settingBtn = findViewById(R.id.settingBtn);

        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText limit = findViewById(R.id.limit);

                /** 금액 제한 데이터 저장 **/
                SharedPreferences pref = getSharedPreferences(PREFERENCE, Activity.MODE_PRIVATE);

                // SharedPreferences 의 데이터를 저장/편집 하기위해 Editor 변수를 선언한다.
                SharedPreferences.Editor editor = pref.edit();

                // key값에 value값을 저장한다.
                // String, boolean, int, float, long 값 모두 저장가능하다.
                editor.putString("limitPay", limit.getText().toString());

                // 메모리에 있는 데이터를 저장장치에 저장한다.
                editor.commit();
                String result = pref.getString("limitPay", "");
                //Log.e("data111", "settingRes " + result);

                TextView textView = findViewById(R.id.saveLimit);
                textView.setText(result);

                finish();
            }
        });

        /** 금액 제한 데이터 저장 **/
        SharedPreferences pref = getSharedPreferences(PREFERENCE, Activity.MODE_PRIVATE);

        String result = pref.getString("limitPay", "");
        TextView textView = findViewById(R.id.saveLimit);
        if(result == "") {
            result = "0";
        }
        Log.e("data1111", "settingRes " + result);
        textView.setText(result);

    }
}
