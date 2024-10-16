package com.zzt.banvp.util;

import android.content.Context;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

/**
 * @author: zeting
 * @date: 2021/12/30
 * Viewpager2 和 indicator 绑定管理器
 *

没有循环，简单使用
ViewpagerIndicatorManager vim1 = new ViewpagerIndicatorManager(vp_banner1, ci_banner1);
vim1.setIndicatorNormalColor(Color.parseColor("#222222"));
vim1.setIndicatorSelectedColor(Color.parseColor("#AAAAAA"));
vim1.attach();

有循环使用：
ViewPager2BannerManager vpbm = new ViewPager2BannerManager(vp_banner2, imgAdapter);
vpbm.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback());
vpbm.attach();
ViewpagerIndicatorManager vim2 = new ViewpagerIndicatorManager(vp_banner2, ci_banner2);
vim2.setBannerManager(vpbm);
vim2.setIndicatorNormalColor(Color.parseColor("#222222"));
vim2.setIndicatorSelectedColor(Color.parseColor("#AAAAAA"));
vim2.attach();
如果使用了setDatas 设置数据需要初始化起始位置
imgAdapter.setDatas(ints);
vpbm.setDataAfter();

 */
public class ViewPager2IndicatorManager {
    ViewPager2 mViewPager2;
    CircleIndicator mIndicator;
    @Nullable
    private RecyclerView.Adapter<?> mAdapter;

    private ViewPager2OnPageChangeCallback mPageChangeCallback;

    /*** 和ViewPager2 绑定的无限滚动管理类 */
    private ViewPager2BannerManager bannerManager;

    public ViewPager2IndicatorManager(ViewPager2 viewPager2, CircleIndicator indicator) {
        this.mViewPager2 = viewPager2;
        this.mIndicator = indicator;
    }

    /**
     * 设置绑定的滚动管理器
     */
    public void setBannerManager(ViewPager2BannerManager bannerManager) {
        this.bannerManager = bannerManager;
    }

    public void attach() {
        if (mViewPager2 == null || mIndicator == null) {
            throw new IllegalStateException(
                    "indicator or ViewPager2 Can not be empty");
        }
        mAdapter = mViewPager2.getAdapter();
        if (mAdapter == null) {
            throw new IllegalStateException(
                    "attached before ViewPager2 has an " + "adapter");
        }

        mPageChangeCallback = new ViewPager2OnPageChangeCallback();
        if (bannerManager != null) {
            bannerManager.registerOnPageChangeCallback(mPageChangeCallback);
        } else {
            mViewPager2.registerOnPageChangeCallback(mPageChangeCallback);
        }
        setIndicatorPageChange();
        getAdapter().registerAdapterDataObserver(mAdapterDataObserver);
    }

    public IndicatorListener getIndicator() {
        return mIndicator;
    }

    public ViewPager2 getViewPager2() {
        return mViewPager2;
    }

    public Context getContext() {
        return mViewPager2.getContext();
    }

    public int getCurrentItem() {
        if (bannerManager != null) {
            return bannerManager.getCurrentItem();
        }
        return getViewPager2().getCurrentItem();
    }

    public RecyclerView.Adapter<?> getAdapter() {
        return mAdapter;
    }

    public IndicatorConfig getIndicatorConfig() {
        if (getIndicator() != null) {
            return getIndicator().getIndicatorConfig();
        }
        return null;
    }

    /**
     * 获取真实数量
     */
    public int getRealCount() {
        if (bannerManager != null) {
            return bannerManager.getRealCount();
        }
        if (getAdapter() != null) {
            return getAdapter().getItemCount();
        }
        return 0;
    }

    public void setIndicatorPageChange() {
        if (getIndicator() != null) {
            getIndicator().onPageChanged(getRealCount(), 0);
        }
    }

    public void setIndicatorSelectedColor(@ColorInt int color) {
        if (getIndicatorConfig() != null) {
            getIndicatorConfig().setSelectedColor(color);
        }
    }

    public void setIndicatorSelectedColorRes(@ColorRes int color) {
        setIndicatorSelectedColor(ContextCompat.getColor(getContext(), color));
    }

    public void setIndicatorNormalColor(@ColorInt int color) {
        if (getIndicatorConfig() != null) {
            getIndicatorConfig().setNormalColor(color);
        }
    }

    public void setIndicatorNormalColorRes(@ColorRes int color) {
        setIndicatorNormalColor(ContextCompat.getColor(getContext(), color));
    }

    public void setIndicatorSpace(int indicatorSpace) {
        if (getIndicatorConfig() != null) {
            getIndicatorConfig().setIndicatorSpace(indicatorSpace);
        }
    }

    public void setIndicatorWidth(int normalWidth, int selectedWidth) {
        if (getIndicatorConfig() != null) {
            getIndicatorConfig().setNormalWidth(normalWidth);
            getIndicatorConfig().setSelectedWidth(selectedWidth);
        }
    }

    public void setIndicatorNormalWidth(int normalWidth) {
        if (getIndicatorConfig() != null) {
            getIndicatorConfig().setNormalWidth(normalWidth);
        }
    }

    public void setIndicatorSelectedWidth(int selectedWidth) {
        if (getIndicatorConfig() != null) {
            getIndicatorConfig().setSelectedWidth(selectedWidth);
        }
    }

    /**
     * viewpager2 切换监听
     */
    class ViewPager2OnPageChangeCallback extends ViewPager2.OnPageChangeCallback {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (getIndicator() != null && position == getCurrentItem() - 1) {
                getIndicator().onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        }

        @Override
        public void onPageSelected(int position) {
            if (getIndicator() != null) {
                getIndicator().onPageSelected(position);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    /**
     * 监听viewpager2 变化
     */
    private final RecyclerView.AdapterDataObserver mAdapterDataObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            setIndicatorPageChange();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            setIndicatorPageChange();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, @Nullable Object payload) {
            setIndicatorPageChange();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            setIndicatorPageChange();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            setIndicatorPageChange();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            setIndicatorPageChange();
        }
    };

}
