package com.zzt.banvp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;


import com.zzt.banvp.util.BannerAdapterBase;

import java.util.List;

/**
 * @author: zeting
 * @date: 2022/1/5
 */
public class ImgAdapter extends BannerAdapterBase<Integer, MyVH> {

    public ImgAdapter(List<Integer> datas) {
        super(datas);
    }

    @Override
    public void setDatas(List<Integer> datas) {
        super.setDatas(datas);
    }

    @NonNull
    @Override
    public MyVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vp_img, parent, false);
        return new MyVH(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyVH holder, int position) {
        int bindingAdapterPosition = holder.getBindingAdapterPosition();
        int realPosition = getRealPosition(bindingAdapterPosition);
        Integer realData = getRealData(bindingAdapterPosition);
        holder.iv_banner_img.setImageResource(realData);

    }

}
