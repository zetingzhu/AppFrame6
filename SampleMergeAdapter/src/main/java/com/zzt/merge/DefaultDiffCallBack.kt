package com.zzt.merge

import androidx.recyclerview.widget.DiffUtil

/**
 * @author: zeting
 * @date: 2021/7/23
 * recycleview 局部刷新
 */
class DefaultDiffCallBack(
    private val mOldDatas: MutableList<String>?,
    private val mNewDatas: MutableList<String>?
)                                                                                 DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return mOldDatas?.size ?: 0
    }

    override fun getNewListSize(): Int {
        return mNewDatas?.size ?: 0
    }

    /**
     * 被DiffUtil调用，用来判断 两个对象是否是相同的Item
     */
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldDatas!![oldItemPosition] == mNewDatas!![newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldDatas?.get(oldItemPosition) == mNewDatas?.get(newItemPosition)
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }
}