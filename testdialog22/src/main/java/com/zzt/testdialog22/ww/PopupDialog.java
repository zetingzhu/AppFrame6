package com.zzt.testdialog22.ww;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.CallSuper;
import androidx.annotation.StyleRes;

import com.zzt.testdialog22.R;


/**
 * 弹窗，内部类，仅供{@link BasicPopup}调用
 *
 * @author 李玉江[QQ :1023694760]
 * @see android.widget.PopupWindow
 * @since 2015-10-19
 */
class PopupDialog {
    private Dialog dialog;
//    private FrameLayout contentLayout;

    PopupDialog(Context context) {
        init(context);
    }

    private void init(Context context) {
//        contentLayout = new FrameLayout(context);
//        contentLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//        contentLayout.setFocusable(true);
//        contentLayout.setFocusableInTouchMode(true);

        dialog = new Dialog(context);
        dialog.setCanceledOnTouchOutside(true);//触摸屏幕取消窗体
        dialog.setCancelable(true);//按返回键取消窗体
        Window window = dialog.getWindow();
        if (window != null) {
//            window.setWindowAnimations(R.style.Animation_Popup);
//            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//            //android.util.AndroidRuntimeException: requestFeature() must be called before adding content
//            window.requestFeature(Window.FEATURE_NO_TITLE);
//            window.setContentView(contentLayout);
//            window.getAttributes().width = WindowManager.LayoutParams.MATCH_PARENT;
        }
    }



    @CallSuper
    void show() {
        dialog.show();
    }

    @CallSuper
    void dismiss() {
        dialog.dismiss();
    }

    void setContentView(View view) {
//        contentLayout.removeAllViews();
//        contentLayout.addView(view);
        dialog.setContentView(view);
    }


//    void setSize(int width, int height) {
//        ViewGroup.LayoutParams params = contentLayout.getLayoutParams();
//        if (params == null) {
//            params = new ViewGroup.LayoutParams(width, height);
//        } else {
//            params.width = width;
//            params.height = height;
//        }
//        contentLayout.setLayoutParams(params);
//    }

    void setOnKeyListener(DialogInterface.OnKeyListener onKeyListener) {
        dialog.setOnKeyListener(onKeyListener);
    }

    Window getWindow() {
        return dialog.getWindow();
    }

}
