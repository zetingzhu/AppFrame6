package com.zzt.horislidesample;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.customview.widget.ViewDragHelper;

/**
 * @author: zeting
 * @date: 2022/2/9
 * 横向滑动
 */
public class SlideHoriView extends FrameLayout {
    private static final String TAG = "SlideHoriView";
    /**
     * 手势监听工具类
     */
    private ViewDragHelper viewDragHelper;
    /**
     * 移动视图
     */
    private View mMoveView;
    /**
     * 上下文对象
     */
    private Context mContext;

    /**
     * 结束监听
     */
    private TouchEndListener endListener;

    public SlideHoriView(Context context) {
        this(context, null);
    }

    public SlideHoriView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideHoriView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initView();
    }

    /**
     * 添加监听
     */
    public void addEndTouchListener(TouchEndListener listener) {
        this.endListener = listener;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    private void initView() {
        viewDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public void onEdgeTouched(int edgeFlags, int pointerId) {
                super.onEdgeTouched(edgeFlags, pointerId);
                Log.d(TAG, "-------onEdgeTouched-------edgeFlags:" + edgeFlags + "  pointerId:" + pointerId);
            }

            @Override
            public void onViewDragStateChanged(int state) {
                super.onViewDragStateChanged(state);
                Log.d(TAG, "-------onViewDragStateChanged-------state:" + state);
            }

            @Override
            public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx, int dy) {
                super.onViewPositionChanged(changedView, left, top, dx, dy);
                Log.d(TAG, "-------onViewPositionChanged-------");
            }

            @Override
            public void onViewCaptured(@NonNull View capturedChild, int activePointerId) {
                super.onViewCaptured(capturedChild, activePointerId);
                Log.d(TAG, "-------onViewCaptured-------");
            }

            @Override
            public boolean onEdgeLock(int edgeFlags) {
                Log.d(TAG, "-------onEdgeLock-------");
                return super.onEdgeLock(edgeFlags);
            }

            @Override
            public void onEdgeDragStarted(int edgeFlags, int pointerId) {
                super.onEdgeDragStarted(edgeFlags, pointerId);
                Log.d(TAG, "-------onEdgeDragStarted-------");
            }

            @Override
            public int getOrderedChildIndex(int index) {
                Log.d(TAG, "-------getOrderedChildIndex-------index:" + index);
                return super.getOrderedChildIndex(index);
            }

            @Override
            public int getViewVerticalDragRange(@NonNull View child) {
                Log.d(TAG, "-------getViewVerticalDragRange-------");
                return super.getViewVerticalDragRange(child);
            }

            @Override
            public int getViewHorizontalDragRange(@NonNull View child) {
                Log.d(TAG, "-------getViewHorizontalDragRange-------");
                return super.getViewHorizontalDragRange(child);
            }

            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                boolean moveTry = mMoveView == child;
                Log.d(TAG, "-------tryCaptureView-------moveTry:" + moveTry);
                getParent().requestDisallowInterceptTouchEvent(true);
                return moveTry;
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
//                Log.i(TAG, "-----onViewReleased 结束 ----- xvel:" + xvel + " -yvel:" + yvel + " left:" + getPaddingLeft());
                super.onViewReleased(releasedChild, xvel, yvel);
                if (endListener != null) {
                    int maxX = getWidth() - getPaddingEnd() - mMoveView.getWidth();
                    int childLeft = releasedChild.getLeft();
                    Log.d(TAG, releasedChild + "最大可滑动：" + maxX + " 当前滑动：" + childLeft);
                    if (childLeft >= (maxX - 10)) {
                        endListener.submitSlide();
                    } else {
                        endListener.endTouch();
                    }
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                releasedChild.layout(getPaddingStart(), getPaddingTop(), getPaddingStart() + releasedChild.getWidth(), getPaddingTop() + releasedChild.getHeight());
            }

            @Override
            public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
                return getPaddingTop();
            }

            @Override
            public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
                final int oldLeft = child.getLeft();
                int minX = getPaddingStart();
                int maxX = getWidth() - getPaddingEnd() - mMoveView.getWidth();
//                Log.i(TAG, "-----clampViewPositionHorizontal -----" + oldLeft + " left:" + left + " maxX:" + maxX + " dx:" + dx + " getPaddingTop():" + getPaddingTop());
                if (left > minX && left < maxX) {
                    child.layout(left, getPaddingTop(), left + child.getWidth(), child.getHeight() + getPaddingTop());
                }
                return oldLeft;
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        viewDragHelper.processTouchEvent(event);
//        Log.w(TAG, "onTouchEvent 触摸 ");
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean site = viewDragHelper.shouldInterceptTouchEvent(ev);
//        Log.w(TAG, "onInterceptTouchEvent 触摸 site:" + site);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();
        if (count != 3) {
            throw new RuntimeException("必须切并且只能包含3个子类，不然就是有问题的");
        }
        int maxHeight = 0;
        int maxWidth = getPaddingStart() + getPaddingEnd();
        int childState = 0;

        for (int i = 0; i < count; i++) {
            if (i == 2) {
                mMoveView = getChildAt(i);
            }
            final View child = getChildAt(i);

            if (child.getVisibility() != GONE) {
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
                final LayoutParams lp = (LayoutParams) child.getLayoutParams();
                maxWidth += child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
                maxHeight = Math.max(maxHeight, child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin + getPaddingTop() + getPaddingBottom());
                childState = combineMeasuredStates(childState, child.getMeasuredState());
            }
        }
        // Check against our minimum height and width
        maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());

        // Check against our foreground's minimum height and width
        final Drawable drawable = getForeground();
        if (drawable != null) {
            maxHeight = Math.max(maxHeight, drawable.getMinimumHeight());
            maxWidth = Math.max(maxWidth, drawable.getMinimumWidth());
        }

        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, childState),
                resolveSizeAndState(maxHeight, heightMeasureSpec, childState << MEASURED_HEIGHT_STATE_SHIFT));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        layoutHorizontal(left, top, right, bottom);
    }

    void layoutHorizontal(int left, int top, int right, int bottom) {
        Log.d(TAG, "绘制 L：" + left + " T:" + top + " R:" + right + " B:" + bottom);
        int childTop = 0;
        int childLeft = getPaddingLeft();
        int childRight = getWidth() - getPaddingEnd();
        final int count = getChildCount();
        if (count != 3) {
            throw new RuntimeException("必须切并且只能包含3个子类，不然就是有问题的");
        }
        int childSpace = getHeight() - getPaddingTop() - getPaddingBottom();
        // 把这两个子类反过来布局
//        for (int i = count; i > 0; i--) {
//            final View child = getChildAt(i - 1);
//            if (child == null) {
//                childLeft += 0;
//            } else if (child.getVisibility() != GONE) {
//                final int childWidth = child.getMeasuredWidth();
//                final int childHeight = child.getMeasuredHeight();
//                Log.d(TAG, " H:" + getHeight() + " - h:" + childHeight);
//                final FrameLayout.LayoutParams lp = (LayoutParams) child.getLayoutParams();
//                if (i == 2) {
//                    childTop = getPaddingTop() + ((childSpace - childHeight) / 2) + lp.topMargin - lp.bottomMargin;
//                } else {
//                    childTop = getPaddingTop();
//                }
//                childLeft += lp.leftMargin;
//                setChildFrame(child, childLeft, childTop, childWidth, childHeight);
//                childLeft += childWidth + lp.rightMargin;
//            }
//        }

        for (int i = count; i > 0; i--) {
            final View child = getChildAt(i - 1);
            if (child != null) {
                final int childWidth = child.getMeasuredWidth();
                final int childHeight = child.getMeasuredHeight();
                childTop = getPaddingTop();
                final LayoutParams lp = (LayoutParams) child.getLayoutParams();
                if (i == count) {
                    childLeft += lp.leftMargin;
                    setChildFrame(child, childLeft, childTop, childWidth, childHeight);
                } else if (child instanceof TextView) {
                    childTop = getPaddingTop() + ((childSpace - childHeight) / 2) + lp.topMargin - lp.bottomMargin;
                    int textLeft = getWidth() / 2 - childWidth / 2;
                    setChildFrame(child, textLeft, childTop, childWidth, childHeight);
                } else {
                    setChildFrame(child, childRight - childWidth, childTop, childWidth, childHeight);
                }
            }
        }
    }

    private void setChildFrame(View child, int left, int top, int width, int height) {
        Log.d(TAG, "首次绘制 " + child + " L：" + left + " T:" + top + " W:" + width + " H:" + height);
        child.layout(left, top, left + width, top + height);
    }


    /**
     * 结束监听
     */
    public interface TouchEndListener {
        // 结束滑动
        void endTouch();

        // 滑动提交
        void submitSlide();
    }

}
