package com.zzt.testdialog22

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.zzt.adapter.StartActivityRecyclerAdapter
import com.zzt.entity.StartActivityDao
import com.zzt.testdialog22.ww.DatePicker
import java.util.*

class MainActivity : AppCompatActivity() {
    var rv_data: RecyclerView? = null
    var diglogUtil: DialogUtil? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        rv_data = findViewById(R.id.rv_data)
        diglogUtil = DialogUtil()
        var daoList: MutableList<StartActivityDao> = mutableListOf()

        daoList.add(StartActivityDao("弹一个添加框", "", "1"))
        daoList.add(StartActivityDao("弹二个添加框", "", "2"))
        daoList.add(StartActivityDao("弹三个添加框", "", "3"))
        StartActivityRecyclerAdapter.setAdapterData(
            rv_data,
            RecyclerView.VERTICAL,
            daoList
        ) { itemView, position, data ->
            if (data is StartActivityDao) {
                when (data.arouter) {
                    "1" -> diglogUtil?.showDialog01(this@MainActivity)
                    "2" -> diglogUtil?.showDialog02(this@MainActivity)
                    "3" -> text3()
                }
            }
        }
    }

    private fun text3() {

        var datePicker = DatePicker(this@MainActivity)
        datePicker?.setLabel("", "", "")
        val date = Date()
        val year = date.year + 1900
        val month = date.month + 1
        datePicker?.setRangeStart(year - 100, month, date.date)
        datePicker?.setRangeEnd(year - 18, month, date.date)
        datePicker?.setSelectedItem(year - 18, month, date.date)

        datePicker?.show()
    }

}