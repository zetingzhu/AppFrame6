package com.zzt.testdialog22;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: zeting
 * @date: 2021/11/21
 */
public class DialogUtil {
    public void showDialog01(Context context) {
        Dialog dialog = new Dialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_dialog_t1, null, false);
        LinearLayout ll_add_view = view.findViewById(R.id.ll_add_view);
        dialog.setContentView(view);


        TextView textView = new TextView(context);
        textView.setText("qqqqqqqqqqqqqqqqqq");
        textView.setTextSize(30);
        textView.setTextColor(Color.parseColor("#000000"));

        ll_add_view.addView(textView);

        dialog.show();
    }


    public void showDialog02(Activity context) {
        Dialog dialog = new Dialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_dialog_t1, null, false);
        LinearLayout ll_add_view = view.findViewById(R.id.ll_add_view);
        dialog.setContentView(view);


        TextView textView = new TextView(context);
        textView.setText("qqqqqqqqqqqqqqqqqq");
        textView.setTextSize(30);
        textView.setTextColor(Color.parseColor("#000000"));
        ll_add_view.addView(textView);


        LinearLayout rootLayout = new LinearLayout(context);
        rootLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        rootLayout.setBackgroundColor(backgroundColor);
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        rootLayout.setGravity(Gravity.CENTER);
        rootLayout.setPadding(0, 0, 0, 0);
        rootLayout.setClipToPadding(false);

        WheelView1 yearView = new WheelView1(context);
        yearView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        yearView.setTextSize(40);
        yearView.setTextColor(Color.parseColor("#000000"));
        yearView.setLineVisible(true);
        yearView.setLineColor(WheelView1.LINE_COLOR);
        yearView.setOffset(WheelView1.OFF_SET);
        yearView.setCycleDisable(false);
        List<String> years = new ArrayList<>();
        years.add("1990");
        years.add("1991");
        years.add("1992");
        years.add("1993");
        years.add("1994");
        years.add("1995");
        years.add("1996");
        years.add("1997");
        yearView.setItems(years);

        rootLayout.removeAllViews();

        rootLayout.addView(yearView);
        ll_add_view.addView(rootLayout);

        dialog.show();
    }
}
