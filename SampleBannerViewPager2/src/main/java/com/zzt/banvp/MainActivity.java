package com.zzt.banvp;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.zzt.banvp.entiy.ImgObj;
import com.zzt.banvp.util.CircleIndicator;
import com.zzt.banvp.util.ViewPager2BannerManager;
import com.zzt.banvp.util.ViewPager2IndicatorManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    ViewPager2 vp_banner1, vp_banner2;
    CircleIndicator ci_banner1, ci_banner2;

    Integer[] arrayIds = {R.drawable.sign_blue_peach_big, R.drawable.sign_red_peach_big, R.drawable.sign_yellow_peach_small};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        vp_banner1 = findViewById(R.id.vp_banner1);
        vp_banner2 = findViewById(R.id.vp_banner2);
        ci_banner1 = findViewById(R.id.ci_banner1);
        ci_banner2 = findViewById(R.id.ci_banner2);

        RecyclerView.Adapter adapter2 = new RecyclerView.Adapter<MyVH>() {
            @NonNull
            @Override
            public MyVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vp_img, parent, false);
                MyVH myVH = new MyVH(view);
                return myVH;
            }

            @Override
            public void onBindViewHolder(@NonNull MyVH holder, int position) {
                holder.iv_banner_img.setImageResource(arrayIds[position]);
            }

            @Override
            public int getItemCount() {
                return arrayIds.length;
            }
        };
        vp_banner1.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        vp_banner1.setAdapter(adapter2);
        ViewPager2IndicatorManager vim1 = new ViewPager2IndicatorManager(vp_banner1, ci_banner1);
        vim1.setIndicatorNormalColor(Color.parseColor("#222222"));
        vim1.setIndicatorSelectedColor(Color.parseColor("#AAAAAA"));
        vim1.attach();


        List<ImgObj> ints = new ArrayList<>();
        for (int i = 0; i < arrayIds.length; i++) {
            ints.add(new ImgObj(arrayIds[i], "第" + (i + 1) + "个"));
        }
        ImgAdapter imgAdapter = new ImgAdapter(null);

        ViewPager2BannerManager vpbm = new ViewPager2BannerManager(vp_banner2, imgAdapter);
        vpbm.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Log.e(TAG, "滑动 -onPageSelected- ：" + position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
        vpbm.attach();
        ViewPager2IndicatorManager vim2 = new ViewPager2IndicatorManager(vp_banner2, ci_banner2);
        vim2.setBannerManager(vpbm);
        vim2.setIndicatorNormalColor(Color.parseColor("#DFDFDE"));
        vim2.setIndicatorSelectedColor(Color.parseColor("#FF0000"));
        vim2.attach();

        vp_banner2.postDelayed(new Runnable() {
            @Override
            public void run() {
                imgAdapter.setDatas(ints);
                vpbm.setDataAfter();

            }
        }, 1000 * 3);

    }
}