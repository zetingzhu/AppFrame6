package com.zzt.merge

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class DifffAdapter(var listData: MutableList<String>) :
    RecyclerView.Adapter<DifffAdapter.DiffViewHolder>() {
    val TAG = DifffAdapter::class.java.simpleName

    fun refreshListData(newList: MutableList<String>?) {
        Log.d(TAG, "更新了多少条数据：" + newList?.size + "\n " + newList)
        var diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(
            DefaultDiffCallBack(
                listData,
                newList
            )
        );
        listData.clear()
        newList?.let {
            listData.addAll(it)
        }
        diffResult.dispatchUpdatesTo(this)
        Log.w(TAG, "更新后的数据 " + listData)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiffViewHolder {
        var view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_list_text, parent, false)
        return DiffViewHolder(view)
    }

    override fun onBindViewHolder(holder: DiffViewHolder, position: Int) {
        holder.title.apply {
            setBackgroundResource(android.R.color.holo_red_dark)
            setTextColor(ContextCompat.getColor(this.context, R.color.white))
            text = listData[position]
        }
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    class DiffViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById<TextView>(R.id.tv_title)
    }
}
