package com.zzt.banvp.util;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: zeting
 * @date: 2022/1/6
 * 循环滑动适配器
 */
public abstract class BannerAdapterBase<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    // 默认增加的左右数量
    public static final int INCREASE_COUNT = 2;
    // 展示数据
    protected List<T> mDatas = new ArrayList<>();
    // ViewHolder
    private VH mViewHolder;
    // 默认增加数量，2为可以左右循环滑动
    private int mIncreaseCount = INCREASE_COUNT;

    public BannerAdapterBase(List<T> datas) {
        setDatas(datas);
    }

    /**
     * 设置实体集合（可以在自己的adapter自定义，不一定非要使用）
     *
     * @param datas
     */
    public void setDatas(List<T> datas) {
        if (datas == null) {
            datas = new ArrayList<>();
        }
        mDatas = datas;
        notifyDataSetChanged();
    }

    /**
     * 获取指定的实体（可以在自己的adapter自定义，不一定非要使用）
     *
     * @param position 真实的position
     * @return
     */
    public T getData(int position) {
        return mDatas.get(position);
    }

    /**
     * 获取指定的实体（可以在自己的adapter自定义，不一定非要使用）
     *
     * @param position 这里传的position不是真实的，获取时转换了一次
     * @return
     */
    public T getRealData(int position) {
        return mDatas.get(getRealPosition(position));
    }


    @Override
    public int getItemCount() {
        return getRealCount() > 1 ? getRealCount() + mIncreaseCount : getRealCount();
    }

    /**
     * 数据真实数据个数
     *
     * @return
     */
    public int getRealCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    /**
     * 获取真实数据位置
     *
     * @param position 当前适配器位置
     * @return
     */
    public int getRealPosition(int position) {
        return getRealPosition(mIncreaseCount == INCREASE_COUNT, position, getRealCount());
    }

    public VH getViewHolder() {
        return mViewHolder;
    }

    public void setIncreaseCount(int increaseCount) {
        this.mIncreaseCount = increaseCount;
    }

    /**
     * 获取真正的位置
     *
     * @param isIncrease 首尾是否有增加
     * @param position   当前位置
     * @param realCount  真实数量
     * @return
     */
    public static int getRealPosition(boolean isIncrease, int position, int realCount) {
        if (!isIncrease) {
            return position;
        }
        int realPosition;
        if (position == 0) {
            realPosition = realCount - 1;
        } else if (position == realCount + 1) {
            realPosition = 0;
        } else {
            realPosition = position - 1;
        }
        return realPosition;
    }
}