package com.zzt.zt_textviewaction.util;

import android.content.Context;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.zzt.zt_textviewaction.R;


/**
 * @author: zeting
 * @date: 2022/7/28
 * 文本选中操作
 */
public class CustomSelectionActionModeUtil implements ActionMode.Callback {
    private static final String TAG = CustomSelectionActionModeUtil.class.getSimpleName();

    private Context mContext ;

    public CustomSelectionActionModeUtil(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        Log.d(TAG, "==========onCreateActionMode==========");
//        MenuInflater menuInflater = actionMode.getMenuInflater();
//        menu.clear();
//        menuInflater.inflate(R.menu.selection_action_menu, menu);

//        RelativeLayout layout = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.open_im_message_select_action_layout, null);
//        actionMode.setCustomView(layout);
        return false;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        Log.d(TAG, "==========onPrepareActionMode==========");
        menu.clear();
        menu.add(Menu.NONE, 3, 3, "") .setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.add(Menu.NONE, 2, 2, "") .setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.add(Menu.NONE, 1, 1, "") .setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem item) {
        Log.d(TAG, "==========onActionItemClicked==========");
        //根据item的ID处理点击事件
//        switch (item.getItemId()) {
//            case R.id.open_im_copy:
//                Log.d(TAG, "==========拷贝==========");
//                actionMode.finish();//收起操作菜单
//                break;
//            case R.id.open_im_reply:
//                Log.d(TAG, "==========回复==========");
//                actionMode.finish();
//                break;
//        }

        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        Log.d(TAG, "==========onDestroyActionMode==========");

    }
}
