package com.zzt.merge

import android.renderscript.ScriptGroup
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.zzt.merge.databinding.ItemListTextBinding

/**
 * @author: zeting
 * @date: 2021/11/17
 *
 */
class BindingViewHolder(var binding: ItemListTextBinding) : RecyclerView.ViewHolder(binding.root) {

    fun setData(title: String) {
        binding.tvTitle.apply {
            binding.tvTitle.setBackgroundResource(android.R.color.darker_gray)
            binding.tvTitle.setTextColor(ContextCompat.getColor(this.context, R.color.white))
            binding.tvTitle.text = title
        }
    }

}