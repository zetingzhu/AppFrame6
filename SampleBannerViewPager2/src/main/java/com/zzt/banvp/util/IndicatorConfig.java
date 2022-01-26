package com.zzt.banvp.util;

import android.content.res.Resources;
import android.graphics.Color;
import android.util.TypedValue;

import androidx.annotation.ColorInt;

/**
 * @author: zeting
 * @date: 2021/12/30
 * 指示器管理对象
 */
public class IndicatorConfig {
    // 指示器数量
    private int indicatorSize;
    // 当前选中位置
    private int currentPosition;
    // 指示器间距
    private int indicatorSpace = dp2px(8);
    // 指示器宽度
    private int normalWidth = dp2px(8);
    // 指示器高度
    private int selectedWidth = dp2px(8);
    // 默认指示器颜色
    @ColorInt
    private int normalColor = Color.parseColor("#9096BB");
    // 选中指示器颜色
    @ColorInt
    private int selectedColor = Color.parseColor("#FFFFFF");

    public int getIndicatorSize() {
        return indicatorSize;
    }

    public IndicatorConfig setIndicatorSize(int indicatorSize) {
        this.indicatorSize = indicatorSize;
        return this;
    }

    public int getNormalColor() {
        return normalColor;
    }

    public IndicatorConfig setNormalColor(int normalColor) {
        this.normalColor = normalColor;
        return this;
    }

    public int getSelectedColor() {
        return selectedColor;
    }

    public IndicatorConfig setSelectedColor(int selectedColor) {
        this.selectedColor = selectedColor;
        return this;
    }

    public int getIndicatorSpace() {
        return indicatorSpace;
    }

    public IndicatorConfig setIndicatorSpace(int indicatorSpace) {
        this.indicatorSpace = indicatorSpace;
        return this;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public IndicatorConfig setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
        return this;
    }

    public int getNormalWidth() {
        return normalWidth;
    }

    public IndicatorConfig setNormalWidth(int normalWidth) {
        this.normalWidth = normalWidth;
        return this;
    }

    public int getSelectedWidth() {
        return selectedWidth;
    }

    public IndicatorConfig setSelectedWidth(int selectedWidth) {
        this.selectedWidth = selectedWidth;
        return this;
    }

    public int dp2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
    }
}
