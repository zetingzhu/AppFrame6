package com.zzt.merge

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.zzt.merge.databinding.ItemListTextBinding

class HeaderAdapter(var listData: MutableList<String>) : RecyclerView.Adapter<BindingViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        val inflate =
            ItemListTextBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return BindingViewHolder(inflate)
    }

    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {
        holder.setData(listData.get(position))
    }

    override fun getItemCount(): Int {
        return listData.size
    }

}
