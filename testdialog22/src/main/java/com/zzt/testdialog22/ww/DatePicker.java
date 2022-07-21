package com.zzt.testdialog22.ww;

import android.app.Activity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;


import com.zzt.testdialog22.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

/**
 * 日期选择器
 *
 * @author 李玉江[QQ :1032694760]
 * @since 2015/12/14
 */
public class DatePicker extends WheelPicker {
    public static final int YEAR_MONTH_DAY = 0;//年月日
    public static final int YEAR_MONTH = 1;//年月
    public static final int MONTH_DAY = 2;//月日
    private ArrayList<String> years = new ArrayList<String>();
    private ArrayList<String> months = new ArrayList<String>();
    private ArrayList<String> days = new ArrayList<String>();
    private OnWheelListener onWheelListener;
    private OnDatePickListener onDatePickListener;
    private String yearLabel = "Year", monthLabel = "Month", dayLabel = "Day";
    private int startYear = 2010, startMonth = 1, startDay = 1;
    private int endYear = 2020, endMonth = 12, endDay = 31;
    private int selectedYearIndex = 0, selectedMonthIndex = 0, selectedDayIndex = 0;
    private int mode = YEAR_MONTH_DAY;

    /**
     * 安卓开发应避免使用枚举类（enum），因为相比于静态常量enum会花费两倍以上的内存。
     * http://developer.android.com/training/articles/memory.html#Overhead
     */
    @IntDef(value = {YEAR_MONTH_DAY, YEAR_MONTH, MONTH_DAY})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Mode {
    }

    public DatePicker(Activity activity) {
        this(activity, YEAR_MONTH_DAY);
    }

    /**
     * @see #YEAR_MONTH_DAY
     * @see #YEAR_MONTH
     * @see #MONTH_DAY
     */
    public DatePicker(Activity activity, @Mode int mode) {
        super(activity);
        this.mode = mode;
    }

    /**
     * 设置年月日的单位
     */
    public void setLabel(String yearLabel, String monthLabel, String dayLabel) {
        this.yearLabel = yearLabel;
        this.monthLabel = monthLabel;
        this.dayLabel = dayLabel;
    }

    /**
     * 设置年份范围
     *
     * @deprecated use setRangeStart and setRangeEnd instead
     */
    @Deprecated
    public void setRange(int startYear, int endYear) {
        this.startYear = startYear;
        this.endYear = endYear;
        changeYearData();
    }

    /**
     * 设置范围：开始的年月日
     */
    public void setRangeStart(int startYear, int startMonth, int startDay) {
        this.startYear = startYear;
        this.startMonth = startMonth;
        this.startDay = startDay;
    }

    /**
     * 设置范围：结束的年月日
     */
    public void setRangeEnd(int endYear, int endMonth, int endDay) {
        this.endYear = endYear;
        this.endMonth = endMonth;
        this.endDay = endDay;
    }


    /**
     * 设置默认选中的年月日
     */
    public void setSelectedItem(int year, int month, int day) {
        changeYearData();
        changeMonthData(year);
        changeDayData(year, month);
        selectedYearIndex = findItemIndex(years, year);
        selectedMonthIndex = findItemIndex(months, month);
        selectedDayIndex = findItemIndex(days, day);
    }



    public void setOnDatePickListener(OnDatePickListener listener) {
        this.onDatePickListener = listener;
    }

    @Override
    @NonNull
    protected View makeCenterView() {
        if (months.size() == 0) {
            // 如果未设置默认项，则需要在此初始化数据
            int year = Calendar.getInstance(Locale.CHINA).get(Calendar.YEAR);
            changeYearData();
            changeDayData(year, changeMonthData(year));
        }

        textColorNormal = activity.getResources().getColor(R.color.color_172346);
        textColorFocus = activity.getResources().getColor(R.color.color_527DFF);
        lineColor = activity.getResources().getColor(R.color.color_dfe4f8);

//        FrameLayout frameLayout = new FrameLayout(activity);

//        LinearLayout layout = new LinearLayout(activity);
//        layout.setOrientation(LinearLayout.HORIZONTAL);
//        layout.setGravity(Gravity.CENTER);
//        layout.setBackgroundColor(activity.getResources().getColor(R.color.white));

        WheelView yearView = new WheelView(activity );
        yearView.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        yearView.setTextSize(textSize);
        yearView.setTextColor(textColorNormal, textColorFocus);
        yearView.setLineVisible(lineVisible);
        yearView.setLineColor(lineColor);
        yearView.setOffset(offset);
        yearView.setCycleDisable(cycleDisable);
//        TextView yearTextView = new TextView(activity);
//        yearTextView.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
//        yearTextView.setTextSize(textSize);
//        yearTextView.setTextColor(textColorFocus);
//        if (!TextUtils.isEmpty(yearLabel)) {
//            yearTextView.setText(yearLabel);
//        }
//        final WheelView monthView = new WheelView(activity.getBaseContext());
//        monthView.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
//        monthView.setTextSize(textSize);
//        monthView.setTextColor(textColorNormal, textColorFocus);
//        monthView.setLineVisible(lineVisible);
//        monthView.setLineColor(lineColor);
//        monthView.setOffset(offset);
//        monthView.setCycleDisable(cycleDisable);
//        TextView monthTextView = new TextView(activity);
//        monthTextView.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
//        monthTextView.setTextSize(textSize);
//        monthTextView.setTextColor(textColorFocus);
//        if (!TextUtils.isEmpty(monthLabel)) {
//            monthTextView.setText(monthLabel);
//        }
//        final WheelView dayView = new WheelView(activity.getBaseContext());
//        dayView.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
//        dayView.setTextSize(textSize);
//        dayView.setTextColor(textColorNormal, textColorFocus);
//        dayView.setLineVisible(lineVisible);
//        dayView.setLineColor(lineColor);
//        dayView.setOffset(offset);
//        dayView.setCycleDisable(cycleDisable);
//        TextView dayTextView = new TextView(activity);
//        dayTextView.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
//        dayTextView.setTextSize(textSize);
//        dayTextView.setTextColor(textColorFocus);
//        if (!TextUtils.isEmpty(dayLabel)) {
//            dayTextView.setText(dayLabel);
//        }
//        layout.addView(dayView);
//        layout.addView(dayTextView);
//        layout.addView(monthView);
//        layout.addView(monthTextView);
//        layout.addView(yearView);
//        layout.addView(yearTextView);
        /**
         * 获取屏幕宽度，来设置每一个选择滚轮的宽度
         */
//        DisplayMetrics dm = activity.getResources().getDisplayMetrics();
//        int disWidth = dm.widthPixels;
//        int yearViewWidth = (disWidth - activity.getResources().getDimensionPixelOffset(R.dimen.margin_16dp) * 2) / 3;
        // 年
//        LinearLayout.LayoutParams yp = (LinearLayout.LayoutParams) yearView.getLayoutParams();
//        yp.width = yearViewWidth;
//        yearView.setLayoutParams(yp);
        // 月
//        LinearLayout.LayoutParams mp = (LinearLayout.LayoutParams) monthView.getLayoutParams();
//        mp.width = yearViewWidth;
//        monthView.setLayoutParams(mp);
        // 日
//        LinearLayout.LayoutParams dp = (LinearLayout.LayoutParams) dayView.getLayoutParams();
//        dp.width = yearViewWidth;
//        dayView.setLayoutParams(dp);

        if (mode == YEAR_MONTH) {
            //年月模式，隐藏日子
//            dayView.setVisibility(View.GONE);
//            dayTextView.setVisibility(View.GONE);
        } else if (mode == MONTH_DAY) {
            //月日模式，隐藏年份
            yearView.setVisibility(View.GONE);
//            yearTextView.setVisibility(View.GONE);
        }
        if (mode != MONTH_DAY) {
//            if (!TextUtils.isEmpty(yearLabel)) {
//                yearTextView.setText(yearLabel);
//            }
            if (selectedYearIndex == 0) {
                yearView.setItems(years);
            } else {
                yearView.setItems(years, selectedYearIndex);
            }
            yearView.setOnWheelListener(new WheelView.OnWheelListener() {
                @Override
                public void onSelected(boolean isUserScroll, int index, String item) {
                    selectedYearIndex = index;
                    if (onWheelListener != null) {
                        onWheelListener.onYearWheeled(selectedYearIndex, item);
                    }
                    //需要根据年份及月份动态计算天数
                    int year = DateUtils.trimZero(item);
                    changeDayData(year, changeMonthData(year));
//                    monthView.setItems(DateUtils.magiskMonth(months), selectedMonthIndex);
//                    dayView.setItems(days, selectedDayIndex);
                }
            });
        }
//        if (!TextUtils.isEmpty(monthLabel)) {
//            monthTextView.setText(monthLabel);
//        }
//        if (selectedMonthIndex == 0) {
//            monthView.setItems(DateUtils.magiskMonth(months));
//        } else {
//            monthView.setItems(DateUtils.magiskMonth(months), selectedMonthIndex);
//        }
//        monthView.setOnWheelListener(new WheelView.OnWheelListener() {
//            @Override
//            public void onSelected(boolean isUserScroll, int index, String item) {
//                selectedMonthIndex = index;
//                if (onWheelListener != null) {
//                    onWheelListener.onMonthWheeled(selectedMonthIndex, item);
//                }
//                if (mode != YEAR_MONTH) {
//                    if (selectedYearIndex < years.size() && index < months.size()) {
//                        changeDayData(DateUtils.trimZero(years.get(selectedYearIndex)), DateUtils.trimZero(months.get(index)));
//                    }
//                    dayView.setItems(days, selectedDayIndex);
//                }
//            }
//        });
        if (mode != YEAR_MONTH) {
//            if (!TextUtils.isEmpty(dayLabel)) {
//                dayTextView.setText(dayLabel);
//            }
//            dayView.setItems(days, selectedDayIndex);
//            dayView.setOnWheelListener(new WheelView.OnWheelListener() {
//                @Override
//                public void onSelected(boolean isUserScroll, int index, String item) {
//                    selectedDayIndex = index;
//                    if (onWheelListener != null) {
//                        onWheelListener.onDayWheeled(selectedDayIndex, item);
//                    }
//                }
//            });
        }


//        frameLayout.addView(layout);

        return yearView;
    }

    private int findItemIndex(ArrayList<String> items, int item) {
        //折半查找有序元素的索引，效率应该高于items.indexOf(...)
        int index = Collections.binarySearch(items, item, new Comparator<Object>() {
            @Override
            public int compare(Object lhs, Object rhs) {
                String lhsStr = lhs.toString();
                String rhsStr = rhs.toString();
                lhsStr = lhsStr.startsWith("0") ? lhsStr.substring(1)                                                                                 lhsStr;
                rhsStr = rhsStr.startsWith("0") ? rhsStr.substring(1)                                                                                 rhsStr;
                return Integer.parseInt(lhsStr) - Integer.parseInt(rhsStr);
            }
        });
        if (index < 0) {
            index = 0;
        }
        return index;
    }

    private void changeYearData() {
        years.clear();
        if (startYear == endYear) {
            years.add(String.valueOf(startYear));
        } else if (startYear < endYear) {
            //年份正序
            for (int i = startYear; i <= endYear; i++) {
                years.add(String.valueOf(i));
            }
        } else {
            //年份逆序
            for (int i = startYear; i >= endYear; i--) {
                years.add(String.valueOf(i));
            }
        }
    }

    private int changeMonthData(int year) {
        String preSelectMonth = months.size() > selectedMonthIndex ? months.get(selectedMonthIndex)                                                                                 null;
        months.clear();
        if (startMonth < 1 || endMonth < 1 || startMonth > 12 || endMonth > 12) {
            throw new IllegalArgumentException("month out of range [1-12]");
        }
        if (startMonth > endMonth) {
            int tmp = startMonth;
            startMonth = endMonth;
            endMonth = tmp;
        }
        if (startYear == endYear) {
            for (int i = startMonth; i <= endMonth; i++) {
                months.add(DateUtils.fillZero(i));
            }
        } else if (year == startYear) {
            for (int i = startMonth; i <= 12; i++) {
                months.add(DateUtils.fillZero(i));
            }
        } else if (year == endYear) {
            for (int i = 1; i <= endMonth; i++) {
                months.add(DateUtils.fillZero(i));
            }
        } else {
            for (int i = 1; i <= 12; i++) {
                months.add(DateUtils.fillZero(i));
            }
        }
        //当前设置的月份不在指定范围，则默认选中范围开始的月份
        int preSelectMonthIndex = preSelectMonth == null ? 0                                                                                 months.indexOf(preSelectMonth);
        selectedMonthIndex = preSelectMonthIndex == -1 ? 0                                                                                 preSelectMonthIndex;
        return DateUtils.trimZero(months.get(selectedMonthIndex));
    }

    private void changeDayData(int year, int month) {
        String preSelectDay = days.size() > selectedDayIndex ? days.get(selectedDayIndex)                                                                                 null;
        days.clear();
        int maxDays = DateUtils.calculateDaysInMonth(year, month);
        if (year == startYear && month == startMonth) {
            for (int i = startDay; i <= maxDays; i++) {
                days.add(DateUtils.fillZero(i));
            }
            //当前设置的日子不在指定范围，则默认选中范围开始的日子
            int preSelectDayIndex = preSelectDay == null ? 0                                                                                 days.indexOf(preSelectDay);
            selectedDayIndex = preSelectDayIndex == -1 ? 0                                                                                 preSelectDayIndex;
        } else if (year == endYear && month == endMonth) {
            for (int i = 1; i <= endDay; i++) {
                days.add(DateUtils.fillZero(i));
            }
            //当前设置的日子不在指定范围，则默认选中范围开始的日子
            int preSelectDayIndex = preSelectDay == null ? 0                                                                                 days.indexOf(preSelectDay);
            selectedDayIndex = preSelectDayIndex == -1 ? 0                                                                                 preSelectDayIndex;
        } else {
            for (int i = 1; i <= maxDays; i++) {
                days.add(DateUtils.fillZero(i));
            }
            if (selectedDayIndex >= maxDays) {
                //年或月变动时，保持之前选择的日不动：如果之前选择的日是之前年月的最大日，则日自动为该年月的最大日
                selectedDayIndex = days.size() - 1;
            }
        }
    }

    @Override
    protected void onSubmit() {
        if (onDatePickListener == null) {
            return;
        }
        String year = getSelectedYear();
        String month = getSelectedMonth();
        String day = getSelectedDay();
        switch (mode) {
            case YEAR_MONTH:
                ((OnYearMonthPickListener) onDatePickListener).onDatePicked(year, month);
                break;
            case MONTH_DAY:
                ((OnMonthDayPickListener) onDatePickListener).onDatePicked(month, day);
                break;
            default:
                ((OnYearMonthDayPickListener) onDatePickListener).onDatePicked(year, month, day);
                break;
        }
    }

    public String getSelectedYear() {
        return years.get(selectedYearIndex);
    }

    public String getSelectedMonth() {
        return months.get(selectedMonthIndex);
    }

    public String getSelectedDay() {
        try {
            return days.get(selectedDayIndex);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    protected interface OnDatePickListener {

    }

    public interface OnYearMonthDayPickListener extends OnDatePickListener {

        void onDatePicked(String year, String month, String day);

    }

    public interface OnYearMonthPickListener extends OnDatePickListener {

        void onDatePicked(String year, String month);

    }

    public interface OnMonthDayPickListener extends OnDatePickListener {

        void onDatePicked(String month, String day);

    }

    public interface OnWheelListener {

        void onYearWheeled(int index, String year);

        void onMonthWheeled(int index, String month);

        void onDayWheeled(int index, String day);

    }

}
