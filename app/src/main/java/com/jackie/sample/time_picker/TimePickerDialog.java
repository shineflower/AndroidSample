package com.jackie.sample.time_picker;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.jackie.sample.R;
import com.jackie.sample.time_picker.loop_view.LoopView;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class TimePickerDialog extends Dialog {

    public interface OnTimeSelectedListener {
        void onTimeSelected(String times);
    }


    private Params params;

    public TimePickerDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    private void setParams(TimePickerDialog.Params params) {
        this.params = params;
    }


    private static final class Params {
        private boolean shadow = true;
        private boolean canCancel = true;
        private LoopView loopHour, loopMin;
        private OnTimeSelectedListener callback;
    }

    public static class Builder {
        private final Context context;
        private final TimePickerDialog.Params params;
        private Integer selectHour;
        private Integer selectMin;

        public Builder(Context context) {
            this.context = context;
            params = new TimePickerDialog.Params();
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
         * 获取当前选择的时间
         *
         * @return int[]数组形式返回。例[12,30]
         */
        private final String getCurrDateValues() {
            int currHour = Integer.parseInt(params.loopHour.getCurrentItemValue());
            int currMin = Integer.parseInt(params.loopMin.getCurrentItemValue());
            return String.valueOf(currHour) + String.valueOf(currMin);
        }

        public TimePickerDialog create() {
            final TimePickerDialog dialog = new TimePickerDialog(context, params.shadow ? R.style.Theme_Light_NoTitle_Dialog : R.style.Theme_Light_NoTitle_NoShadow_Dialog);
            View view = LayoutInflater.from(context).inflate(R.layout.layout_picker_time, null);
            Calendar calendar = Calendar.getInstance();
            final LoopView loopHour = (LoopView) view.findViewById(R.id.loop_hour);
            if (selectHour != null) {
                loopHour.setCurrentItem(selectHour);
            } else {
                loopHour.setCurrentItem(calendar.get(Calendar.HOUR_OF_DAY));
            }

            //修改优化边界值 by lmt 16/ 9 /12.禁用循环滑动,循环滑动有bug
            loopHour.setCyclic(false);
            loopHour.setArrayList(d(0, 24));

            final LoopView loopMin = (LoopView) view.findViewById(R.id.loop_min);
            loopMin.setCyclic(false);
            loopMin.setArrayList(d(0, 60));
            if (selectMin != null) {
                loopMin.setCurrentItem(selectMin);
            } else {
                loopMin.setCurrentItem(calendar.get(Calendar.MINUTE));
            }

            view.findViewById(R.id.tx_finish).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    params.callback.onTimeSelected(getCurrDateValues());
                }
            });

            view.findViewById(R.id.after_hour).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Calendar now= Calendar.getInstance();

                    now.add(Calendar.MINUTE,60);

                    SimpleDateFormat sdf=new SimpleDateFormat("HH:mm");

                    String dateStr=sdf.format(now.getTimeInMillis());
                    dialog.dismiss();
                    params.callback.onTimeSelected(dateStr);

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
            dialog.setCanceledOnTouchOutside(params.canCancel);
            dialog.setCancelable(params.canCancel);

            params.loopHour = loopHour;
            params.loopMin = loopMin;
            dialog.setParams(params);

            return dialog;
        }


        public Builder setOnTimeSelectedListener(OnTimeSelectedListener onTimeSelectedListener) {
            params.callback = onTimeSelectedListener;
            return this;
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
