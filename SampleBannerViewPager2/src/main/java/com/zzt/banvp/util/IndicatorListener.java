package com.zzt.banvp.util;

import androidx.annotation.Px;

/**
 * @author: zeting
 * @date: 2021/12/30
 * 指示器监听
 */
public interface IndicatorListener {

    /**
     * 获取指示器当前数据对象
     *
     * @return
     */
    IndicatorConfig getIndicatorConfig();

    /**
     * 页面数据更新，重新设置指示器
     *
     * @param count           数量
     * @param currentPosition 当前选中位置
     */
    void onPageChanged(int count, int currentPosition);

    /**
     * This method will be invoked when the current page is scrolled, either as part
     * of a programmatically initiated smooth scroll or a user initiated touch scroll.
     *
     * @param position             Position index of the first page currently being displayed.
     *                             Page position+1 will be visible if positionOffset is nonzero.
     * @param positionOffset       Value from [0, 1) indicating the offset from the page at position.
     * @param positionOffsetPixels Value in pixels indicating the offset from position.
     */
    void onPageScrolled(int position, float positionOffset, @Px int positionOffsetPixels);

    /**
     * This method will be invoked when a new page becomes selected. Animation is not
     * necessarily complete.
     *
     * @param position Position index of the new selected page.
     */
    void onPageSelected(int position);
}
