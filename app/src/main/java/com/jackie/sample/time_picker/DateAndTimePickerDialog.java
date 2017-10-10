package com.jackie.sample.time_picker;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.jackie.sample.R;
import com.jackie.sample.time_picker.loop_view.DateUtil;
import com.jackie.sample.time_picker.loop_view.LoopListener;
import com.jackie.sample.time_picker.loop_view.LoopView;
import com.jackie.sample.utils.LogUtils;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class DateAndTimePickerDialog extends Dialog {

    private static  int MIN_YEAR = 1900;
    private static  int MAX_YEAR = 0;
    private Params params;

    public DateAndTimePickerDialog(Context context, int themeResId) {
        super(context, themeResId);
        // 最大年份是当前年份+20
        Calendar c = Calendar.getInstance();
        MAX_YEAR = c.get(Calendar.YEAR) + 20;
    }

    private void setParams(DateAndTimePickerDialog.Params params) {
        this.params = params;
    }

    public interface OnDateTimeSelectedListener {
         void onDateTimeSelected(String date);
         void onCancel();
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
	 /* 触摸外部弹窗 */
        if (isOutOfBounds(getContext(), event)) {
            if (params.callback != null) {
                params.callback.onCancel();
            }
        }
        return super.onTouchEvent(event);
    }

    private boolean isOutOfBounds(Context context, MotionEvent event) {
        final int x = (int) event.getX();
        final int y = (int) event.getY();
        final int slop = ViewConfiguration.get(context).getScaledWindowTouchSlop();
        final View decorView = getWindow().getDecorView();
        return (x < -slop) || (y < -slop) || (x > (decorView.getWidth() + slop))
                || (y > (decorView.getHeight() + slop));
    }

    private static final class Params {
        private boolean shadow = true;
        private LoopView loopYear, loopDay ,loopMonth,loopHour, loopMin;
        private OnDateTimeSelectedListener callback;
    }

    public static class Builder {
        private final Context context;
        private final DateAndTimePickerDialog.Params params;
        private Integer minYear;
        private Integer maxYear;
        private Integer selectYear;
        private Integer selectMonth;
        private Integer selectDay;
        private Integer selectHour;
        private Integer selectMin;
        private Integer minMonth;
        private Integer maxMonth;
        private Integer minDay;
        private Integer maxDay;
        private Integer minHour;
        private Integer maxHour;
        private Integer minMin;
        private Integer maxMin;
        private String mTitle;
        private String mData;
        private boolean canCancel = true;


        public Builder(Context context) {
            this.context = context;
            params = new DateAndTimePickerDialog.Params();
        }

        public Builder setData(String data) {
            this.mData = data;
            return this;
        }

	    public Builder setCancel(boolean cancel) {
		    this.canCancel = cancel;
		    return this;
	    }

        public Builder setMinHour(int hour) {
            minHour = hour;
            return this;
        }
        public Builder setTitle(String title) {
            mTitle = title;
            return this;
        }

        public Builder setMaxHour(int hour) {
            maxHour = hour;
            return this;
        }

        public Builder setMinMin(int min) {
            minMin = min;
            return this;
        }

        public Builder setMaxMin(int min) {
            maxMin = min;
            return this;
        }

        public Builder setMinYear(int year){
            minYear=year;
            return this;
        }

        public Builder setMaxYear(int year){
            maxYear=year;
            return this;
        }

        public Builder setMinMonth(int month){
            minMonth=month;
            return this;
        }

        public Builder setMaxMonth(int month){
            maxMonth=month;
            return this;
        }

        public Builder setMinDay(int day){
            minDay=day;
            return this;
        }

        public Builder setMaxDay(int day){
            maxDay=day;
            return this;
        }

        public Builder setSelectYear(int year){
            this.selectYear=year;
            return this;
        }

        public Builder setSelectMonth(int month){
            this.selectMonth=month;
            return this;
        }

        public Builder setSelectDay(int day){
            this.selectDay=day;
            return this;
        }

        public Builder setSelectHour(int hour) {
            this.selectHour = hour;
            return this;
        }

        public Builder setSelectMin(int min) {
            this.selectMin = min;
            return this;
        }

        /**
         * 获取当前选择的日期
         *
         * @return int[]数组形式返回。例[1990,6,15]
         */
        private final String getCurrDateValues() {
            int currYear = Integer.parseInt(params.loopYear.getCurrentItemValue());
            int currMonth = Integer.parseInt(params.loopMonth.getCurrentItemValue());
            int currDay = Integer.parseInt(params.loopDay.getCurrentItemValue());
            int currHour = Integer.parseInt(params.loopHour.getCurrentItemValue());
            int currMin = Integer.parseInt(params.loopMin.getCurrentItemValue());
            String data = currYear + "-" + (currMonth > 9 ? currMonth : ("0" + currMonth)) + "-"
                    + (currDay > 9 ? currDay : ("0" + currDay)) +" " +  (currHour > 9 ? currHour : ("0" + currHour)) + ":"+ (currMin > 9 ? currMin : ("0" + currMin));
            return data;
        }

        public Builder setOnDateSelectedListener(OnDateTimeSelectedListener onDateSelectedListener) {
            params.callback = onDateSelectedListener;
            return this;
        }


        public DateAndTimePickerDialog create() {
            final DateAndTimePickerDialog dialog = new DateAndTimePickerDialog(context, params.shadow ? R.style.Theme_Light_NoTitle_Dialog : R.style.Theme_Light_NoTitle_NoShadow_Dialog);
            View view = LayoutInflater.from(context).inflate(R.layout.layout_picker_date_and_time, null);

            TextView textView = (TextView) view.findViewById(R.id.tv_cancel);
            if (mTitle != null) {
                textView.setText(mTitle);
            }

            if (mData != null) {
                List<Integer> dataList = DateUtil.getDateAndTimeForString(mData);
                if (dataList != null && dataList.size() >= 5) {
                    selectYear =dataList.get(0) - 1;
                    selectMonth=dataList.get(1) - 1;
                    selectDay = dataList.get(2) - 1;
                    selectHour = dataList.get(3);
                    selectMin = dataList.get(4);
                }

            }


            Calendar c = Calendar.getInstance();

            final LoopView loopDay = (LoopView) view.findViewById(R.id.loop_day);
            loopDay.setArrayList(d(1, 30));
            if(selectDay!=null){
                loopDay.setCurrentItem(selectDay - 1);
            }else{
                loopDay.setCurrentItem(c.get(Calendar.DAY_OF_MONTH) - 1);
            }
            //loopDay.setNotLoop();

            final LoopView loopYear = (LoopView) view.findViewById(R.id.loop_year);
            if (maxYear != null) {
                MAX_YEAR = maxYear;
            }
            if (minYear != null) {
                MIN_YEAR = minYear;
            }
            loopYear.setArrayList(d(MIN_YEAR, MAX_YEAR - MIN_YEAR + 1));
            if(selectYear!=null){
                loopYear.setCurrentItem(selectYear-MIN_YEAR);
            }else{
                loopYear.setCurrentItem(c.get(Calendar.YEAR) - MIN_YEAR);
            }
            loopYear.setNotLoop();

            final LoopView loopMonth = (LoopView) view.findViewById(R.id.loop_month);
            loopMonth.setArrayList(d(1, 12));
            if(selectMonth!=null){
                loopMonth.setCurrentItem(selectMonth - 1);
            }else{
                loopMonth.setCurrentItem(c.get(Calendar.MONTH));
            }
            loopMonth.setNotLoop();

            final LoopView loopHour = (LoopView) view.findViewById(R.id.loop_hour);
            if(selectHour!=null){
                loopHour.setCurrentItem(selectHour);
            }else{
                loopHour.setCurrentItem(c.get(Calendar.HOUR_OF_DAY));
            }
            //修改优化边界值 by lmt 16/ 9 /12.禁用循环滑动,循环滑动有bug
            loopHour.setCyclic(false);
            loopHour.setArrayList(d(0, 24));

            final LoopView loopMin = (LoopView) view.findViewById(R.id.loop_min);
            if(selectMin!=null){
                loopMin.setCurrentItem(selectMin);
            }else{
                loopMin.setCurrentItem(c.get(Calendar.MINUTE));
            }
            loopMin.setCyclic(false);
            loopMin.setArrayList(d(0, 60));




            final LoopListener maxDaySyncListener = new LoopListener() {
                @Override
                public void onItemSelect(int item) {
                    Calendar c = Calendar.getInstance();
                    boolean needFixed=true;
                    if(minYear!=null){
                        if(Integer.parseInt(loopYear.getCurrentItemValue())==minYear ){
                            if(minMonth!=null){
                                if(Integer.parseInt(loopMonth.getCurrentItemValue())<minMonth){
                                    loopMonth.setCurrentItem(minMonth - 1);
                                }
                            }
                        }else if(Integer.parseInt(loopYear.getCurrentItemValue())<minYear){
                            loopYear.setCurrentItem(minYear-MIN_YEAR);
                        }
                    }

                    if(maxYear!=null){
                        if(Integer.parseInt(loopYear.getCurrentItemValue())==maxYear ){
                            if(maxMonth!=null){
                                if(Integer.parseInt(loopMonth.getCurrentItemValue())>maxMonth){
                                    loopMonth.setCurrentItem(maxMonth - 1);
                                }
                            }
                        }else if(Integer.parseInt(loopYear.getCurrentItemValue())>maxYear){
                            loopYear.setCurrentItem(maxYear-MIN_YEAR);
                        }
                    }

                    c.set(Integer.parseInt(loopYear.getCurrentItemValue()), Integer.parseInt(loopMonth.getCurrentItemValue()) - 1, 1);
                    LogUtils.showLog(loopYear.getCurrentItemValue());
                    c.roll(Calendar.DATE, false);

                    if(needFixed){
                        int maxDayOfMonth = c.get(Calendar.DATE);
                        int fixedCurr = loopDay.getCurrentItem();
                        loopDay.setArrayList(d(1, maxDayOfMonth));
                        // 修正被选中的日期最大值
                        if (fixedCurr > maxDayOfMonth) fixedCurr = maxDayOfMonth - 1;
                        loopDay.setCurrentItem(fixedCurr);
                    }
                }
            };

            final LoopListener dayLoopListener=new LoopListener() {
                @Override
                public void onItemSelect(int item) {
                    if(minYear!=null && minMonth!=null && minDay!=null
                            && Integer.parseInt(loopYear.getCurrentItemValue())==minYear
                            && Integer.parseInt(loopMonth.getCurrentItemValue())==minMonth
                            && Integer.parseInt(loopDay.getCurrentItemValue())<minDay
                            ){
                        loopDay.setCurrentItem(minDay-1);
                    }

                    if(maxYear!=null && maxMonth!=null && maxDay!=null
                            && Integer.parseInt(loopYear.getCurrentItemValue())==maxYear
                            && Integer.parseInt(loopMonth.getCurrentItemValue())==maxMonth
                            && Integer.parseInt(loopDay.getCurrentItemValue())>maxDay
                            ){
                        loopDay.setCurrentItem(maxDay-1);
                    }
                }
            };
            loopYear.setListener(maxDaySyncListener);
            loopMonth.setListener(maxDaySyncListener);
            loopDay.setListener(dayLoopListener);

            view.findViewById(R.id.tx_finish).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    params.callback.onDateTimeSelected(getCurrDateValues());
                }
            });

            Window win = dialog.getWindow();
            win.getDecorView().setPadding(0, 0, 0, 0);
            WindowManager.LayoutParams lp = win.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            win.setAttributes(lp);
            win.setGravity(Gravity.BOTTOM);
            win.setWindowAnimations(R.style.Animation_Bottom_Rising);

            dialog.setContentView(view);
            dialog.setCanceledOnTouchOutside(canCancel);
            dialog.setCancelable(canCancel);

            params.loopYear = loopYear;
            params.loopMonth = loopMonth;
            params.loopDay = loopDay;
            params.loopHour = loopHour;
            params.loopMin = loopMin;
            dialog.setParams(params);

            return dialog;
        }

        /**
         * 将数字传化为集合，并且补充0
         *
         * @param startNum 数字起点
         * @param count    数字个数
         * @return
         */
        private static List<String> d(int startNum, int count) {
            String[] values = new String[count];
            for (int i = startNum; i < startNum + count; i++) {
                String tempValue = (i < 10 ? "0" : "") + i;
                values[i - startNum] = tempValue;
            }
            return Arrays.asList(values);
        }

    }
}
