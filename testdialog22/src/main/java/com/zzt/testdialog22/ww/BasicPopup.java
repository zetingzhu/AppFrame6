package com.zzt.testdialog22.ww;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.CallSuper;
import androidx.annotation.StyleRes;


/**
 * 弹窗基类
 *
 * @param <V> 弹窗的内容视图类型
 * @author 李玉江[QQ:1023694760]
 * @since 2015/7/19
 */
public abstract class BasicPopup<V extends View> implements DialogInterface.OnKeyListener {
    public static final int MATCH_PARENT = ViewGroup.LayoutParams.MATCH_PARENT;
    public static final int WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT;
    protected Activity activity;
    protected int screenWidthPixels;
    protected int screenHeightPixels;
    private PopupDialog popupDialog;
    private int width = 0, height = 0;
    private boolean isFillScreen = false;
    private boolean isHalfScreen = false;
    private boolean isPrepared = false;
    private int gravity = Gravity.BOTTOM;//默认位于屏幕底部

    public BasicPopup(Activity activity) {
        this.activity = activity;
        DisplayMetrics displayMetrics = ScreenUtils.displayMetrics(activity);
        screenWidthPixels = displayMetrics.widthPixels;
        screenHeightPixels = displayMetrics.heightPixels;
        popupDialog = new PopupDialog(activity);
        popupDialog.setOnKeyListener(this);
    }

    /**
     * 创建弹窗的内容视图
     *
     * @return the view
     */
    protected abstract V makeContentView();

    /**
     * 弹出窗显示之前调用
     */
    private void onShowPrepare() {
        if (isPrepared) {
            return;
        }
//        popupDialog.getWindow().setGravity(gravity);
        V view = makeContentView();
        popupDialog.setContentView(view);// 设置弹出窗体的布局
//        LogUtils.verbose(this, "do something before popup show");
//        if (width == 0 && height == 0) {
//            //未明确指定宽高，优先考虑全屏再考虑半屏然后再考虑包裹内容
//            width = screenWidthPixels;
//            if (isFillScreen) {
//                height = MATCH_PARENT;
//            } else if (isHalfScreen) {
//                height = screenHeightPixels / 2;
//            } else {
//                height = WRAP_CONTENT;
//            }
//        } else if (width == 0) {
//            width = screenWidthPixels;
//        } else if (height == 0) {
//            height = WRAP_CONTENT;
//        }
////        popupDialog.setSize(width, height);
//        isPrepared = true;
    }


    @CallSuper
    public void show() {
        onShowPrepare();
        popupDialog.show();
        LogUtils.verbose(this, "popup show");
    }

    public void dismiss() {
        popupDialog.dismiss();
        LogUtils.verbose(this, "popup dismiss");
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public final boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            return onKeyDown(keyCode, event);
        }
        return false;
    }


}
