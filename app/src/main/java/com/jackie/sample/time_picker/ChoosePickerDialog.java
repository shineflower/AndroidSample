package com.jackie.sample.time_picker;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.jackie.sample.R;
import com.jackie.sample.time_picker.picker_view.CharacterPickerView;

import java.util.ArrayList;
import java.util.List;

public class ChoosePickerDialog extends Dialog {

    private Params params;

    public ChoosePickerDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    private void setParams(ChoosePickerDialog.Params params) {
        this.params = params;
    }


    public void setSelection(String itemValue) {
        if (params.dataList.size() > 0) {
            int idx = params.dataList.indexOf(itemValue);
            if (idx >= 0) {
                params.initSelection = idx;
                params.loopData.setSelectOptions(params.initSelection);
            }
        }
    }

    public interface OnSelectedListener {
        void onSelected(String itemValue, int position);
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
        private boolean canCancel = true;
        private CharacterPickerView loopData;
        private String title;
        private String unit;
        private int initSelection;
        private OnSelectedListener callback;
        private final List<String> dataList = new ArrayList<>();
    }

    public static class Builder {
        private final Context context;
        private final ChoosePickerDialog.Params params;

        public Builder(Context context) {
            this.context = context;
            params = new ChoosePickerDialog.Params();
        }

        private final String getCurrDateValue() {
            if (params.dataList.size() == 0) {
                return null;
            } else {
                return String.valueOf(params.dataList.get(params.loopData.getCurrentItems())).trim();
            }
        }

        public Builder setData(List<String> dataList) {
            params.dataList.clear();
            params.dataList.addAll(dataList);
            return this;
        }

        public Builder setTitle(String title) {
            params.title = title;
            return this;
        }

        public Builder setUnit(String unit) {
            params.unit = unit;
            return this;
        }

        public Builder setSelection(int selection) {
            params.initSelection = selection;
            return this;
        }

        public Builder setSelection(String selection) {
            if (params.dataList.size() > 0) {
                int idx = params.dataList.indexOf(selection);
                if (idx >= 0) {
                    params.initSelection = idx;
                }
            }
            return this;
        }


        public Builder setOnDataSelectedListener(OnSelectedListener onDataSelectedListener) {
            params.callback = onDataSelectedListener;
            return this;
        }


        public ChoosePickerDialog create() {
            final ChoosePickerDialog dialog = new ChoosePickerDialog(context, params.shadow ? R.style.Theme_Light_NoTitle_Dialog : R.style.Theme_Light_NoTitle_NoShadow_Dialog);
            View view = LayoutInflater.from(context).inflate(R.layout.layout_choose_picker, null);

            if (!TextUtils.isEmpty(params.title)) {
                TextView txTitle = (TextView) view.findViewById(R.id.tv_title);
                txTitle.setText(params.title);
            }
            if (!TextUtils.isEmpty(params.unit)) {
                TextView txUnit = (TextView) view.findViewById(R.id.tx_unit);
                txUnit.setText(params.unit);
            }

            final CharacterPickerView loopData = (CharacterPickerView) view.findViewById(R.id.loop_data);
            loopData.setPicker(params.dataList);
            if (params.dataList.size() > 0) loopData.setSelectOptions(params.initSelection);
            view.findViewById(R.id.tx_finish).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();

                    if (getCurrDateValue() == null) {
                        return;
                    }

                    params.callback.onSelected(getCurrDateValue(),loopData.getCurrentItems());
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

            params.loopData = loopData;
            dialog.setParams(params);

            return dialog;
        }
    }
}
