package com.jackie.sample.utils;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jackie.sample.R;

/**
 * Dialog工具类封装
 * Created by Jackie on 2017/6/22.
 */

public class DialogUtils {
    private static DialogUtils mDialogTools;
    private Dialog mDialog;

    private OnConfirmListener mOnConfirmListener;

    private DialogUtils() {

    }

    public static DialogUtils getInstance() {
        if (null == mDialogTools) {
            synchronized (DialogUtils.class) {
                if (null == mDialogTools) {
                    mDialogTools = new DialogUtils();
                }
            }
        }

        return mDialogTools;
    }

    public interface OnConfirmListener {
        void onConfirm(Dialog dialog, View view, String input);
    }

    public void setOnConfirmListener(OnConfirmListener onConfirmListener) {
        this.mOnConfirmListener = onConfirmListener;
    }


    /**
     * 带取消确定的Dialog
     * @param title              标题
     * @param content            提示内容
     * @param cancelOutSide      点击窗口外面是否消失
     * @param onConfirmListener  点击确定的回调, 如果是null则dismiss dialog
     */
    public void showCustomDialogPrompt(Context context, String title, String content, boolean cancelOutSide, final OnConfirmListener onConfirmListener) {
        View view = View.inflate(context, R.layout.custom_dialog_1, null);
        TextView dialogTitle = (TextView) view.findViewById(R.id.dialog_title);
        TextView dialogContent = (TextView) view.findViewById(R.id.dialog_content);
        Button dialogCancel = (Button) view.findViewById(R.id.dialog_cancel);
        Button dialogConfirm = (Button) view.findViewById(R.id.dialog_confirm);

        mDialog = new AlertDialog.Builder(context).setView(view).create();

        mDialog.setCanceledOnTouchOutside(cancelOutSide);
        if (!mDialog.isShowing()) {
            mDialog.show();
        }

        WindowManager.LayoutParams params = mDialog.getWindow().getAttributes();
        params.width = context.getResources().getDisplayMetrics().widthPixels * 5 / 6;
        mDialog.getWindow().setAttributes(params);

        if (null != title) {
            dialogTitle.setText(title);
        }

        dialogContent.setText(content);

        dialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

        dialogConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onConfirmListener) {
                    onConfirmListener.onConfirm(mDialog, v, "");
                } else {
                    mDialog.dismiss();
                }
            }
        });
    }

    /**
     * 只有一个按钮的提示框
     * @param title               标题
     * @param content             提示内容
     * @param cancelOutSide       点击外部是否消失
     * @param onConfirmListener   按钮点击的回调事件, 传null默认dismiss dialog
     */
    public void showCustomDialogWarning(Context context, String title, String content, boolean cancelOutSide, final OnConfirmListener onConfirmListener) {
        View view = View.inflate(context, R.layout.custom_dialog_2, null);
        TextView dialogTitle = (TextView) view.findViewById(R.id.dialog_title);
        TextView dialogContent = (TextView) view.findViewById(R.id.dialog_content);
        Button dialogConfirm = (Button) view.findViewById(R.id.dialog_confirm);

        mDialog = new AlertDialog.Builder(context).setView(view).create();

        mDialog.setCanceledOnTouchOutside(cancelOutSide);
        if (!mDialog.isShowing()) {
            mDialog.show();
        }

        WindowManager.LayoutParams params = mDialog.getWindow().getAttributes();
        params.width = context.getResources().getDisplayMetrics().widthPixels * 5 / 6;
        mDialog.getWindow().setAttributes(params);

        if (null != title) {
            dialogTitle.setText(title);
        }

        dialogContent.setText(content);

        dialogConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onConfirmListener) {
                    onConfirmListener.onConfirm(mDialog, v, "");
                } else {
                    mDialog.dismiss();
                }
            }
        });
    }

    public void showCustomDialogInput(Context context, String title, boolean cancelOutSide, final OnConfirmListener onConfirmListener) {
        View view = View.inflate(context, R.layout.custom_dialog_3, null);
        TextView dialogTitle = (TextView) view.findViewById(R.id.dialog_title);
        final EditText dialogInput = (EditText) view.findViewById(R.id.dialog_input);
        Button dialogCancel = (Button) view.findViewById(R.id.dialog_cancel);
        Button dialogConfirm = (Button) view.findViewById(R.id.dialog_confirm);

        mDialog = new AlertDialog.Builder(context).setView(view).create();
        mDialog.setCanceledOnTouchOutside(cancelOutSide);
        if (!mDialog.isShowing()) {
            mDialog.show();
        }

        WindowManager.LayoutParams params = mDialog.getWindow().getAttributes();
        params.width = context.getResources().getDisplayMetrics().widthPixels * 5 / 6;
        mDialog.getWindow().setAttributes(params);

        if (null != title) {
            dialogTitle.setText(title);
        }

        dialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

        dialogConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onConfirmListener) {
                    String inputContent = dialogInput.getText().toString().trim();
                    onConfirmListener.onConfirm(mDialog, v, inputContent);
                } else {
                    mDialog.dismiss();
                }
            }
        });
    }
}
