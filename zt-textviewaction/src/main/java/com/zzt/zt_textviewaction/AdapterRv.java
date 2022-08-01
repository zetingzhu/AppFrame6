package com.zzt.zt_textviewaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zzt.zt_textviewaction.util.SelectableTextHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: zeting
 * @date: 2022/8/1
 */
public class AdapterRv extends RecyclerView.Adapter<AdapterRv.MYh> {
    private static final String TAG = AdapterRv.class.getSimpleName();
    List<String> mList;

    public AdapterRv() {
        this.mList = new ArrayList<>();
        mList.add("2222222222222222222222222fdfdsfdad");
        mList.add("22222222222222fdaf22222222222fdfdsfdad");
        mList.add("fdafdaf");
        mList.add("2222222222222222222222222fdfdsfdad");
        mList.add("22222fdaf222222222222fdasf22222222fdfdsfdad");
        mList.add("2222222222222222222222222fdfdsfdad");
        mList.add("2222222222222fdaf222222222222fdfdsfdad");
        mList.add("2222222222222222222222222fdfdsfdad");
        mList.add("2222222222222fdafds222222222222fdfdsfdad");
        mList.add("2222222222222222222222222fdfdsfdad");
        mList.add("22222222222fdafd22222222222222fdfdsfdad");
        mList.add("222222fdafd2222222222222222222fdfdsfdad");
        mList.add("2222222fdadf222222222222222222fdfdsfdad");
        mList.add("fdafdaf");
        mList.add("2222222222222222222222222fdfdsfdad");
        mList.add("22222fdaf222222222222fdasf22222222fdfdsfdad");
        mList.add("2222222222222222222222222fdfdsfdad");
        mList.add("2222222222222fdaf222222222222fdfdsfdad");
        mList.add("2222222222222222222222222fdfdsfdad");
        mList.add("2222222222222fdafds222222222222fdfdsfdad");
        mList.add("2222222222222222222222222fdfdsfdad");
        mList.add("22222222222fdafd22222222222222fdfdsfdad");
        mList.add("222222fdafd2222222222222222222fdfdsfdad");
        mList.add("2222222fdadf222222222222222222fdfdsfdad");

    }

    @NonNull
    @Override
    public MYh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_text, parent, false);
        return new MYh(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull MYh holder, int position) {
        holder.tv_title.setText(mList.get(holder.getBindingAdapterPosition()));

        new SelectableTextHelper.Builder(holder.tv_title)
                .setSelectedColor(holder.tv_title.getResources().getColor(R.color.color_ffff00))
                .setCursorHandleSizeInDp(20)
                .setCursorHandleColor(holder.tv_title.getResources().getColor(R.color.color_3D56FF))
                .addSelectActionListener(new com.zzt.zt_textviewaction.util.SelectActionListener() {
                    @Override
                    public void onTextSelected(CharSequence content) {

                    }

                    @Override
                    public void onTextSelectedCopy(CharSequence content) {
                        Log.e("测试", "复制的数据: " + content);
//                        TextUtil.copyText(tv_open_message, content.toString());
                    }

                    @Override
                    public void onTextSelectedReply(CharSequence content) {
                        Log.d(TAG, "点击了回复 选中内容：" + content);

                    }

                    @Override
                    public void onTextSelectedDownload(CharSequence content) {

                    }
                })
                .build();
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class MYh extends RecyclerView.ViewHolder {
        private TextView tv_title;

        public MYh(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
        }
    }
}
