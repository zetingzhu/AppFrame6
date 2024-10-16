package com.zzt.banvp.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;


/**
 * @author: zeting
 * @date: 2021/3/11
 * 下方圆角矩形滚动滑块
 */
public class RoundRectIndicator extends BaseIndicator {
    private static final String TAG = RoundRectIndicator.class.getSimpleName();
    // 背景色画笔
    Paint mBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    // 前景色画笔
    Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    // 总宽度
    private int viewWidth = 0;
    // 前景色宽度
    private int barWidth = 0;
    // 背景色矩形
    private RectF mBgRect = new RectF();
    // 前景色矩形
    private RectF mRect = new RectF();
    // 弧度
    private Float mRadius = 0f;
    // 进度
    Float progress = 0f;

    public RoundRectIndicator(Context context) {
        super(context);
        initView(context);
    }

    public RoundRectIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public RoundRectIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
//        mBgPaint.setColor(ContextCompat.getColor(context, R.color.color_F2F3F9_or_474747));
//        mBgPaint.setStyle(Paint.Style.FILL);
//        mPaint.setColor(ContextCompat.getColor(context, R.color.color_9096BB_or_ffffff));
//        mPaint.setStyle(Paint.Style.FILL);
//
//        barWidth = context.getResources().getDimensionPixelOffset(R.dimen.margin_20dp);
    }

    public void setBgQColor(@ColorInt int bgColor, @ColorInt int color) {
        mBgPaint.setColor(bgColor);
        mPaint.setColor(color);
    }

    public void setProgress(Float progress) {
        this.progress = progress;
        invalidate();
    }

    public void setBarWidth(int barWidth) {
        this.progress = 0f;
        this.barWidth = barWidth;
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
        mBgRect.set(0f, 0f, w * 1f, h * 1f);
        mRadius = h / 2f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制背景
        canvas.drawRoundRect(mBgRect, mRadius, mRadius, mBgPaint);
        //计算指示器的长度和位置
        float leftOffset = (viewWidth - barWidth) * progress;
        float left = mBgRect.left + leftOffset;
        float right = left + barWidth;
        mRect.set(left, mBgRect.top, right, mBgRect.bottom);
        //绘制指示器
        canvas.drawRoundRect(mRect, mRadius, mRadius, mPaint);
    }
}
