package com.zzt.zt_textviewaction;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author: zeting
 * @date: 2022/8/1
 */
public class ListAct extends AppCompatActivity {
    RecyclerView llllllll;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_aaa);
        llllllll = findViewById(R.id.llllllll);

        AdapterRv adapterRv = new AdapterRv();

        llllllll.setLayoutManager(new LinearLayoutManager(ListAct.this));
        llllllll.setAdapter(adapterRv);
    }
}
