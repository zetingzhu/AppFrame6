package com.zzt.dialogutilcode.zdialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.zzt.dialogutilcode.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: zeting
 * @date: 2022/11/8
 */
public class ZDialogUtil {
    public static class MessageDialogBuilder extends ZDialogBuilder<ZDialogUtil.MessageDialogBuilder> {
        // 左右间距
        private int lpLeftMargin;
        private int lpRightMargin;
        // 是否显示删除按钮
        private boolean hasRightDel = false;

        // 所有内容的信息
        private List<ZDialogTextView> mTextViews = new ArrayList<>();

        public MessageDialogBuilder(Context context) {
            super(context, R.style.Style_Base_Dialog);
            initView();
        }

        public MessageDialogBuilder(Context context, int style) {
            super(context, style);
            initView();
        }

        private void initView() {
            setActionContainerOrientation(ZDialogBuilder.VERTICAL)
                    .setActionSpace(dp2px(getBaseContext(), 8))
                    .setNightMode(true);
            lpLeftMargin = dp2px(getBaseContext(), 16);
            lpRightMargin = dp2px(getBaseContext(), 16);
        }

        /**
         * 设置顶部图片
         */
        public ZDialogUtil.MessageDialogBuilder setTopImage(@DrawableRes int iconRes) {
            setTopImage(new ZDialogImageView(iconRes)
                    .setLpHeight(dp2px(getBaseContext(), 80))
                    .setLpWidth(dp2px(getBaseContext(), 80))
            );
            return this;
        }

        /**
         * 设置顶部图片
         */
        public ZDialogUtil.MessageDialogBuilder setTopImage(Drawable iconRes) {
            setTopImage(new ZDialogImageView(0)
                    .setIconDrawable(iconRes)
                    .setLpHeight(dp2px(getBaseContext(), 80))
                    .setLpWidth(dp2px(getBaseContext(), 80))
            );
            return this;
        }

        /**
         * 添加对话框底部的操作按钮
         */
        public ZDialogUtil.MessageDialogBuilder addTextView(@Nullable ZDialogTextView dialogTV) {
            if (dialogTV != null) {
                mTextViews.add(dialogTV);
            }
            return this;
        }

        /**
         * 设置右侧删除图片
         */
        public ZDialogUtil.MessageDialogBuilder setRightDel(ZDialogImageView.DialogImageViewListener listener) {
            return setRightDel(true, 0, listener);
        }


        /**
         * 设置上面蓝色按钮
         */
        public ZDialogUtil.MessageDialogBuilder addActionTopBlue(@Nullable ZDialogAction action) {
            if (action != null) {
                action.setTextSize(16)
                        .setTextColor(ContextCompat.getColor(getBaseContext(), R.color.white))
                        .setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.round_3d56ff_3dp))
                        .setBtnHeight(dp2px(getBaseContext(), 40))
                        .setLpMargin(lpLeftMargin, 0, lpRightMargin, 0);
                addAction(action);
            }
            return this;
        }

        /**
         * 设置下面灰色按钮
         */
        public ZDialogUtil.MessageDialogBuilder addActionBottomGray(@Nullable ZDialogAction action) {
            if (action != null) {
                action.setTextSize(16)
                        .setTextColor(ContextCompat.getColor(getBaseContext(), R.color.color_999999))
                        .setBackground(null)
                        .setBtnHeight(dp2px(getBaseContext(), 40))
                        .setLpMargin(lpLeftMargin, 0, lpRightMargin, dp2px(getBaseContext(), 8));
                addAction(action);
            }
            return this;
        }


        /**
         * 设置右侧删除图片
         */
        public ZDialogUtil.MessageDialogBuilder setRightDel(boolean show, @DrawableRes int iconRes, ZDialogImageView.DialogImageViewListener listener) {
            this.hasRightDel = show;
            if (show) {
                if (iconRes == 0) {
                    setRightDelete(new ZDialogImageView(R.drawable.ic_close)
                            .setLpHeight(dp2px(getBaseContext(), 24))
                            .setLpWidth(dp2px(getBaseContext(), 24))
                            .setLpMargin(0, dp2px(getBaseContext(), 12),
                                    dp2px(getBaseContext(), 12), 0)
                            .onClick(listener)
                    );
                } else {
                    setRightDelete(new ZDialogImageView(iconRes)
                            .setLpHeight(dp2px(getBaseContext(), 24))
                            .setLpWidth(dp2px(getBaseContext(), 24))
                            .setLpMargin(0, dp2px(getBaseContext(), 12),
                                    dp2px(getBaseContext(), 12), 0)
                            .onClick(listener)
                    );
                }
            } else {
                setRightDelete(null);
            }
            return this;
        }


        public ZDialogUtil.MessageDialogBuilder setBottomDel(ZDialogImageView.DialogImageViewListener listener) {
            return setBottomDel(true, 0, listener);
        }

        /**
         * 设置底部删除图片
         */
        public ZDialogUtil.MessageDialogBuilder setBottomDel(boolean show, @DrawableRes int iconRes, ZDialogImageView.DialogImageViewListener listener) {
            if (show) {
                if (iconRes == 0) {
                    setBottomDelete(new ZDialogImageView(R.drawable.ic_close)
                            .setLpHeight(dp2px(getBaseContext(), 24))
                            .setLpWidth(dp2px(getBaseContext(), 24))
                            .setLpMargin(0, dp2px(getBaseContext(), 40), 0, 0)
                            .onClick(listener)
                    );
                } else {
                    setBottomDelete(new ZDialogImageView(iconRes)
                            .setLpHeight(dp2px(getBaseContext(), 24))
                            .setLpWidth(dp2px(getBaseContext(), 24))
                            .setLpMargin(0, dp2px(getBaseContext(), 40), 0, 0)
                            .onClick(listener)
                    );
                }
            } else {
                setBottomDelete(null);
            }
            return this;
        }

        /**
         * 设置对话框的消息文本
         */
        public ZDialogUtil.MessageDialogBuilder setMessage(CharSequence message) {
            if (!TextUtils.isEmpty(message)) {
                mTextViews.add(new ZDialogTextView(message)
                        .setTextColor(Color.parseColor("#252C58"))
                        .setTextSize(18)
                        .setLpMargin(lpLeftMargin, dp2px(getBaseContext(), 32), lpRightMargin, dp2px(getBaseContext(), 40))
                        .setGravity(Gravity.CENTER)
                );
            }
            return this;
        }

        /**
         * 设置对话框的消息文本
         */
        public ZDialogUtil.MessageDialogBuilder setTitleMessage(CharSequence title, CharSequence message) {
            if (!TextUtils.isEmpty(title)) {
                mTextViews.add(new ZDialogTextView(title)
                        .setTextColor(Color.parseColor("#252C58"))
                        .setTextSize(18)
                        .setLpMargin(lpLeftMargin, dp2px(getBaseContext(), 32), lpRightMargin, 0)
                        .setGravity(Gravity.CENTER)
                        .setTypefaceStyle(Typeface.BOLD)
                );
            }
            if (!TextUtils.isEmpty(message)) {
                mTextViews.add(new ZDialogTextView(message)
                        .setTextColor(Color.parseColor("#252C58"))
                        .setTextSize(16)
                        .setLpMargin(lpLeftMargin, dp2px(getBaseContext(), 16), lpRightMargin, dp2px(getBaseContext(), 24))
                        .setGravity(Gravity.CENTER)
                );
            }
            return this;
        }


        @Nullable
        @Override
        protected View onCreateContent(@NonNull BaseShowDismissAppCompatDialog dialog, @NonNull ConstraintLayout parent, @NonNull Context context) {
            int size = mTextViews.size();
            if (size > 0) {
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);
                for (int i = 0; i < size; i++) {
                    ZDialogTextView mdtv = mTextViews.get(i);
                    LinearLayout.LayoutParams mTVLp = mdtv.buildLayoutParams();
                    if (i == 0 && hasRightDel) {
                        mTVLp.topMargin = dp2px(getBaseContext(), 12);
                    }
                    AppCompatTextView textView = mdtv.buildTextView(mDialog, i);
                    layout.addView(textView, mTVLp);
                }
                return layout;
            }
            return null;
        }
    }
}
