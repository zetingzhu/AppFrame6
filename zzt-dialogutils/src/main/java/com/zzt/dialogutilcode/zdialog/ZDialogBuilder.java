package com.zzt.dialogutilcode.zdialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Space;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.zzt.dialogutilcode.R;
import com.zzt.dialogutilcode.util.QMUIDrawableHelper;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

public abstract class ZDialogBuilder<T extends ZDialogBuilder> {
    private static final String TAG = ZDialogBuilder.class.getSimpleName();

    @IntDef({HORIZONTAL, VERTICAL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Orientation {
    }

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;

    // 设置底部按钮横竖样式
    @Orientation
    private int mActionContainerOrientation = HORIZONTAL;

    private Context mContext;
    protected BaseShowDismissAppCompatDialog mDialog;
    private boolean mCancelable = true;
    private boolean mCanceledOnTouchOutside = true;

    // 底部操作按钮
    protected List<ZDialogAction> mActions = new ArrayList<>();
    // 底部按钮间距
    private int actionSpace = 50;
    // 顶部图片
    private ZDialogImageView topImage;
    // 删除按钮
    private ZDialogImageView rightDelete;
    // 底部删除按钮
    private ZDialogImageView bottomDelete;
    // dialog 样式
    private int mStyle;
    // 是否深色模式
    private boolean nightMode = false;

    public ZDialogBuilder(Context context) {
        this.mContext = context;
    }

    public ZDialogBuilder(Context context, int style) {
        this.mContext = context;
        this.mStyle = style;
    }

    public Context getBaseContext() {
        return mContext;
    }

    /**
     * 是否深色模式
     */
    @SuppressWarnings("unchecked")
    public T setNightMode(boolean night) {
        this.nightMode = night;
        return (T) this;
    }

    public boolean isNightMode() {
        return nightMode;
    }

    /**
     * 添加顶部大图片
     */
    @SuppressWarnings("unchecked")
    public T setTopImage(ZDialogImageView topImage) {
        this.topImage = topImage;
        return (T) this;
    }

    /**
     * 右上角删除按钮
     */
    @SuppressWarnings("unchecked")
    public T setRightDelete(ZDialogImageView rightDelete) {
        this.rightDelete = rightDelete;
        return (T) this;
    }

    /**
     * 添加底部删除图片
     */
    @SuppressWarnings("unchecked")
    public T setBottomDelete(ZDialogImageView bottomDelete) {
        this.bottomDelete = bottomDelete;
        return (T) this;
    }

    /**
     * 设置弹框 style
     */
    @SuppressWarnings("unchecked")
    public T setStyle(int mStyle) {
        this.mStyle = mStyle;
        return (T) this;
    }


    /**
     * 添加对话框是否可以取消
     */
    @SuppressWarnings("unchecked")
    public T setCancelable(boolean cancelable) {
        mCancelable = cancelable;
        return (T) this;
    }

    /**
     * 添加对话框外部是否可以点击
     */
    @SuppressWarnings("unchecked")
    public T setCanceledOnTouchOutside(boolean canceledOnTouchOutside) {
        mCanceledOnTouchOutside = canceledOnTouchOutside;
        return (T) this;
    }


    /**
     * 添加底部按钮排列方式
     */
    @SuppressWarnings("unchecked")
    public T setActionContainerOrientation(int actionContainerOrientation) {
        mActionContainerOrientation = actionContainerOrientation;
        return (T) this;
    }

    /**
     * 添加对话框底部的操作按钮
     */
    @SuppressWarnings("unchecked")
    public T addAction(@Nullable ZDialogAction action) {
        if (action != null) {
            mActions.add(action);
        }
        return (T) this;
    }

    /**
     * 设置底部操作按钮间距
     */
    @SuppressWarnings("unchecked")
    public T setActionSpace(int actionSpace) {
        this.actionSpace = actionSpace;
        return (T) this;
    }

    /**
     * 添加操作按钮
     *
     * @param str      文案
     * @param listener 点击回调事件
     */
    @SuppressWarnings("unchecked")
    public T addAction(CharSequence str, ZDialogAction.ActionListener listener) {
        ZDialogAction action = new ZDialogAction(str)
                .onClick(listener);
        mActions.add(action);
        return (T) this;
    }


    /**
     * 产生一个 Dialog 并显示出来
     */
    public BaseShowDismissAppCompatDialog show() {
        final BaseShowDismissAppCompatDialog dialog = create();
        dialog.show();
        return dialog;
    }

    /**
     * 只产生一个 Dialog, 不显示出来
     *
     * @see #create(int)
     */
    public BaseShowDismissAppCompatDialog create() {
        return create(mStyle);
    }

    /**
     * 产生一个Dialog，但不显示出来。
     *
     * @param style Dialog 的样式
     * @see #create()
     */
    @SuppressLint("InflateParams")
    public BaseShowDismissAppCompatDialog create(@StyleRes int style) {
        mDialog = new BaseShowDismissAppCompatDialog(mContext, style);
        Context dialogContext = mDialog.getContext();
        // 最外面不带背景视图，用来放顶部头像和外部删除按钮
        ConstraintLayout mDialogRootView = onCreateDialogRootView(dialogContext);
        // 带背景dialog 内容视图
        ConstraintLayout mDialogParentView = onCreateDialogContentView(dialogContext);
        // 顶部图片
        AppCompatImageView topImg = onCreateDialogTopView(mDialog, topImage);
        // 右上角删除
        View rightDeleteView = onCreateDeleteBtn(mDialog, rightDelete);
        // 底部删除删除
        View bottomDeleteView = onCreateDeleteBtn(mDialog, bottomDelete);
        // 下方操作按钮
        View operatorLayout = onCreateOperatorLayout(mDialog, mDialogParentView, dialogContext);
        // 添加自定义内容信息
        View contentLayout = onCreateContent(mDialog, mDialogParentView, dialogContext);

        checkAndSetId(mDialogRootView, R.id.z_dialog_root_layout);
        checkAndSetId(mDialogParentView, R.id.z_dialog_parent_layout);
        checkAndSetId(rightDeleteView, R.id.z_dialog_right_delete);
        checkAndSetId(bottomDeleteView, R.id.z_dialog_bottom_delete);
        checkAndSetId(contentLayout, R.id.z_dialog_content_layout);
        checkAndSetId(operatorLayout, R.id.z_dialog_operator_layout);
        checkAndSetId(topImg, R.id.z_dialog_top_image);

        // 右上角删除
        if (rightDeleteView != null) {
            ConstraintLayout.LayoutParams lp = onCreateRightDeleteLayoutParams(dialogContext, rightDelete);
            if (contentLayout != null) {
                lp.topToBottom = contentLayout.getId();
            }
            mDialogParentView.addView(rightDeleteView, lp);
        }

        // 中间填充内容
        if (contentLayout != null) {
            ConstraintLayout.LayoutParams lp = onCreateContentLayoutParams(dialogContext);
            if (topImg != null && topImage != null) {
                lp.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
                lp.topMargin = topImage.getLpHeight() / 2;
            } else if (rightDeleteView != null) {
                lp.topToBottom = rightDeleteView.getId();
            } else {
                lp.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
            }
            if (operatorLayout != null) {
                lp.bottomToTop = operatorLayout.getId();
            } else {
                lp.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
            }
            mDialogParentView.addView(contentLayout, lp);
        }

        // 下方操作按钮
        if (operatorLayout != null) {
            ConstraintLayout.LayoutParams lp = onCreateOperatorLayoutLayoutParams(dialogContext);
            if (contentLayout != null) {
                lp.topToBottom = contentLayout.getId();
            } else {
                lp.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
            }
            mDialogParentView.addView(operatorLayout, lp);
        }


        ConstraintLayout.LayoutParams contentLp = onCreateParentLayoutParams(mContext);
        if (contentLp != null) {
            if (topImg != null && topImage != null) {
                contentLp.topMargin = topImage.getLpHeight() / 2;
            }
            if (bottomDeleteView != null && bottomDelete != null) {
                contentLp.bottomToTop = bottomDeleteView.getId();
            }
            mDialogRootView.addView(mDialogParentView, contentLp);
            // 顶部按钮
            if (topImg != null && topImage != null) {
                ConstraintLayout.LayoutParams lp = onCreateTopImgLayoutParams(mContext, topImage);
                mDialogRootView.addView(topImg, lp);
            }

            // 底部删除按钮
            if (bottomDeleteView != null && bottomDelete != null) {
                ConstraintLayout.LayoutParams lp = onCreateBottomRightLayoutParams(mContext, bottomDelete);
                lp.topToBottom = mDialogParentView.getId();
                mDialogRootView.addView(bottomDeleteView, lp);
            }
        }

        mDialog.addContentView(mDialogRootView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mDialog.setCancelable(mCancelable);
        mDialog.setCanceledOnTouchOutside(mCanceledOnTouchOutside);
        return mDialog;
    }


    private void checkAndSetId(@Nullable View view, int id) {
        if (view != null && view.getId() == View.NO_ID) {
            view.setId(id);
        }
    }


    @NonNull
    protected ConstraintLayout onCreateDialogRootView(@NonNull Context context) {
        ConstraintLayout dialogView = new ConstraintLayout(context);
        return dialogView;
    }

    @NonNull
    protected ConstraintLayout onCreateDialogContentView(@NonNull Context context) {
        ConstraintLayout dialogView = new ConstraintLayout(context);
        dialogView.setBackgroundResource(R.drawable.white_round_6dp);
        return dialogView;
    }

    @NonNull
    protected AppCompatImageView onCreateDialogTopView(@NonNull BaseShowDismissAppCompatDialog dialog, ZDialogImageView imgView) {
        if (imgView != null) {
            return imgView.buildView(dialog);
        }
        return null;
    }


    @Nullable
    protected View onCreateDeleteBtn(@NonNull BaseShowDismissAppCompatDialog dialog, ZDialogImageView imgView) {
        if (imgView != null) {
            return imgView.buildView(dialog);
        }
        return null;
    }

    @NonNull
    protected ConstraintLayout.LayoutParams onCreateTopImgLayoutParams(@NonNull Context context, ZDialogImageView imgView) {
        ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (imgView != null) {
            lp = imgView.buildLayoutParams();
        }
        lp.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        lp.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
        lp.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        return lp;
    }

    @NonNull
    protected ConstraintLayout.LayoutParams onCreateBottomRightLayoutParams(@NonNull Context context, ZDialogImageView imgView) {
        ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (imgView != null) {
            lp = imgView.buildLayoutParams();
        }
        lp.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        lp.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
        lp.verticalChainStyle = ConstraintLayout.LayoutParams.CHAIN_PACKED;
        return lp;
    }

    @NonNull
    protected ConstraintLayout.LayoutParams onCreateRightDeleteLayoutParams(@NonNull Context context, ZDialogImageView imgView) {
        ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (imgView != null) {
            lp = imgView.buildLayoutParams();
        }
        lp.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
        lp.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        lp.verticalChainStyle = ConstraintLayout.LayoutParams.CHAIN_PACKED;
        return lp;
    }

    protected ConstraintLayout.LayoutParams onCreateContentLayoutParams(@NonNull Context context) {
        ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        lp.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
        lp.constrainedHeight = true;
        lp.verticalChainStyle = ConstraintLayout.LayoutParams.CHAIN_PACKED;
        return lp;
    }

    protected ConstraintLayout.LayoutParams onCreateParentLayoutParams(@NonNull Context context) {
        ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        lp.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
        lp.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        lp.constrainedHeight = true;
        return lp;
    }


    @NonNull
    protected ConstraintLayout.LayoutParams onCreateOperatorLayoutLayoutParams(@NonNull Context context) {
        ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        lp.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
        lp.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
        lp.verticalChainStyle = ConstraintLayout.LayoutParams.CHAIN_PACKED;
        return lp;
    }

    /**
     * 创建中间内容
     *
     * @param dialog
     * @param parent
     * @param context
     * @return
     */
    @Nullable
    protected abstract View onCreateContent(@NonNull BaseShowDismissAppCompatDialog dialog, @NonNull ConstraintLayout parent, @NonNull Context context);


    /**
     * 创建底部操作按钮
     *
     * @param dialog
     * @param parent
     * @param context
     * @return
     */
    @Nullable
    protected View onCreateOperatorLayout(@NonNull final BaseShowDismissAppCompatDialog dialog, @NonNull ConstraintLayout parent, @NonNull Context context) {
        int size = mActions.size();
        if (size > 0) {
            LinearLayout layout = new LinearLayout(context);
            layout.setOrientation(mActionContainerOrientation == VERTICAL ? LinearLayout.VERTICAL : LinearLayout.HORIZONTAL);
            for (int i = 0; i < size; i++) {
                if (i != 0) {
                    // 不是第一个时候添加间距
                    layout.addView(createActionContainerSpace(context));
                }
                ZDialogAction action = mActions.get(i);
                LinearLayout.LayoutParams actionLp = action.buildLayoutParams();
                AppCompatButton actionView = action.buildActionView(mDialog, i);
                layout.addView(actionView, actionLp);
            }
            return layout;
        }
        return null;
    }

    private View createActionContainerSpace(Context context) {
        Space space = new Space(context);
        LinearLayout.LayoutParams spaceLp = new LinearLayout.LayoutParams(actionSpace, actionSpace);
        spaceLp.weight = 0;
        space.setLayoutParams(spaceLp);
        return space;
    }

    public static int dp2px(Context context, float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
    }

}
