/*
 * Copyright  2017  zengp
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zzt.zt_textviewaction.selectv3;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Layout;
import android.text.Selection;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatEditText;

import com.zzt.zt_textviewaction.R;

/**
 * SelectableTextView ————增强版的TextView，具有以下功能：
 * <p> 1：长按文字弹出ActionMenu菜单；菜单menu可以自定义；实现自定义功能（复制，全选，翻译，分享等；默认实现了全选和复制功能）
 * <p> 2：文本两端对齐功能；适用于中文文本，英文文本 以及中英混合文本
 * Created by zengpu on 2016/11/20.
 */
public class SelectableTextView extends AppCompatEditText {

    private final int TRIGGER_LONGPRESS_TIME_THRESHOLD = 300;    // 触发长按事件的时间阈值
    private final int TRIGGER_LONGPRESS_DISTANCE_THRESHOLD = 10; // 触发长按事件的位移阈值

    private Context mContext;
    private int mScreenHeight;      // 屏幕高度
    private int mStatusBarHeight;   // 状态栏高度
    private int mActionMenuHeight;  // 弹出菜单高度
    private int mTextHighlightColor;// 选中文字背景高亮颜色

    private float mTouchDownX = 0;
    private float mTouchDownY = 0;
    private float mTouchDownRawY = 0;

    private boolean isLongPress = false;               // 是否发触了长按事件
    private boolean isLongPressTouchActionUp = false;  // 长按事件结束后，标记该次事件

//    private boolean isForbiddenActionMenu = false;     // 是否需要两端对齐 ，默认false

    private int mStartLine;             //action_down触摸事件 起始行
    private int mStartTextOffset;       //action_down触摸事件 字符串开始位置的偏移值
    private int mCurrentLine;           // action_move触摸事件 当前行
    private int mCurrentTextOffset;     //action_move触摸事件 字符串当前位置的偏移值


    private PopupWindow mActionMenuPopupWindow; // 长按弹出菜单
    private ActionMenu mActionMenu = null;

    private OnClickListener mOnClickListener;
    private CustomActionMenuCallBack mCustomActionMenuCallBack;

    public SelectableTextView(Context context) {
        this(context, null);
    }

    public SelectableTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SelectableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;

        TypedArray mTypedArray = context.obtainStyledAttributes(attrs,
                R.styleable.SelectableTextView);
//        isForbiddenActionMenu = mTypedArray.getBoolean(R.styleable.SelectableTextView_forbiddenActionMenu, false);
        mTextHighlightColor = mTypedArray.getColor(R.styleable.SelectableTextView_textHeightColor, 0x60ffeb3b);
        mTypedArray.recycle();

        init();
    }


    private void init() {
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        mScreenHeight = wm.getDefaultDisplay().getHeight();
        mStatusBarHeight = Utils.getStatusBarHeight(mContext);
        mActionMenuHeight = Utils.dp2px(mContext, 45);

        setTextIsSelectable(true);
        setCursorVisible(false);

        setTextHighlightColor(mTextHighlightColor);

        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.d("SelectableTextView", "ACTION_MOVE 长按");
                isLongPress = true;
                isLongPressTouchActionUp = false;
                return false;
            }
        });
    }


    @Override
    public boolean getDefaultEditable() {
        // 返回false，屏蔽掉系统自带的ActionMenu
        return false;
    }

//    public void setForbiddenActionMenu(boolean forbiddenActionMenu) {
//        isForbiddenActionMenu = forbiddenActionMenu;
//    }

    public void setTextHighlightColor(int color) {
        this.mTextHighlightColor = color;
        String color_hex = String.format("%08X", color);
        color_hex = "#40" + color_hex.substring(2);
        setHighlightColor(Color.parseColor(color_hex));
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        super.setOnClickListener(l);
        if (null != l) {
            mOnClickListener = l;
        }
    }

    private CheckForLongPress mPendingCheckForLongPress;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        Layout layout = getLayout();
        int currentLine; // 当前所在行

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                Log.d("SelectableTextView", "ACTION_DOWN");
                // 每次按下时，创建ActionMenu菜单，创建不成功，屏蔽长按事件
                if (null == mActionMenu) {
                    mActionMenu = createActionMenu();
                }
                mTouchDownX = event.getX();
                mTouchDownY = event.getY();
                mTouchDownRawY = event.getRawY();
                if (isLongPressTouchActionUp) {
                    currentLine = layout.getLineForVertical(getScrollY() + (int) event.getY());
                    int mWordOffset_move = layout.getOffsetForHorizontal(currentLine, (int) event.getX());
                    mStartLine = currentLine;
                    mStartTextOffset = mWordOffset_move;
                    isLongPress = true;
                    isLongPressTouchActionUp = false;
                } else {
                    isLongPress = false;
                    isLongPressTouchActionUp = false;

                    // 长按监听
                    if (mPendingCheckForLongPress == null) {
                        mPendingCheckForLongPress = new CheckForLongPress();
                    }
                    mPendingCheckForLongPress.setAnchor(mTouchDownX, mTouchDownY);
                    postDelayed(mPendingCheckForLongPress, 400);

                    if (mActionMenu.getChildCount() > 0) {
                        // 手指移动过程中的字符偏移
                        currentLine = layout.getLineForVertical(getScrollY() + (int) event.getY());
                        int mWordOffset_move = layout.getOffsetForHorizontal(currentLine, (int) event.getX());
                        mStartLine = currentLine;
                        mStartTextOffset = mWordOffset_move;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d("SelectableTextView", "ACTION_MOVE");
                // 先判断是否禁用了ActionMenu功能，以及ActionMenu是否创建失败，
                // 二者只要满足了一个条件，退出长按事件
                if (mActionMenu.getChildCount() > 0) {
                    // 手指移动过程中的字符偏移
                    currentLine = layout.getLineForVertical(getScrollY() + (int) event.getY());
                    int mWordOffset_move = layout.getOffsetForHorizontal(currentLine, (int) event.getX());
                    if (isLongPress) {
                        requestFocus();
                        mCurrentLine = currentLine;
                        mCurrentTextOffset = mWordOffset_move;
                        // 通知父布局不要拦截触摸事件
                        getParent().requestDisallowInterceptTouchEvent(true);
                        // 选择字符
                        Selection.setSelection(getEditableText(), Math.min(mStartTextOffset, mWordOffset_move),
                                Math.max(mStartTextOffset, mWordOffset_move));
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.d("SelectableTextView", "ACTION_UP");
                // 处理长按事件
                if (isLongPress) {
                    currentLine = layout.getLineForVertical(getScrollY() + (int) event.getY());
                    int mWordOffsetEnd = layout.getOffsetForHorizontal(currentLine, (int) event.getX());
                    // 至少选中一个字符
                    mCurrentLine = currentLine;
                    mCurrentTextOffset = mWordOffsetEnd;
                    int maxOffset = getEditableText().length() - 1;
                    if (mStartTextOffset > maxOffset) {
                        mStartTextOffset = maxOffset;
                    }
                    if (mCurrentTextOffset > maxOffset) {
                        mCurrentTextOffset = maxOffset;
                    }
                    if (mCurrentTextOffset == mStartTextOffset) {
                        if (mCurrentTextOffset == layout.getLineEnd(currentLine) - 1) {
                            mStartTextOffset -= 1;
                        } else {
                            mCurrentTextOffset += 1;
                        }
                    }

                    Selection.setSelection(getEditableText(), Math.min(mStartTextOffset, mCurrentTextOffset),
                            Math.max(mStartTextOffset, mCurrentTextOffset));
                    // 计算菜单显示位置
                    int mPopWindowOffsetY = calculatorActionMenuYPosition((int) mTouchDownRawY, (int) event.getRawY());
                    // 弹出菜单
                    showActionMenu(mPopWindowOffsetY, mActionMenu);
                    isLongPressTouchActionUp = true;
                    isLongPress = false;

                } else if (event.getEventTime() - event.getDownTime() < TRIGGER_LONGPRESS_TIME_THRESHOLD) {
                    // 由于onTouchEvent最终返回了true,onClick事件会被屏蔽掉，因此在这里处理onClick事件
                    if (null != mOnClickListener)
                        mOnClickListener.onClick(this);
                }
                // 通知父布局继续拦截触摸事件
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }
        return true;
    }

    /* ***************************************************************************************** */

    private final class CheckForLongPress implements Runnable {
        private float mX;
        private float mY;

        private CheckForLongPress() {
        }

        @Override
        public void run() {
            Log.d("SelectableTextView", "自定义 长按");
            isLongPress = true;
            isLongPressTouchActionUp = false;
        }

        public void setAnchor(float x, float y) {
            mX = x;
            mY = y;
        }
    }

    /**
     * 创建ActionMenu菜单
     *
     * @return
     */
    private ActionMenu createActionMenu() {
        // 创建菜单
        ActionMenu actionMenu = new ActionMenu(mContext);
        // 是否需要移除默认item
        boolean isRemoveDefaultItem = false;
        if (null != mCustomActionMenuCallBack) {
            isRemoveDefaultItem = mCustomActionMenuCallBack.onCreateCustomActionMenu(actionMenu);
        }
        if (!isRemoveDefaultItem)
            actionMenu.addDefaultMenuItem(); // 添加默认item

        actionMenu.addCustomItem();  // 添加自定义item
        actionMenu.setFocusable(true); // 获取焦点
        actionMenu.setFocusableInTouchMode(true);

        if (actionMenu.getChildCount() != 0) {
            // item监听
            for (int i = 0; i < actionMenu.getChildCount(); i++) {
                actionMenu.getChildAt(i).setOnClickListener(mMenuClickListener);
            }
        }
        return actionMenu;
    }

    /**
     * 长按弹出菜单
     *
     * @param offsetY
     * @param actionMenu
     * @return 菜单创建成功，返回true
     */
    private void showActionMenu(int offsetY, ActionMenu actionMenu) {
        mActionMenuPopupWindow = new PopupWindow(actionMenu, WindowManager.LayoutParams.WRAP_CONTENT, mActionMenuHeight, true);
        mActionMenuPopupWindow.setFocusable(true);
        mActionMenuPopupWindow.setOutsideTouchable(false);
        mActionMenuPopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        mActionMenuPopupWindow.showAtLocation(this, Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, offsetY);

        mActionMenuPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                Selection.removeSelection(getEditableText());
                // 如果设置了分散对齐，ActionMenu销毁后，强制刷新一次，防止出现文字背景未消失的情况
                SelectableTextView.this.postInvalidate();
            }
        });
    }

    /**
     * 隐藏菜单
     */
    private void hideActionMenu() {
        if (null != mActionMenuPopupWindow) {
            mActionMenuPopupWindow.dismiss();
            mActionMenuPopupWindow = null;
        }
    }

    /**
     * 菜单点击事件监听
     */
    private OnClickListener mMenuClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

            String menuItemTitle = (String) v.getTag();

            // 选中的字符的开始和结束位置
            int start = getSelectionStart();
            int end = getSelectionEnd();
            // 获得选中的字符
            String selected_str;
            if (start < 0 || end < 0 || end <= start) {
                selected_str = "";
            } else {
                selected_str = getText().toString().substring(start, end);
            }
            if (menuItemTitle.equals(ActionMenu.DEFAULT_MENU_ITEM_TITLE_SELECT_ALL)) {
                //全选事件
                Selection.selectAll(getEditableText());
            } else if (menuItemTitle.equals(ActionMenu.DEFAULT_MENU_ITEM_TITLE_COPY)) {
                // 复制事件
                Utils.copyText(mContext, selected_str);
                Toast.makeText(mContext, "复制成功！", Toast.LENGTH_SHORT).show();
                hideActionMenu();

            } else {
                // 自定义事件
                if (null != mCustomActionMenuCallBack) {
                    mCustomActionMenuCallBack.onCustomActionItemClicked(menuItemTitle, selected_str);
                }
                hideActionMenu();
            }
        }
    };

    /**
     * 计算弹出菜单相对于父布局的Y向偏移
     *
     * @param yOffsetStart 所选字符的起始位置相对屏幕的Y向偏移
     * @param yOffsetEnd   所选字符的结束位置相对屏幕的Y向偏移
     * @return
     */
    private int calculatorActionMenuYPosition(int yOffsetStart, int yOffsetEnd) {
        if (yOffsetStart > yOffsetEnd) {
            int temp = yOffsetStart;
            yOffsetStart = yOffsetEnd;
            yOffsetEnd = temp;
        }
        int actionMenuOffsetY;

        if (yOffsetStart < mActionMenuHeight * 3 / 2 + mStatusBarHeight) {
            if (yOffsetEnd > mScreenHeight - mActionMenuHeight * 3 / 2) {
                // 菜单显示在屏幕中间
                actionMenuOffsetY = mScreenHeight / 2 - mActionMenuHeight / 2;
            } else {
                // 菜单显示所选文字下方
                actionMenuOffsetY = yOffsetEnd + mActionMenuHeight / 2;
            }
        } else {
            // 菜单显示所选文字上方
            actionMenuOffsetY = yOffsetStart - mActionMenuHeight * 3 / 2;
        }
        return actionMenuOffsetY;
    }

    /* ***************************************************************************************** */

    public void setCustomActionMenuCallBack(CustomActionMenuCallBack callBack) {
        this.mCustomActionMenuCallBack = callBack;
    }
}
