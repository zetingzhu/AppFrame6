package com.zzt.zt_textviewaction;

import android.content.Context;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.zzt.zt_textviewaction.popup.ZPopOpenIMImageBubble;


/**
 * @author: zeting
 * @date: 2022/7/28
 * 文本选中操作
 */
public class CustomSelectionActionModeUtil implements ActionMode.Callback {
    private static final String TAG = "Custom TextHelper";

    private Context mContext;
    private TextView textContent;
    private ActionMode actionMode;
    private ZPopOpenIMImageBubble zPopOpenIMImageBubble;

    public CustomSelectionActionModeUtil(TextView textView) {
        this.textContent = textView;
        this.mContext = textView.getContext();
    }

    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        Log.d(TAG, "==========onCreateActionMode==========");
        this.actionMode = actionMode;
//        if (zPopOpenIMImageBubble != null) {
//            zPopOpenIMImageBubble.dismiss();
//        }

        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        Log.d(TAG, "==========onPrepareActionMode==========");
        menu.clear();
        menu.add(Menu.NONE, 3, 3, "3333").setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.add(Menu.NONE, 2, 2, "2222").setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.add(Menu.NONE, 1, 1, "1111").setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);

        actionMode = mode;

        View customView = mode.getCustomView();
        Log.d(TAG, ">>1>" + customView);

        MenuInflater menuInflater = mode.getMenuInflater();
        Log.d(TAG, ">>2>" + menuInflater);
//        zPopOpenIMImageBubble = new ZPopOpenIMImageBubble(mContext, ZDirection.DIRECTION_TOP, new SelectActionListener() {
//            @Override
//            public void onTextSelected(CharSequence content) {
//                actionMode.finish();
//            }
//
//            @Override
//            public void onTextSelectedCopy(CharSequence content) {
//                actionMode.finish();
//            }
//
//            @Override
//            public void onTextSelectedReply(CharSequence content) {
//                actionMode.finish();
//            }
//
//            @Override
//            public void onTextSelectedDownload(CharSequence content) {
//
//            }
//        });
//        zPopOpenIMImageBubble.show(textContent);
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem item) {
        Log.d(TAG, "==========onActionItemClicked==========");
        actionMode.finish();
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        Log.d(TAG, "==========onDestroyActionMode==========");
        if (textContent != null) {
            int min = 0;
            int max = textContent.length();
            if (textContent.isFocused()) {
                final int selStart = textContent.getSelectionStart();
                final int selEnd = textContent.getSelectionEnd();

                min = Math.max(0, Math.min(selStart, selEnd));
                max = Math.max(0, Math.max(selStart, selEnd));
            }
            String content = String.valueOf(textContent.getText().subSequence(min, max));
            Log.d(TAG, ">>>" + content);
        }
    }
}
