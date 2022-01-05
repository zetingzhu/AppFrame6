package com.zzt.sampleflow

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.zzt.adapter.StartActivityRecyclerAdapter
import com.zzt.entity.StartActivityDao
import com.zzt.sampleflow.databinding.ActivityFlowBinding

class FlowActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFlowBinding
    var daoList: MutableList<StartActivityDao> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFlowBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
    }

    private fun initData() {
        daoList.add(StartActivityDao("", "", "1"))
        StartActivityRecyclerAdapter.setAdapterData(
            binding.rvData,
            RecyclerView.VERTICAL,
            daoList
        ) { position, data ->
            if (data is StartActivityDao) {
                when (data.arouter) {
                    "1" -> {

                    }
                }
            }
        }
    }


}