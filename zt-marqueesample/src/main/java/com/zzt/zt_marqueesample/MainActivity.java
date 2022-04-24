package com.zzt.zt_marqueesample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private String textLang = "开始，这是一个长文本 ， 这是一个长文本 ， 这是一个长文本 ， 这是一个长文本 ，结束";
    private TextView tv_m1, tv_m2;
    private  AutoScrollTextView tv_m3 ;
    private  MarqueeHorizontalTextView tv_m4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        tv_m1 = findViewById(R.id.tv_m1);
        tv_m2 = findViewById(R.id.tv_m2);
        tv_m3 = findViewById(R.id.tv_m3);
        tv_m4 = findViewById(R.id.tv_m4);

        tv_m1.setText(textLang);
        tv_m1.setSelected(true);
        tv_m2.setText(textLang);
        tv_m3.setText(textLang);
        tv_m3.init(getWindowManager());
        tv_m3.startScroll();
        tv_m4.setText(textLang , TextView.BufferType.NORMAL);
    }
}