package com.zzt.horislidesample;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.RectF;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.customview.widget.ViewDragHelper;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Property;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;


/**
 * 横向滑动
 */
public class SlideHoriView extends FrameLayout {
    private static final String TAG = "SlideHoriView";


    private static final int DEFAULT_CHILD_GRAVITY = Gravity.TOP | Gravity.START;
    LinearLayout linearLayout;

    private ViewDragHelper viewDragHelper;

    /**
     * 解锁触发阀值
     * 就是说当用户滚动滑块占当前视图宽度的百分比
     * 比如说 当前 视图宽度 = 1000px
     * 触发比率 = 0.75
     * 那么 触发阀值就是 750 = 1000 * 0.75
     */
    private float unlockTriggerValue = 0.99f;

    /**
     * 动画时长
     */
    private int animationTimeDuration = 300;


    /**
     * 回弹动画实现
     */
    private ObjectAnimator oa;

    /**
     * 锁视图
     */
    private View mLockView;
    private View mMoveView;

    /**
     * 圆角背景矩形
     */
    private RectF mRoundRect = new RectF();
    // 上下半部分圆弧
    private RectF mRoundArcTop = new RectF();
    private RectF mRoundArcBottom = new RectF();
    // 发光区域
    private Rect mRectW = new Rect();

    /**
     * 上下文对象
     */
    private Context mContext;

    /**
     * 触摸事件时间
     */
    private long downTime = 0L;
    private long upTime = 0L;
    private long leadTime = 0L;
    /**
     * 是否显示滑动操作提醒
     */
    private boolean isShowBg = false;
    /**
     * 是否可以滑动
     */
    private boolean isMove = false;
    /**
     * 监听事件
     */
    private OnTouchBring mTouchBring;
    private SlideOnClickListener mClickListener;
    /**
     * 滑动的中心位置
     */
    private float moveHeight = 0f;
    /**
     * 是否属于点击事件
     */
    private boolean isOnClick = false;


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
     * 设置是否可以滑动
     */
    public void setIsMove(boolean move) {
        this.isMove = move;
    }

    /**
     * 设置接触监听
     *
     * @param mTouchBring
     */
    public void setOnTouchBring(OnTouchBring mTouchBring) {
        this.mTouchBring = mTouchBring;
    }

    /**
     * 设置点击监听
     *
     * @param mSlideClick
     */
    public void setOnClickListener(SlideOnClickListener mSlideClick) {
        this.mClickListener = mSlideClick;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

    }

    private void initView() {
        viewDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {

            @Override
            public boolean tryCaptureView(View child, int pointerId) {

                return true;
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                Log.i(TAG, "-----onViewReleased 结束 -----");
                super.onViewReleased(releasedChild, xvel, yvel);
                moveHeight = 0f;
                upTime = System.currentTimeMillis();
                leadTime = upTime - downTime;
                if ((upTime - downTime) <= 1000) {
                    isOnClick = true;
                    if (mClickListener != null) {
                        if (!isMove) {
                            mClickListener.onClick();
                        }
                    }
                } else {
                    isOnClick = false;
                }

                int movedDistance = releasedChild.getTop() + releasedChild.getHeight();
                if (movedDistance >= getHeight() * unlockTriggerValue) {
                    stopTouch(false);
                    if (mClickListener != null) {
                        if (isMove) {
                            mClickListener.onMoveClick();
                        }
                    }
                } else {
//                    //回到起点
                    animToYToPosition(releasedChild, 0, animationTimeDuration, isOnClick);
                }

            }

            @Override
            public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {



                return super.clampViewPositionHorizontal(child, left, dx);
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                if (isMove) {
                    isShowBg = true;
                    if (mTouchBring != null) {
                        mTouchBring.bringToFront();
                    }
                    final int oldTop = child.getTop();
                    int minY = 0;
                    int maxY = getHeight() - mLockView.getHeight();
                    if (top > minY && top < maxY) {
                        child.layout(child.getLeft(), top, child.getLeft() + child.getWidth(), top + child.getHeight());
                        moveHeight = top + child.getHeight() - child.getWidth() / 2;
                    }

                    return oldTop;
                } else {
                    return child.getTop();
                }
            }

        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        viewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.d(TAG, "onInterceptTouchEvent 触摸");
        if (mTouchBring != null) {
            mTouchBring.bringToFront();
        }
        return viewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();
        int maxHeight = 0;
        int maxWidth = 0;
        int childState = 0;

        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
                final FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) child.getLayoutParams();
                maxWidth += child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
                maxHeight += Math.max(maxHeight, child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin);
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
        int childTop = getPaddingTop();
        int childLeft = getPaddingLeft();
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child == null) {
                childLeft += 0;
            } else if (child.getVisibility() != GONE) {
                final int childWidth = child.getMeasuredWidth();
                final int childHeight = child.getMeasuredHeight();
                final FrameLayout.LayoutParams lp = (LayoutParams) child.getLayoutParams();

                childLeft += lp.leftMargin;
                setChildFrame(child, childLeft, childTop, childWidth + getPaddingRight(), childHeight);
                childLeft += childWidth + lp.rightMargin;
            }
        }
    }

    private void setChildFrame(View child, int left, int top, int width, int height) {
        child.layout(left, top, left + width, top + height);
    }

    /**
     * 使用动画转到指定的位置
     *
     * @param view 需要作用动画的视图
     * @param toY  需要转到的位置
     */
    public void animToYToPosition(final View view, int toY, long animationTime, final boolean onClick) {
        Property<View, Integer> layoutProperty = new Property<View, Integer>(Integer.class, "layout") {

            @Override
            public void set(View object, Integer value) {
                Log.i(TAG, "----- animToYToPosition mLockView -----");
                object.layout((getWidth() - mLockView.getWidth()) / 2, value,
                        (getWidth() - mLockView.getWidth()) / 2 + object.getWidth(), value + object.getHeight());

                moveHeight = value + object.getHeight() - object.getWidth() / 2;
            }

            @Override
            public Integer get(View object) {
                return view.getTop();
            }
        };

        //原来的动画正在执行
        //取消掉，防止多重动画冲突
        if (oa != null && oa.isRunning()) {
            oa.cancel();
        }
        oa = ObjectAnimator.ofInt(view, layoutProperty, view.getTop(), toY);
        oa.setInterpolator(new AccelerateInterpolator());
        oa.setDuration(animationTime);
        oa.start();
        oa.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                Log.i(TAG, "----- 动画执行结束 -----");
                stopTouch(onClick);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                Log.i(TAG, "----- 动画执行取消 -----");
                stopTouch(onClick);
            }
        });

    }

    /**
     * 结束各种动画操作
     */
    public void stopTouch(boolean onClick) {
        if (onClick) {
            mLockView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    stopTouch(false);
                }
            }, 1000);
        } else {
            isShowBg = false;
            // 结束触摸的时候换到底层
            if (mTouchBring != null) {
                mTouchBring.bringToBack();
            }
            // 设置按钮位置在初始位置
            int lockViewWidth = mLockView.getMeasuredWidth();
            int lockViewHeight = lockViewWidth;
            Log.i(TAG, "----- stopTouch mLockView -----");
            mLockView.layout(0, 0, lockViewWidth, lockViewHeight);

            postInvalidate();
        }
    }

    public interface SlideOnClickListener {
        /**
         * 点击事件
         */
        void onClick();

        /**
         * 滑动到底部的事件
         */
        void onMoveClick();
    }

    public interface OnTouchBring {
        /**
         * 将布局移动到最上层
         */
        void bringToFront();

        /**
         * 将布局移动到下层
         */
        void bringToBack();
    }

}
