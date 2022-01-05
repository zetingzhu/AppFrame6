package com.zzt.rfar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SettingTestAct extends AppCompatActivity {

    Button button;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_test);

        initView();
    }

    private void initView() {

        button = findViewById(R.id.button);
        textView = findViewById(R.id.textView);

        String stringExtra = getIntent().getStringExtra("ddd");
        textView.setText(stringExtra);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("EEE", "这个是返回的值");
                setResult(999, intent);
                finish();
            }
        });

    }
}