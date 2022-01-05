package com.zzt.merge

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * @author: zeting
 * @date: 2021/11/17
 *
 */
class MViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var title = itemView.findViewById<TextView>(R.id.tv_title)

}