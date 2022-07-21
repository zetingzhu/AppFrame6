package com.zzt.merge

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class FooterAdapter(var listData: ArrayList<String>)                                                                                 RecyclerView.Adapter<MViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MViewHolder {
        var view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_list_text, parent, false)
        return MViewHolder(view)
    }

    override fun onBindViewHolder(holder: MViewHolder, position: Int) {
        holder.title.apply {
            setBackgroundResource(R.color.black)
            setTextColor(ContextCompat.getColor(this.context, R.color.white))
            text = listData[position]
        }
    }

    override fun getItemCount(): Int {
        return listData.size
    }


}
