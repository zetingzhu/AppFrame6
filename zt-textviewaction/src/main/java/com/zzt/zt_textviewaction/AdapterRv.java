package com.zzt.zt_textviewaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zzt.zt_textviewaction.selectv2.SelectableTextHelper;
import com.zzt.zt_textviewaction.selectv3.ActionMenu;
import com.zzt.zt_textviewaction.selectv3.CustomActionMenuCallBack;
import com.zzt.zt_textviewaction.selectv3.SelectableTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: zeting
 * @date: 2022/8/1
 */
public class AdapterRv extends RecyclerView.Adapter<AdapterRv.MYh> {
    private static final String TAG = "TextHelper AdapterRv";
    List<String> mList;
    private SelectableTextHelper selectableTextHelper;
    private int mTouchX = 0;
    private int mTouchY = 0;

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
        View operateView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_select_text_operate, null);
        selectableTextHelper = new SelectableTextHelper(operateView, R.drawable.select_text_view_arrow);
        return new MYh(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull MYh holder, int position) {
        holder.tv_title.setText(mList.get(holder.getBindingAdapterPosition()));

//        new SelectableTextHelper.Builder(holder.tv_title)
//                .setSelectedColor(holder.tv_title.getResources().getColor(R.color.color_ffff00))
//                .setCursorHandleSizeInDp(20)
//                .setCursorHandleColor(holder.tv_title.getResources().getColor(R.color.color_3D56FF))
//                .addSelectActionListener(new com.zzt.zt_textviewaction.util.SelectActionListener() {
//                    @Override
//                    public void onTextSelected(CharSequence content) {
//
//                    }
//
//                    @Override
//                    public void onTextSelectedCopy(CharSequence content) {
//                        Log.e("测试", "复制的数据: " + content);
////                        TextUtil.copyText(tv_open_message, content.toString());
//                    }
//
//                    @Override
//                    public void onTextSelectedReply(CharSequence content) {
//                        Log.d(TAG, "点击了回复 选中内容：" + content);
//
//                    }
//
//                    @Override
//                    public void onTextSelectedDownload(CharSequence content) {
//
//                    }
//                })
//                .build();


//        holder.tv_title.setText(mList.get(holder.getBindingAdapterPosition()));
//        holder.tv_title.clearFocus();
////        holder.tv_title.setForbiddenActionMenu(false);
//        holder.tv_title.setCustomActionMenuCallBack(new CustomActionMenuCallBack() {
//            @Override
//            public boolean onCreateCustomActionMenu(ActionMenu menu) {
//                return false;
//            }
//
//            @Override
//            public void onCustomActionItemClicked(String itemTitle, String selectedContent) {
//
//            }
//        });


        holder.tv_title.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mTouchX = (int) event.getX();
                mTouchY = (int) event.getY();
                return false;
            }
        });
        holder.tv_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (selectableTextHelper != null) {
//                    Log.w(TAG, "列表点击");
//                    selectableTextHelper.resetSelectionInfo();
//                    selectableTextHelper.hideSelectView();
//                }
            }
        });
        holder.tv_title.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (selectableTextHelper != null) {
                    selectableTextHelper.resetSelectionInfo();
                    selectableTextHelper.hideSelectView();
                    Log.d(TAG, "长按：" + mTouchX + "-" + mTouchY + "   =  " + v.getX() + "-" + v.getY());
                    selectableTextHelper.showSelectView(holder.tv_title, mTouchX, mTouchY);
                }
                return false;
            }
        });


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class MYh extends RecyclerView.ViewHolder {
        //        private SelectableTextView tv_title;
        private TextView tv_title;

        public MYh(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
        }
    }
}
