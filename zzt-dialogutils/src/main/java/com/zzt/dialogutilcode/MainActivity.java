package com.zzt.dialogutilcode;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.zzt.adapter.StartActivityRecyclerAdapter;
import com.zzt.dialogutilcode.widget.dialog.QMUIDialog;
import com.zzt.dialogutilcode.widget.dialog.QMUIDialogAction;
import com.zzt.entity.StartActivityDao;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    RecyclerView rv_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        LeftFixedEditText lfet_text = findViewById(R.id.lfet_text);
        lfet_text.setFixedText("$");

        rv_list = findViewById(R.id.rv_list);

        List<StartActivityDao> mListDialog = new ArrayList<>();
        mListDialog.add(new StartActivityDao("列表对话框", "", "1"));
        mListDialog.add(new StartActivityDao("确定取消对话框", "", "2"));

        StartActivityRecyclerAdapter.setAdapterData(rv_list, RecyclerView.VERTICAL, mListDialog, (itemView, position, data) -> {
            switch (data.getArouter()) {
                case "1":
//                    final String[] items = new String[]{"蓝色（默认）", "黑色", "白色"};
//                    new QMUIDialog.MenuDialogBuilder(MainActivity.this)
//                            .addItems(items, new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    log("点击了 " + which);
//                                    dialog.dismiss();
//                                }
//                            })
//                            .create()
//                            .show();
                    break;
                case "2":
                    new QMUIDialog.MessageDialogBuilder(MainActivity.this)
                            .setTitle("这个是标题")
                            .setMessage("确认下载此文件？")
                            .addAction("去掉", new QMUIDialogAction.ActionListener() {
                                @Override
                                public void onClick(QMUIDialog dialog, int index) {
                                    dialog.dismiss();
                                    log("点击了 " + index);
                                }
                            })
                            .addAction("确定", new QMUIDialogAction.ActionListener() {
                                @Override
                                public void onClick(QMUIDialog dialog, int index) {
                                    dialog.dismiss();
                                    log("点击了 " + index);
                                }
                            })
                            .show();
                    break;
            }
        });
    }

    public void log(String str) {
        Log.d(TAG, ">>>>>" + str);
        Toast.makeText(MainActivity.this, str, Toast.LENGTH_LONG).show();
    }
}