package com.jackie.sample.custom_view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class ReminderEditText extends EditText {
	public ReminderEditText(Context context) {
		this(context, null);
	}

	public ReminderEditText(Context context, AttributeSet attrs) {
		this(context, attrs, android.R.attr.editTextStyle);
	}

	public ReminderEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		mInputTipList.add("18888888888");
		mInputTipList.add("28888888888");
		mInputTipList.add("28888444444");
	}

	private OnReminderListener mOnReminderListener;
	public interface OnReminderListener {
		void onReminder(boolean focused);
	}

	public void setOnReminderListenr(OnReminderListener onReminderListenr){
		this.mOnReminderListener = onReminderListenr;
	}

//	private String mInputTipText = "18888888888";// 用来做匹配用的字符串,当然要提供一个方法可以配置这个属性的啦
	private List<String> mInputTipList = new ArrayList<>();

//	public void setInputTipText(String inputTipText){
//		mInputTipText = inputTipText;
//	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);// 不要注释此方法哦~~~

		if (isFocused()) {// 只在有焦点的情况下检测
			for (String inputTipText : mInputTipList) {
				if (isInputTipPattern(inputTipText)) {
					// 只处理了竖直居中的情况
					canvas.save();
					final TextPaint textPaint = getPaint();// 因为是继承的TextView,
					// 所以可以直接拿到paint,就不要自己new了.
					textPaint.setColor(Color.GRAY);// 颜色随便设.

					// 只绘制末尾的文本区域
					canvas.clipRect(
							textPaint.measureText(String.valueOf(getText()), 0, getText().length()) + getPaddingLeft(), 0,
							getMeasuredWidth(), getMeasuredHeight());// 重点哦,要不然会和输入框原本的内容重叠呢;

					canvas.drawText(inputTipText, getPaddingLeft(),
							getMeasuredHeight() / 2 - textPaint.descent() / 2 - textPaint.ascent() / 2, textPaint);// 绘制提示文本
					canvas.restore();

                    return;
				}
			}
		}
	}

	private boolean isCenterVertical() {
		return Gravity.CENTER_VERTICAL == (getGravity() & Gravity.CENTER_VERTICAL);
	}

	private boolean isInputTipPattern(String inputTipText) {
		String text = getText().toString();

		return isCenterVertical()
				/* 必须是Gravity.CENTER_VERTICAL */ && !TextUtils.isEmpty(inputTipText) /* 需要自动匹配的文本不能为空 */
				&& !TextUtils.isEmpty(text) /* 当前文本框内容不能为空 */ && inputTipText.startsWith(text) && !TextUtils
				.equals(inputTipText, text) /* 匹配的内容如果已经一致了, 就没必要了. */;
	}

	@Override
	protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
		super.onFocusChanged(focused, direction, previouslyFocusedRect);

		if (!focused) {
			// 没有焦点的时候, 检查自动匹配输入
			for (String inputTipText : mInputTipList) {
				if (isInputTipPattern(inputTipText)) {
					setText(inputTipText);

                    //一旦匹配了一个，跳出循环
                    return;
				}
			}

			if(mOnReminderListener != null){
				mOnReminderListener.onReminder(focused);
			}
		}
	}
}