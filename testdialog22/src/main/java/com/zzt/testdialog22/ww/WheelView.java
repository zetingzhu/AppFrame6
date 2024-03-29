package com.zzt.testdialog22.ww;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;


import com.zzt.testdialog22.R;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * 基于原版(https://github.com/wangjiegulu/WheelView)作了一下修改：
 * 去掉回弹阴影
 * 修正以便支持联动效果
 * 可设置颜色
 * 设置文字大小
 * 分隔线是否可见
 * 初始设置选中选项
 * 伪循环滚动
 *
 * @author 李玉江[QQ:1023694760]
 * @since 2015/12/17
 */
public class WheelView extends ScrollView {
    public static final int TEXT_SIZE = 16;
    public static final int TEXT_COLOR_FOCUS = 0XFF0288CE;
    public static final int TEXT_COLOR_NORMAL = 0XFFBBBBBB;
    public static final int LINE_COLOR = 0XFF83CDE6;
    public static final int OFF_SET = 1;
    private static final int DELAY = 50;

    private Context context;
    private LinearLayout views;
    private LinkedList<String> items = new LinkedList<String>();
    private int offset = OFF_SET; // 偏移量（需要在最前面和最后面补全）

    private int displayItemCount; // 每页显示的数量

    private int selectedIndex = OFF_SET;//索引值含补全的占位符的索引
    private int initialY;

    private Runnable scrollerTask = new ScrollerTask();
    private int itemHeight = 0;
    private int[] selectedAreaBorder;//获取选中区域的边界
    private OnWheelListener onWheelListener;

    private Paint paint;
    private int viewWidth;
    private int textSize = TEXT_SIZE;
    private int textColorNormal = TEXT_COLOR_NORMAL;
    private int textColorFocus = TEXT_COLOR_FOCUS;
    private int lineColor = LINE_COLOR;
    private boolean lineVisible = true;
    private boolean isUserScroll = false;//是否用户手动滚动
    private boolean cycleDisable = false;//是否禁用伪循环
    private float previousY = 0;//记录按下时的Y坐标
    private boolean isUseAppTextView = false;
    // 未选中文字大小
    private int textSizeNormal = TEXT_SIZE;
    // 添加一个默认View高度, 如果没值就默认计算
    private int viewHeightDefault = -1;
    // 是否线充满宽度
    private boolean isFullWidth = false;

    public WheelView(Context context) {
        super(context);
        init(context);
    }

    public WheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WheelView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        this.context = context;

        // 2015/12/15 去掉ScrollView的阴影
        setFadingEdgeLength(0);
        if (Build.VERSION.SDK_INT >= 9) {
            setOverScrollMode(OVER_SCROLL_NEVER);
        }

        setVerticalScrollBarEnabled(false);

        views = new LinearLayout(context);
        views.setOrientation(LinearLayout.VERTICAL);
        addView(views);
    }

    private void startScrollerTask() {
        initialY = getScrollY();
        postDelayed(scrollerTask, DELAY);
    }

    private void initData() {
        displayItemCount = offset * 2 + 1;

        // 2015/12/15 添加此句才可以支持联动效果
        views.removeAllViews();

        for (String item : items) {
            views.addView(createView(item));
        }

        // 2016/1/15 焦点文字颜色高亮位置，逆推“int position = y / itemHeight + offset”
        refreshItemView(itemHeight * (selectedIndex - offset));
    }

    @ColorInt
    private int randomColor() {
        return Color.argb(128, new Random().nextInt(256), new Random().nextInt(256), new Random().nextInt(256));
    }

    private TextView createView(String item) {
//        TextView tv = new TextView(context);
        AppCompatTextView tv = new AppCompatTextView(context, null, R.style.wheelStyle);
        if (viewHeightDefault == -1) {
            tv.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        } else {
            tv.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, viewHeightDefault));
        }
        tv.setSingleLine(true);
        tv.setEllipsize(TextUtils.TruncateAt.END);
        tv.setText(item);
        tv.setTextSize(textSize);
        tv.setGravity(Gravity.CENTER);
        if (viewHeightDefault == -1) {
            int padding = dip2px(15);
            tv.setPadding(padding, padding, padding, padding);
        }
        if (0 == itemHeight) {
            itemHeight = getViewMeasuredHeight(tv);
            LogUtils.verbose(this, "itemHeight: " + itemHeight + " viewHeightDefault: " + viewHeightDefault);
            views.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, itemHeight * displayItemCount));
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) this.getLayoutParams();
            setLayoutParams(new LinearLayout.LayoutParams(lp.width, itemHeight * displayItemCount));
        }
        return tv;
    }

    private void refreshItemView(int y) {
        int position = y / itemHeight + offset;
        int remainder = y % itemHeight;
        int divided = y / itemHeight;

        if (remainder == 0) {
            position = divided + offset;
        } else {
            if (remainder > itemHeight / 2) {
                position = divided + offset + 1;
            }
        }

        int childSize = views.getChildCount();
        for (int i = 0; i < childSize; i++) {
            TextView itemView = (TextView) views.getChildAt(i);
            if (null == itemView) {
                return;
            }
            // 2015/12/15 可设置颜色
            if (position == i) {
                itemView.setTextColor(textColorFocus);
                itemView.setTextSize(textSize);
            } else {
                itemView.setTextColor(textColorNormal);
                itemView.setTextSize(textSizeNormal);
            }
        }
    }


    /**
     * 选中回调
     */
    private void onSelectedCallBack() {
        if (null != onWheelListener) {
            // 2015/12/25 真实的index应该忽略偏移量
            int realIndex = selectedIndex - offset;
            onWheelListener.onSelected(isUserScroll, realIndex, items.get(this.selectedIndex >= items.size() ? items.size() - 1 : this.selectedIndex));
        }
    }

    private int dip2px(float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private int getViewMeasuredHeight(View view) {
        if (viewHeightDefault == -1) {
            int width = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
            int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
            view.measure(width, expandSpec);
            return view.getMeasuredHeight();
        } else {
            return viewHeightDefault;
        }
    }

    @Override
    public void setBackground(Drawable background) {
        setBackgroundDrawable(background);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void setBackgroundDrawable(Drawable background) {
        if (context != null) {
            super.setBackgroundDrawable(new LineDrawable());
        } else {
            super.setBackgroundDrawable(background);
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        LogUtils.verbose(this, "horizontal scroll origin: " + l + ", vertical scroll origin: " + t);
        refreshItemView(t);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        LogUtils.verbose(this, "w: " + w + ", h: " + h + ", oldw: " + oldw + ", oldh: " + oldh);
        viewWidth = w;
        setBackgroundDrawable(null);
    }

    @Override
    public void fling(int velocityY) {
        super.fling(velocityY / 3);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        isUserScroll = true;//触发触摸事件，说明是用户在滚动
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                previousY = ev.getY();
                break;
            case MotionEvent.ACTION_UP:
                LogUtils.verbose(this, String.format("items=%s, offset=%s", items.size(), offset));
                LogUtils.verbose(this, "selectedIndex=" + selectedIndex);
                if (cycleDisable) {
                    startScrollerTask();
                    break;
                }
                float delta = ev.getY() - previousY;
                LogUtils.verbose(this, "delta=" + delta);
                if (selectedIndex == offset && delta > 0) {
                    //滑动到第一项时，若继续向下滑动，则自动跳到最后一项
                    setSelectedIndex(items.size() - offset * 2 - 1);
                } else if (selectedIndex == (items.size() - offset - 1) && delta < 0) {
                    //滑动到最后一项时，若继续向上滑动，则自动跳到第一项
                    setSelectedIndex(0);
                } else {
                    startScrollerTask();
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    private void _setItems(List<String> list) {
        items.clear();
        items.addAll(list);
        // 前面和后面补全
        for (int i = 0; i < offset; i++) {
            items.addFirst("");
            items.addLast("");
        }
        initData();
    }

    public void setItems(List<String> list) {
        _setItems(list);
        // 2015/12/25 初始化时设置默认选中项
        setSelectedIndex(0);
    }

    public void setItems(List<String> list, int index) {
        _setItems(list);
        setSelectedIndex(index);
    }

    public void setItems(List<String> list, String item) {
        _setItems(list);
        setSelectedItem(item);
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public int getTextSizeNormal() {
        return textSizeNormal;
    }

    public void setTextSizeNormal(int textSizeNormal) {
        this.textSizeNormal = textSizeNormal;
    }

    public int getTextColor() {
        return textColorFocus;
    }

    public void setTextColor(@ColorInt int textColorNormal, @ColorInt int textColorFocus) {
        this.textColorNormal = textColorNormal;
        this.textColorFocus = textColorFocus;
    }

    public void setTextColor(@ColorInt int textColor) {
        this.textColorFocus = textColor;
    }

    public boolean isLineVisible() {
        return lineVisible;
    }

    public void setLineVisible(boolean lineVisible) {
        this.lineVisible = lineVisible;
    }

    public int getLineColor() {
        return lineColor;
    }

    public void setLineColor(@ColorInt int lineColor) {
        this.lineColor = lineColor;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(@IntRange(from = 1, to = 4) int offset) {
        if (offset < 1 || offset > 4) {
            throw new IllegalArgumentException("Offset must between 1 and 4");
        }
        this.offset = offset;
    }

    public void setCycleDisable(boolean cycleDisable) {
        this.cycleDisable = cycleDisable;
    }

    /**
     * 从0开始计数，所有项包括偏移量
     */
    private void setSelectedIndex(@IntRange(from = 0) final int index) {
        isUserScroll = false;
        this.post(new Runnable() {
            @Override
            public void run() {
                //滚动到选中项的位置，smoothScrollTo滚动视觉效果有延迟
                //smoothScrollTo(0, index * itemHeight);
                scrollTo(0, index * itemHeight);
                //选中这一项的值
                selectedIndex = index + offset;
                onSelectedCallBack();
                //默认选中第一项时颜色需要高亮
                refreshItemView(itemHeight * index);
            }
        });
    }

    public void setSelectedItem(String item) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).equals(item)) {
                //调用_setItems(List)时额外添加了offset个占位符到items里，需要忽略占位符所占的位
                setSelectedIndex(i - offset);
                break;
            }
        }
    }

    public String getSelectedItem() {
        return items.get(selectedIndex);
    }

    public int getSelectedIndex() {
        return selectedIndex - offset;
    }

    public void setOnWheelListener(OnWheelListener onWheelListener) {
        this.onWheelListener = onWheelListener;
    }

    public interface OnWheelListener {
        /**
         * 滑动选择回调
         *
         * @param isUserScroll 是否用户手动滚动，用于联动效果判断是否自动重置选中项
         * @param index        当前选择项的索引
         * @param item         当前选择项的值
         */
        void onSelected(boolean isUserScroll, int index, String item);
    }

    private class ScrollerTask implements Runnable {

        @Override
        public void run() {
            // 2015/12/17 java.lang.ArithmeticException: divide by zero
            if (itemHeight == 0) {
                return;
            }
            int newY = getScrollY();
            if (initialY - newY == 0) { // stopped
                final int remainder = initialY % itemHeight;
                final int divided = initialY / itemHeight;
                LogUtils.verbose(this, "initialY: " + initialY + ", remainder: " + remainder + ", divided: " + divided + ", itemHeight:" + itemHeight);
                if (remainder == 0) {
                    selectedIndex = divided + offset;
                    onSelectedCallBack();
                } else {
                    if (remainder > itemHeight / 2) {
                        post(new Runnable() {
                            @Override
                            public void run() {
                                smoothScrollTo(0, initialY - remainder + itemHeight);
                                selectedIndex = divided + offset + 1;
                                onSelectedCallBack();
                            }
                        });
                    } else {
                        post(new Runnable() {
                            @Override
                            public void run() {
                                smoothScrollTo(0, initialY - remainder);
                                selectedIndex = divided + offset;
                                onSelectedCallBack();
                            }
                        });
                    }
                }
            } else {
                startScrollerTask();
            }
        }

    }

    private class LineDrawable extends Drawable {

        LineDrawable() {
            if (viewWidth == 0) {
                viewWidth = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth();
            }

            // 2015/12/22 可设置分隔线是否可见
            if (!lineVisible) {
                return;
            }

            if (null == paint) {
                paint = new Paint();
                paint.setColor(lineColor);
                paint.setStrokeWidth(dip2px(0.5f));
            }
        }

        @Override
        public void draw(@NonNull Canvas canvas) {
            if (!lineVisible) {
                return;
            }
            if (null == selectedAreaBorder) {
                selectedAreaBorder = new int[2];
                selectedAreaBorder[0] = itemHeight * offset;
                selectedAreaBorder[1] = itemHeight * (offset + 1);
            }
            if (isFullWidth) {
                canvas.drawLine(0, selectedAreaBorder[0], viewWidth, selectedAreaBorder[0], paint);
                canvas.drawLine(0, selectedAreaBorder[1], viewWidth, selectedAreaBorder[1], paint);
            } else {
                canvas.drawLine(viewWidth / 6, selectedAreaBorder[0], viewWidth * 5 / 6, selectedAreaBorder[0], paint);
                canvas.drawLine(viewWidth / 6, selectedAreaBorder[1], viewWidth * 5 / 6, selectedAreaBorder[1], paint);
            }
        }

        @Override
        public void setAlpha(int alpha) {

        }

        @Override
        public void setColorFilter(ColorFilter cf) {

        }

        @Override
        public int getOpacity() {
            return PixelFormat.UNKNOWN;
        }
    }

    public void setUseAppTextView(boolean useAppTextView) {
        isUseAppTextView = useAppTextView;
    }


    public int getItemHeight() {
        return itemHeight;
    }

    public int getViewHeightDefault() {
        return viewHeightDefault;
    }

    public void setViewHeightDefault(int viewHeightDefault) {
        this.viewHeightDefault = viewHeightDefault;
    }

    public boolean isFullWidth() {
        return isFullWidth;
    }

    public void setFullWidth(boolean fullWidth) {
        isFullWidth = fullWidth;
    }
}
