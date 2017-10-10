package com.jackie.sample.time_picker.loop_view;

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

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class DatePickerDialog extends Dialog {

    private static  int MIN_YEAR = 1900;
    private static  int MAX_YEAR = 0;
    private Params params;

    public DatePickerDialog(Context context, int themeResId) {
        super(context, themeResId);
        // TODO: 2017/8/24  孙力说最大年份是当前年份+20
        Calendar c = Calendar.getInstance();
        MAX_YEAR = c.get(Calendar.YEAR) + 20;
    }

    private void setParams(DatePickerDialog.Params params) {
        this.params = params;
    }

    public interface OnDateSelectedListener {
         void onDateSelected(String date);
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
        private LoopView loopYear, loopMonth, loopDay;
        private OnDateSelectedListener callback;
    }

    public static class Builder {
        private final Context context;
        private final DatePickerDialog.Params params;
        private Integer minYear;
        private Integer maxYear;
        private Integer selectYear;
        private Integer selectMonth;
        private Integer selectDay;
        private Integer minMonth;
        private Integer maxMonth;
        private Integer minDay;
        private Integer maxDay;
        private String mTitle;
        private String mData;
        private boolean canCancel = true;


        public Builder(Context context) {
            this.context = context;
            params = new DatePickerDialog.Params();
        }

        public Builder setMinYear(int year){
            minYear=year;
            return this;
        }

        public Builder setDate(String date) {
            mData = date;
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

        public Builder setTitle(String title) {
            mTitle = title;
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

        public Builder setCancel(boolean cancel) {
            this.canCancel = cancel;
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
            return String.valueOf(currYear) + "-" + String.valueOf(currMonth) + "-" + String.valueOf(currDay);
        }

        public Builder setOnDateSelectedListener(OnDateSelectedListener onDateSelectedListener) {
            params.callback = onDateSelectedListener;
            return this;
        }


        public DatePickerDialog create() {
            final DatePickerDialog dialog = new DatePickerDialog(context, params.shadow ? R.style.Theme_Light_NoTitle_Dialog : R.style.Theme_Light_NoTitle_NoShadow_Dialog);
            View view = LayoutInflater.from(context).inflate(R.layout.layout_picker_date, null);

            TextView textView = (TextView) view.findViewById(R.id.tv_cancel);
            if (mTitle != null) {
                textView.setText(mTitle);
            }
            if (mData != null) {
                List<Integer> dataList = DateUtil.getDateForString(mData);
                if (dataList != null && dataList.size() >= 3) {
                    selectYear = dataList.get(0) - 1;
                    selectMonth = dataList.get(1) - 1;
                    selectDay = dataList.get(2) - 1;
                }

            }

            //获取明天的日期
            view.findViewById(R.id.tomorrow_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Date date=new Date();//取时间
                    Calendar calendar = new GregorianCalendar();
                    calendar.setTime(date);
                    calendar.add(calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动
                    date=calendar.getTime(); //这个时间就是日期往后推一天的结果
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    String dateString = formatter.format(date);
                    dialog.dismiss();
                    params.callback.onDateSelected(dateString);
                }
            });



            Calendar c = Calendar.getInstance();

            final LoopView loopDay = (LoopView) view.findViewById(R.id.loop_day);
            loopDay.setArrayList(d(1, 30));
            if(selectDay!=null){
                loopDay.setCurrentItem(selectDay);
            }else{
                loopDay.setCurrentItem(c.get(Calendar.DATE) -1 );
            }
            loopDay.setNotLoop();

            final LoopView loopYear = (LoopView) view.findViewById(R.id.loop_year);
            if (maxYear != null) {
                MAX_YEAR = maxYear;
            }
            if (minYear != null) {
                MIN_YEAR = minYear;
            }
            loopYear.setArrayList(d(MIN_YEAR, MAX_YEAR - MIN_YEAR + 1));
            if(selectYear!=null){
                loopYear.setCurrentItem(selectYear-MIN_YEAR+1);
            }else{
                loopYear.setCurrentItem(c.get(Calendar.YEAR) - MIN_YEAR);
            }
            loopYear.setNotLoop();

            final LoopView loopMonth = (LoopView) view.findViewById(R.id.loop_month);
            loopMonth.setArrayList(d(1, 12));
            if(selectMonth!=null){
                loopMonth.setCurrentItem(selectMonth);
            }else{
                loopMonth.setCurrentItem(c.get(Calendar.MONTH));
            }
            loopMonth.setNotLoop();



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
                    params.callback.onDateSelected(getCurrDateValues());
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
