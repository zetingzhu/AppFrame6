package com.zzt.merge

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zzt.merge.databinding.ActivityMergeAdapterBinding
import java.util.*
import kotlin.collections.ArrayList

/**
 *
 * ConcatAdapter
 */
class DiffAdapterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMergeAdapterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView(binding)
    }

    private fun initView(binding: ActivityMergeAdapterBinding) {

        val contentAd = DifffAdapter(
            generateList(
                "Content",
                2
            )
        )
        binding.rvData.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.rvData.adapter = contentAd

        binding.btnR.setOnClickListener {
            var random: Random = Random()
            contentAd.refreshListData(generateList("刷新数据", random.nextInt(30)))
        }
    }


    private fun generateList(title: String, count: Int) = ArrayList<String>().apply {
        for (index in 0 until count) {
            add("$title position = $index")
        }
    }
}