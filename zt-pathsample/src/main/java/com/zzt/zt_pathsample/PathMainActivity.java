package com.zzt.zt_pathsample;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class PathMainActivity extends AppCompatActivity {
    public static final String TAG = "PathMainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path_main);

        setLogString("获取根路径 : ", PathUtils.getRootPath());
        setLogString("获取数据路径  : ", PathUtils.getDataPath());
        setLogString("获取下载缓存路径 : ", PathUtils.getDownloadCachePath());
        setLogString("获取内存应用数据路径 : ", PathUtils.getInternalAppDataPath());
        setLogString("获取内存应用代码缓存路径 : ", PathUtils.getInternalAppCodeCacheDir());
        setLogString("获取内存应用缓存路径  : ", PathUtils.getInternalAppCachePath());
        setLogString("获取内存应用数据库路径 : ", PathUtils.getInternalAppDbsPath());
//        setLogString("获取内存应用数据库路径  : ", PathUtils.getInternalAppDbPath());
        setLogString("获取内存应用文件路径  : ", PathUtils.getInternalAppFilesPath());
        setLogString("获取内存应用 SP 路径 : ", PathUtils.getInternalAppSpPath());
        setLogString("获取内存应用未备份文件路径 : ", PathUtils.getInternalAppNoBackupFilesPath());
        setLogString("获取外存路径 : ", PathUtils.getExternalStoragePath());
        setLogString("获取外存音乐路径 : ", PathUtils.getExternalMusicPath());
        setLogString("获取外存播客路径  : ", PathUtils.getExternalPodcastsPath());
        setLogString("获取外存铃声路径 : ", PathUtils.getExternalRingtonesPath());
        setLogString("获取外存闹铃路径  : ", PathUtils.getExternalAlarmsPath());
        setLogString("获取外存通知路径 : ", PathUtils.getExternalNotificationsPath());
        setLogString("获取外存图片路径  : ", PathUtils.getExternalPicturesPath());
        setLogString("获取外存影片路径  : ", PathUtils.getExternalMoviesPath());
        setLogString("获取外存下载路径 : ", PathUtils.getExternalDownloadsPath());
        setLogString("获取外存数码相机图片路径  : ", PathUtils.getExternalDcimPath());
        setLogString("获取外存文档路径 : ", PathUtils.getExternalDocumentsPath());
        setLogString("获取外存应用数据路径 : ", PathUtils.getExternalAppDataPath());
        setLogString("获取外存应用缓存路径  : ", PathUtils.getExternalAppCachePath());
        setLogString("获取外存应用文件路径  : ", PathUtils.getExternalAppFilesPath());
        setLogString("获取外存应用音乐路径  : ", PathUtils.getExternalAppMusicPath());
        setLogString("获取外存应用播客路径 : ", PathUtils.getExternalAppPodcastsPath());
        setLogString("获取外存应用铃声路径  : ", PathUtils.getExternalAppRingtonesPath());
        setLogString("获取外存应用闹铃路径 : ", PathUtils.getExternalAppAlarmsPath());
        setLogString("获取外存应用通知路径 : ", PathUtils.getExternalAppNotificationsPath());
        setLogString("获取外存应用图片路径 : ", PathUtils.getExternalAppPicturesPath());
        setLogString("获取外存应用影片路径 : ", PathUtils.getExternalAppMoviesPath());
        setLogString("获取外存应用下载路径 : ", PathUtils.getExternalAppDownloadPath());
        setLogString("获取外存应用数码相机图片路径 : ", PathUtils.getExternalAppDcimPath());
        setLogString("获取外存应用文档路径  : ", PathUtils.getExternalAppDocumentsPath());
        setLogString("获取外存应用 OBB 路径 : ", PathUtils.getExternalAppObbPath());
        setLogString("优先获取外部根路径  : ", PathUtils.getRootPathExternalFirst());
        setLogString("优先获取外部数据路径  : ", PathUtils.getAppDataPathExternalFirst());
        setLogString("优先获取外部文件路径  : ", PathUtils.getFilesPathExternalFirst());
        setLogString("优先获取外部缓存路径  : ", PathUtils.getCachePathExternalFirst());
        setLogString("返回 SDCard 路径", SDCardUtils.getSDCardPathByEnvironment());

        testFile1();
        testFile2();
    }

    private void testFile2() {
//        if (!checkPermission()) {
//            requestPermission();
//        } else {
        String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/text1.txt";
        setLogString("testFile2  1  ", filePath);
        File file = new File(filePath);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            setLogString("testFile2  2 ", filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        }
    }

    private void testFile1() {
        String downloadsPath = PathUtils.getExternalDownloadsPath();
        File file = new File(downloadsPath + File.separator + "abc");
        Log.e(TAG, "file:" + file);
        boolean directory = file.isDirectory();
        Log.e(TAG, "directory:" + directory);
        if (!directory) {
            boolean mkdirs = file.mkdirs();
            Log.e(TAG, "mkdirs:" + mkdirs);
        }
        File fileText = new File(file.getAbsolutePath() + "/abcd.txt");
        Log.e(TAG, "file:" + fileText);
        boolean exists = fileText.exists();
        Log.e(TAG, "exists:" + exists);
        if (!exists) {
            try {
                boolean newFile = fileText.createNewFile();
                Log.e(TAG, "newFile:" + newFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void setLogString(String lab, String message) {
        Log.d(TAG, lab + " : " + message);
    }

    //读写权限
    private static String[] PERMISSIONS_STORAGE = {
            READ_EXTERNAL_STORAGE,
            WRITE_EXTERNAL_STORAGE};
    //请求状态码
    private static int PERMISSION_REQUEST_CODE = 1111;


    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            int result = ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE);
            int result1 = ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s", getApplicationContext().getPackageName())));
                startActivityForResult(intent, PERMISSION_REQUEST_CODE);
            } catch (Exception e) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult(intent, PERMISSION_REQUEST_CODE);
            }
        } else {
            //below android 11
            ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                Log.i("MainActivity", "申请的权限为：" + permissions[i] + ",申请结果：" + grantResults[i]);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    // perform action when allow permission success
                } else {
                    Toast.makeText(this, "Allow permission for storage access!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}