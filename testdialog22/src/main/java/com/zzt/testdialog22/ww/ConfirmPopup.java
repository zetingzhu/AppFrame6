package com.zzt.testdialog22.ww;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.zzt.testdialog22.R;


/**
 * 带确定及取消按钮的弹窗
 *
 * @param <V> the type parameter
 * @author 李玉江[QQ:1032694760]
 * @since 2015/10/21
 */
public abstract class ConfirmPopup<V extends View> extends BasicPopup<View> {
    protected boolean topLineVisible = true;
    protected int topLineColor = 0xFFDDDDDD;
    protected int topBackgroundColor = Color.WHITE;
    protected int topHeight = 40;//dp
    protected boolean cancelVisible = true;
    protected CharSequence cancelText = "";
    protected CharSequence submitText = "";
    protected CharSequence titleText = "";
    protected int cancelTextColor = Color.BLACK;
    protected int submitTextColor = Color.BLACK;
    protected int titleTextColor = Color.BLACK;
    protected int cancelTextSize = 0;
    protected int submitTextSize = 0;
    protected int titleTextSize = 0;
    protected int backgroundColor = Color.WHITE;
    protected TextView titleView;

    public ConfirmPopup(Activity activity) {
        super(activity);
        cancelText = activity.getString(R.string.s16_17);
        submitText = activity.getString(R.string.s16_18);
    }

    /**
     * @see #makeHeaderView()
     * @see #makeCenterView()
     * @see #makeFooterView()
     */
    @Override
    protected final View makeContentView() {
//        LinearLayout rootLayout = new LinearLayout(activity);
//        rootLayout.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
//        rootLayout.setOrientation(LinearLayout.VERTICAL);
//        rootLayout.setGravity(Gravity.CENTER);
//        rootLayout.setPadding(0, 0, 0, 0);
//        rootLayout.setClipToPadding(false);
//        View headerView = makeHeaderView();

        // 添加顶部
//        View topView = makeTopView();
//        rootLayout.addView(topView);


        // 日期选择
        View conterView = makeCenterView();
//        conterView.setBackgroundResource(R.color.white);
//        rootLayout.addView(conterView, new LinearLayout.LayoutParams(MATCH_PARENT, 0, 1.0f));
//        rootLayout.addView(conterView, new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));

//        // 底部布局
//        View footerView = makeFooterView();
//        if (footerView != null) {
//            rootLayout.addView(footerView);
//        }

//        // 确定取消按钮
//        if (headerView != null) {
//            rootLayout.addView(headerView);
//        }
        return conterView;
    }

    /**
     * 添加顶部布局
     *
     * @return
     */
    @Nullable
    protected View makeTopView() {
        LinearLayout topLayout = new LinearLayout(activity);
        topLayout.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        topLayout.setGravity(Gravity.RIGHT);
//        topLayout.setBackgroundResource(R.drawable.white_round_3dp_top);

        View topView = new View(activity);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(activity.getResources().getDimensionPixelOffset(R.dimen.margin_24dp), activity.getResources().getDimensionPixelOffset(R.dimen.margin_24dp));
        layoutParams.topMargin = activity.getResources().getDimensionPixelOffset(R.dimen.margin_12dp);
        layoutParams.rightMargin = activity.getResources().getDimensionPixelOffset(R.dimen.margin_12dp);
        topView.setLayoutParams(layoutParams);
//        topView.setBackgroundResource(R.drawable.ic_close_profile);
        topLayout.addView(topView);

        topLayout.setOnClickListener(v -> dismiss());
        return topLayout;
    }

    @Nullable
    protected View makeHeaderView() {
        RelativeLayout topButtonLayout = new RelativeLayout(activity);
        topButtonLayout.setLayoutParams(new RelativeLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        topButtonLayout.setBackgroundColor(topBackgroundColor);
        topButtonLayout.setGravity(Gravity.CENTER_VERTICAL);

        titleView = new TextView(activity);
        titleView.setId(R.id.data_picker_bottom_center);
        titleView.setBackgroundColor(activity.getResources().getColor(R.color.color_3D56FF));
        RelativeLayout.LayoutParams titleLayoutParams = new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        int margin = 4;
        titleLayoutParams.leftMargin = margin;
        titleLayoutParams.rightMargin = margin;
        titleLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        titleLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        titleView.setLayoutParams(titleLayoutParams);
        titleView.setGravity(Gravity.CENTER);
        if (!TextUtils.isEmpty(titleText)) {
            titleView.setText(titleText);
        }
        titleView.setTextColor(titleTextColor);
        if (titleTextSize != 0) {
            titleView.setTextSize(titleTextSize);
        }
        topButtonLayout.addView(titleView);

        Button cancelButton = new Button(activity);
        cancelButton.setVisibility(cancelVisible ? View.VISIBLE : View.GONE);
        RelativeLayout.LayoutParams cancelButtonLayoutParams = new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        cancelButtonLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        cancelButtonLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        cancelButtonLayoutParams.addRule(RelativeLayout.LEFT_OF, titleView.getId());
        cancelButtonLayoutParams.leftMargin = 20;
        cancelButtonLayoutParams.bottomMargin = 80;
        cancelButton.setLayoutParams(cancelButtonLayoutParams);
//        cancelButton.setBackgroundColor(Color.TRANSPARENT);

        cancelButton.setGravity(Gravity.CENTER);
        if (!TextUtils.isEmpty(cancelText)) {
            cancelButton.setText(cancelText);
        }
        cancelButton.setTextColor(cancelTextColor);
        if (cancelTextSize != 0) {
            cancelButton.setTextSize(cancelTextSize);
        }
        cancelButton.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                onCancel();
            }
        });
        //  去掉阴影
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cancelButton.setStateListAnimator(null);
        }
        topButtonLayout.addView(cancelButton);


        Button submitButton = new Button(activity);
        RelativeLayout.LayoutParams submitButtonLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        submitButtonLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        submitButtonLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        submitButtonLayoutParams.addRule(RelativeLayout.RIGHT_OF, titleView.getId());
        submitButtonLayoutParams.rightMargin = 20;
        submitButtonLayoutParams.bottomMargin = 80;
        submitButton.setLayoutParams(submitButtonLayoutParams);
        submitButton.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        submitButton.setGravity(Gravity.CENTER);
        if (!TextUtils.isEmpty(submitText)) {
            submitButton.setText(submitText);
        }
        submitButton.setTextColor(submitTextColor);
        if (submitTextSize != 0) {
            submitButton.setTextSize(submitTextSize);
        }
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                onSubmit();
            }
        });
        //  去掉阴影
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            submitButton.setStateListAnimator(null);
        }
        topButtonLayout.addView(submitButton);

        return topButtonLayout;
    }

    @NonNull
    protected abstract V makeCenterView();

    @Nullable
    protected View makeFooterView() {
        return null;
    }

    protected void onSubmit() {

    }

    protected void onCancel() {

    }

}
