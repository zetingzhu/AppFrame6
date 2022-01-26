package com.zzt.banvp;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author: zeting
 * @date: 2020/1/10
 */
public class MyVH extends RecyclerView.ViewHolder {
    ImageView iv_banner_img;

    public MyVH(@NonNull View itemView) {
        super(itemView);
        iv_banner_img = itemView.findViewById(R.id.iv_banner_img);
    }
}
