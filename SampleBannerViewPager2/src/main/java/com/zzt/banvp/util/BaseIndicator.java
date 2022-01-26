package com.zzt.banvp.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * @author: zeting
 * @date: 2021/12/30
 * 指示器基类
 */
public class BaseIndicator extends View implements IndicatorListener {
    protected IndicatorConfig config;
    protected Paint mPaint;
    protected float offset;

    public BaseIndicator(Context context) {
        this(context, null);
    }

    public BaseIndicator(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        config = new IndicatorConfig();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.TRANSPARENT);
        mPaint.setColor(config.getNormalColor());
    }

    @Override
    public IndicatorConfig getIndicatorConfig() {
        return config;
    }

    @Override
    public void onPageChanged(int count, int currentPosition) {
        config.setIndicatorSize(count);
        config.setCurrentPosition(currentPosition);
        requestLayout();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        offset = positionOffset;
        invalidate();
    }

    @Override
    public void onPageSelected(int position) {
        config.setCurrentPosition(position);
        invalidate();
    }

}
