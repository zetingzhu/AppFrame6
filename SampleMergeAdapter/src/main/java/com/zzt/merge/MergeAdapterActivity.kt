package com.zzt.merge

import android.content.Intent
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
class MergeAdapterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMergeAdapterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView(binding)
    }

    private fun initView(binding: ActivityMergeAdapterBinding) {
        // 1. 定义Config
        val config = ConcatAdapter.Config.Builder()
            .setIsolateViewTypes(true)
            .setStableIdMode(ConcatAdapter.Config.StableIdMode.SHARED_STABLE_IDS)
            .build()
        val adapter = ConcatAdapter(config)
        // 2. 使用ConcatAdapter将三个Adapter组合起来。
        adapter.addAdapter(HeaderAdapter(generateList("Header", 2)).apply {
            setHasStableIds(true)
        })
        val contentAd = ContentAdapter(
            generateList(
                "Content",
                2
            )
        ).apply {
            setHasStableIds(true)
        }
        adapter.addAdapter(contentAd)
        adapter.addAdapter(FooterAdapter(generateList("Footer", 2)).apply {
            setHasStableIds(true)
        })
        binding.rvData.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.rvData.adapter = adapter
        binding.btnR.setOnClickListener {
            var random: Random = Random()
            contentAd.refreshListData(generateList("刷新数据", random.nextInt(30)))
        }
        binding.btnDiff.setOnClickListener {
            startActivity(Intent(this@MergeAdapterActivity, DiffAdapterActivity::class.java))
        }
    }


    private fun generateList(title: String, count: Int) = ArrayList<String>().apply {
        for (index in 0 until count) {
            add("$title position = $index")
        }
    }
}