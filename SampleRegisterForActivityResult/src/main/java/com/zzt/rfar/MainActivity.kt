package com.zzt.rfar

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.widget.Button
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.zzt.adapter.StartActivityRecyclerAdapter
import com.zzt.entity.StartActivityDao
import java.io.File

class MainActivity : AppCompatActivity() {
    val TAG = MainActivity::class.java.simpleName

    var rv_list: RecyclerView? = null
    var daoList: MutableList<StartActivityDao> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    /**
     *
    StartIntentSenderForResult                                                                                 google支付
    RequestMultiplePermissions                                                                                 多个权限请求
    RequestPermission                                                                                 单个权限请求
    TakePicturePreview                                                                                 拍照预览
    TakePicture                                                                                 拍照
    TakeVideo                                                                                 摄像
    PickContact                                                                                 选择联系人
    GetContent                                                                                 获取各种文件的Uri
    GetMultipleContents                                                                                 获取多个各种文件的Uri
    OpenDocument                                                                                 打开文件
    OpenMultipleDocuments                                                                                 打开多个文件
    OpenDocumentTree                                                                                 打开文件夹
    CreateDocument                                                                                 创建文件
     */
    private fun initView() {
        rv_list = findViewById(R.id.rv_list)

        daoList.add(StartActivityDao("应用上方权限申请", "", "1"))
        daoList.add(StartActivityDao("申请权限组", "", "2"))
        daoList.add(StartActivityDao("申请单个权限", "", "3"))
        daoList.add(StartActivityDao("拍照", "", "4"))
        daoList.add(StartActivityDao("拍照返回bitmap", "", "41"))
        daoList.add(StartActivityDao("文件选择", "", "5"))
        daoList.add(StartActivityDao("混合选择器", "", "6"))
        daoList.add(StartActivityDao("跳转其他Activity", "", "7"))
        daoList.add(StartActivityDao("选择联系人", "PickContact", "8"))
        daoList.add(StartActivityDao("打开文件夹", "OpenDocumentTree", "9"))
        StartActivityRecyclerAdapter.setAdapterData(
            rv_list,
            RecyclerView.VERTICAL,
            daoList
        ) { itemView, position, data ->
            if (data is StartActivityDao) {
                when (data.arouter) {
                    "1" -> btnV1()
                    "2" -> btnV2()
                    "3" -> btnV3()
                    "4" -> btnV4()
                    "41" -> btnV41()
                    "5" -> btnV5()
                    "6" -> btnV6()
                    "7" -> btnV7()
                    "8" -> btnV8()
                    "9" -> btnV9()
                }
            }
        }
    }

    private val result9: ActivityResultLauncher<Uri> =
        registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) {
            Log.d(TAG, "打开文件夹: ${it}")
        }

    private fun btnV9() {
        result9.launch(null)

    }

    val result8: ActivityResultLauncher<Void> =
        registerForActivityResult(ActivityResultContracts.PickContact()) {
            Log.d(TAG, "选择联系人:${it}")
        }

    private fun btnV8() {
        result8.launch(null)
    }

    var result71 =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            Log.d(TAG, "接收值： ${result?.resultCode}  ${result?.data?.getStringExtra("EEE")}")
        }

    private fun btnV7() {
        var intent = Intent()
        intent.setClass(this@MainActivity, SettingTestAct::class.java)
        intent.putExtra("ddd", "传了个什么过去了")
        result71.launch(intent)
    }

    private fun btnV6() {
        result7.launch(arrayOf("image/*", "text/plain"))
    }

    private fun btnV5() {
        result6.launch("text/plain")
    }

    private fun btnV41() {
        result5.launch(null)
    }

    private fun btnV4() {
        val file = File(filesDir, "picFromCamera")
        val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val values = ContentValues()
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, "图片名称.jpg")
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        } else {
            FileProvider.getUriForFile(
                this,
                applicationContext.packageName + ".fileProvider",
                file
            )
        }
        Log.d(TAG, "路径：" + uri?.path)
        result3.launch(uri)
    }

    private fun btnV3() {
        result4.launch(Manifest.permission.WRITE_SECURE_SETTINGS)
    }

    private fun btnV2() {
        result2.launch(
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.WRITE_SECURE_SETTINGS
            )
        )
    }

    private fun btnV1() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
            intent.data = Uri.parse("package:${packageName}")
            result1.launch(intent)
        }
    }

    val result1 = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        Log.d(TAG, "悬浮窗申请 $it")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.d(TAG, "获取悬浮窗状态" + Settings.canDrawOverlays(this@MainActivity))
        }
    }
    val result2 = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
        Log.d(TAG, "返回结果 $it")
    }
    val result3 = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        Log.d(TAG, "返回结果 $it")
    }
    val result5 = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) {
        Log.d(TAG, "返回结果 $it")
    }
    val result4 = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        Log.d(TAG, "返回结果 $it")
    }
    val result6 = registerForActivityResult(ActivityResultContracts.GetContent()) {
        Log.d(TAG, "文件：$it")
    }
    val result7 = registerForActivityResult(ActivityResultContracts.OpenDocument()) {
        Log.d(TAG, "多类型：$it")
    }

}