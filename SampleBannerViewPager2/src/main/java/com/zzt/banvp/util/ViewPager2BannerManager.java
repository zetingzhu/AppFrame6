package com.zzt.banvp.util;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author: zeting
 * @date: 2022/1/6
 * Viewpager2 无限滑动 banner 处理
 * <p>
 * <p>
 * 有循环使用：
 * ViewPager2BannerManager vpbm = new ViewPager2BannerManager(vp_banner2, imgAdapter);
 * vpbm.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback());
 * vpbm.attach();
 * ViewpagerIndicatorManager vim2 = new ViewpagerIndicatorManager(vp_banner2, ci_banner2);
 * vim2.setBannerManager(vpbm);
 * vim2.setIndicatorNormalColor(Color.parseColor("#222222"));
 * vim2.setIndicatorSelectedColor(Color.parseColor("#AAAAAA"));
 * vim2.attach();
 * 如果使用了setDatas 设置数据需要初始化起始位置
 * imgAdapter.setDatas(ints);
 * vpbm.setDataAfter();
 */
public class ViewPager2BannerManager {
    private static final String TAG = ViewPager2BannerManager.class.getSimpleName();
    public static final int INVALID_VALUE = -1;
    // 轮播开始位置
    private int mStartPosition = 1;
    @NotNull
    ViewPager2 mViewPager2;
    @NotNull
    private BannerAdapterBase<?, ?> mAdapter;
    /**
     * 处理循环的滑动监听
     */
    private BannerOnPageChangeCallback mPageChangeCallback;
    /**
     * 真实的滑动监听
     */
    private CompositeOnPageChangeCallback mPageChangeEventDispatcher;

    public ViewPager2BannerManager(@NotNull ViewPager2 viewPager2, @NotNull BannerAdapterBase<?, ?> adapter) {
        this.mViewPager2 = viewPager2;
        this.mAdapter = adapter;
        mPageChangeEventDispatcher = new CompositeOnPageChangeCallback(3);
    }

    /**
     * 添加真实的滑动监听
     */
    public void registerOnPageChangeCallback(@NonNull ViewPager2.OnPageChangeCallback callback) {
        if (mPageChangeEventDispatcher != null) {
            mPageChangeEventDispatcher.addOnPageChangeCallback(callback);
        }
    }

    /**
     * 移除滑动回调
     */
    public void unregisterOnPageChangeCallback(@NonNull ViewPager2.OnPageChangeCallback callback) {
        if (mPageChangeEventDispatcher != null) {
            mPageChangeEventDispatcher.removeOnPageChangeCallback(callback);
        }
    }

    public void attach() {
        if (mViewPager2 == null || mAdapter == null) {
            throw new IllegalStateException(
                    "Adapter or ViewPager2 Can not be empty");
        }
        mPageChangeCallback = new BannerOnPageChangeCallback();
        mViewPager2.registerOnPageChangeCallback(mPageChangeCallback);

        mViewPager2.setOffscreenPageLimit(2);
        mViewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        mViewPager2.setAdapter(mAdapter);
        setCurrentItem(mStartPosition, false);
    }

    public ViewPager2 getViewPager2() {
        return mViewPager2;
    }

    public Context getContext() {
        return mViewPager2.getContext();
    }

    public BannerAdapterBase<?, ?> getAdapter() {
        return mAdapter;
    }

    /**
     * 在设置数据之后初始化
     */
    public void setDataAfter() {
        setCurrentItem(mStartPosition, false);
    }

    /**
     * 返回banner真实总数
     */
    public int getRealCount() {
        if (getAdapter() != null) {
            return getAdapter().getRealCount();
        }
        return 0;
    }

    /**
     * 获取当前选中位置
     */
    public int getCurrentItem() {
        return getViewPager2().getCurrentItem();
    }

    /**
     * 获取 adapter 当前数据总数
     */
    public int getItemCount() {
        if (getAdapter() != null) {
            return getAdapter().getItemCount();
        }
        return 0;
    }

    /**
     * 跳转到指定位置（最好在设置了数据后在调用，不然没有意义）
     */
    public void setCurrentItem(int position, boolean smoothScroll) {
        getViewPager2().setCurrentItem(position, smoothScroll);
    }

    /**
     * viewpager2 处理循环的滑动监听
     */
    class BannerOnPageChangeCallback extends ViewPager2.OnPageChangeCallback {
        private int mTempPosition = INVALID_VALUE;
        private boolean isScrolled;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//            Log.d(TAG, "滑动 -onPageScrolled- ：" + position);
            int realPosition = BannerAdapterBase.getRealPosition(true, position, getRealCount());
            if (mPageChangeEventDispatcher != null && realPosition == getCurrentItem() - 1) {
                mPageChangeEventDispatcher.onPageScrolled(realPosition, positionOffset, positionOffsetPixels);
            }
        }

        @Override
        public void onPageSelected(int position) {
            Log.d(TAG, "滑动 ViewPager2BannerManager -onPageSelected-  滑动位置 ：" + position + " isScrolled:" + isScrolled);
            if (isScrolled) {
                mTempPosition = position;
                Log.d(TAG, "滑动 ViewPager2BannerManager -onPageSelected-  滑动临时位置 ：" + mTempPosition);
                int realPosition = BannerAdapterBase.getRealPosition(true, position, getRealCount());
                if (mPageChangeEventDispatcher != null) {
                    Log.d(TAG, "滑动 ViewPager2BannerManager -onPageSelected- 实际位置 ：" + realPosition);
                    mPageChangeEventDispatcher.onPageSelected(realPosition);
                }
            } else {
                if (mTempPosition == INVALID_VALUE) {
                    int realPosition = BannerAdapterBase.getRealPosition(true, position, getRealCount());
                    if (mPageChangeEventDispatcher != null) {
                        mPageChangeEventDispatcher.onPageSelected(realPosition);
                    }
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            Log.d(TAG, "滑动 ViewPager2BannerManager -onPageScrollStateChanged- ：" + state);
            //手势滑动中,代码执行滑动中
            if (state == ViewPager2.SCROLL_STATE_DRAGGING || state == ViewPager2.SCROLL_STATE_SETTLING) {
                isScrolled = true;
            } else if (state == ViewPager2.SCROLL_STATE_IDLE) {
                //滑动闲置或滑动结束
                isScrolled = false;
                if (mTempPosition != INVALID_VALUE) {
                    if (mTempPosition == 0) {
                        setCurrentItem(getRealCount(), false);
                    } else if (mTempPosition == getItemCount() - 1) {
                        setCurrentItem(1, false);
                    }
                }
            }
            if (mPageChangeEventDispatcher != null) {
                mPageChangeEventDispatcher.onPageScrollStateChanged(state);
            }
        }
    }

}
