package com.zzt.zt_textviewaction;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.zzt.zt_textviewaction.selectv3.ActionMenu;
import com.zzt.zt_textviewaction.selectv3.CustomActionMenuCallBack;
import com.zzt.zt_textviewaction.selectv3.SelectableTextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CustomActionMenuCallBack {
    private TextView tv_open_message;
    private Button button;
    private SelectableTextView selectableTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_open_message = findViewById(R.id.tv_open_message);

        tv_open_message.setCustomSelectionActionModeCallback(new CustomSelectionActionModeUtil(tv_open_message));

        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), ListAct.class));
            }
        });
        initView();
    }

    private void initView() {
        String str_en = "renxu autumn July, Jiwang, Suzi and guest boating tours under Chibi. Xu breeze, rippleless. Wine is off, chanting the moon poetry, song slim chapter. A little while, months of Dongshan, wandering in the bullfight. Dew Yokoe, Shuiguang next day. Even a reed like, Ling Wan Qing loss. The vast Feng seems such as virtual Yufeng, and not to stop; waving as aloof, emergence and immortal. (Feng Tong) </p> <p>    and drinking music, I and song. The Song said: \"Zhao Gui Xi Lan paddle upstream come blow out light and air. You come to my arms, at beauty come one day.\" A blowing flute, and the Yi song. The hum of course, such as resentment, such as mu of bamboo, linger on faintly, as if weeping and complaining. Dance Youhe the cry of a hidden dragon boat. </p> <p> Suzi stern, sat, and asked the guests said: \"what is it?\" The guests said: \"yuemingxingxi, Ukraine magpie flying south.\" This is not a poem of Cao Mengde? Seomang Xiakou, looking east Wuchang mountains, Miao Yu, between green and the non shuro Meng moral trapped in between? The broken Jingzhou, Jiangling, East River, a convoy of ships thousands of miles, empty word flags, pour wine, having a lance sideward and poetizing Linjiang, a hero of the age and where is also solid,? Kuangwu and son, in Jiang Nagisa, Lu fish and shrimp and the friends of the elk, riding a leaf boat, belonging to lift bottle gourd. Send ephemera in the world, a boundless sea. Sad moment of my life, the infinite envy of Yangtze river. With the fly to roam, hold the moon and long end. Don't know will suddenly left, supporting ring in Beifeng. ";
        selectableTextView = (SelectableTextView) findViewById(R.id.ctv_content);
//        selectableTextView.setText(Html.fromHtml(str_en).toString());
        selectableTextView.setText(str_en);
        selectableTextView.clearFocus();
//        selectableTextView.setTextJustify(false);
//        selectableTextView.setForbiddenActionMenu(false);
        selectableTextView.setCustomActionMenuCallBack(this);
//        selectableTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "SelectableTextView 的onClick事件", Toast.LENGTH_SHORT).show();
//            }
//        });

    }

    @Nullable
    @Override
    public ActionMode onWindowStartingActionMode(ActionMode.Callback callback) {
        ActionBar actionBar = getActionBar();
        ActionMode actionMode = super.onWindowStartingActionMode(callback);
        return actionMode;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        return super.onCreateOptionsMenu(menu);
        return false;
    }


    @Override
    public boolean onCreateCustomActionMenu(ActionMenu menu) {
        menu.setActionMenuBgColor(0xff666666);  // ActionMenu背景色
        menu.setMenuItemTextColor(0xffffffff);  // ActionMenu文字颜色
        List<String> titleList = new ArrayList<>();
        titleList.add("翻译");
        titleList.add("分享");
        titleList.add("分享");
        menu.addCustomMenuItem(titleList);  // 添加菜单
        return false; // 返回false，保留默认菜单(全选/复制)；返回true，移除默认菜单
    }

    @Override
    public void onCustomActionItemClicked(String itemTitle, String selectedContent) {
        Toast.makeText(MainActivity.this, "ActionMenu: " + itemTitle, Toast.LENGTH_SHORT).show();
    }
}