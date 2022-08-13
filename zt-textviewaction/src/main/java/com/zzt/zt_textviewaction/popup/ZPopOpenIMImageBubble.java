package com.zzt.zt_textviewaction.popup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.zzt.zt_textviewaction.R;
import com.zzt.zt_textviewaction.selectV1.SelectActionListener;


/**
 * @author: zeting
 * @date: 2022/7/28
 * 长按图片给出的引导
 */
public class ZPopOpenIMImageBubble extends ZNormalPopup {
    public ZPopOpenIMImageBubble(Context context, @ZDirection int preferredDirection, SelectActionListener mSelectListener) {
        super(context, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        initView(context, preferredDirection, mSelectListener);
    }


    public void initView(Context context, @ZDirection int preferredDirection, SelectActionListener mSelectListener) {

        View contentView = LayoutInflater.from(context).inflate(R.layout.open_im_message_select_action_layout, null);
        contentView.findViewById(R.id.iv_bottom_triangle).setVisibility(View.VISIBLE);
        contentView.findViewById(R.id.tv_copy).setEnabled(false);
        contentView.findViewById(R.id.tv_reply).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectListener != null) {
                    mSelectListener.onTextSelectedReply("");
                }
                dismiss();
            }
        });

        view(contentView);
//        bgColor(ContextCompat.getColor(context, R.color.color_4C4C4C));
        radius(context.getResources().getDimensionPixelOffset(R.dimen.margin_4dp));
        arrow(false);
        arrowSize(context.getResources().getDimensionPixelOffset(R.dimen.margin_16dp), context.getResources().getDimensionPixelOffset(R.dimen.margin_10dp));
        preferredDirection(preferredDirection);
    }

    @Override
    public void show(@NonNull View anchor) {
        super.show(anchor);
    }
}
