package com.jackie.sample.custom_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.InputFilter;
import android.text.InputType;
import android.util.AttributeSet;
import android.widget.EditText;

import com.jackie.sample.R;
import com.jackie.sample.utils.DensityUtils;
import com.jackie.sample.utils.ScreenUtils;

/**
 * Created by Jackie on 2017/8/9.
 * 短信验证码输入控件
 */

public class CodeEditText extends EditText {
    private Context mContext;

    private Paint mGrayLinePaint;
    private Paint mBlueLinePaint;
    private Paint mCodePaint;

    private int mLineWidth;    //线宽
    private int mLineHeight;   //线高
    private int mCodeLength;   //长度
    private int mCodeSize;     //字体大小
    private int mLinePadding;  //线间距

    private String mInputText = "";  //输入的文本内容
    private int mInputLength = 0;    //输入的文本长度

    private int mWidth;  //控件的宽度
    private int mHeight; //控件的高度

    private OnInputFinishedListener mOnInputFinishedListener;

    public interface OnInputFinishedListener {
        void onInputFinished(String code);
    }

    public void setOnInputFinishedListener(OnInputFinishedListener onInputFinishedListener) {
        this.mOnInputFinishedListener = onInputFinishedListener;
    }

    public CodeEditText(Context context) {
        this(context, null);
    }

    public CodeEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CodeEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.mContext = context;

        initAttrs(attrs);
        initView();
    }

    private void initAttrs(AttributeSet attrs) {
        mLineWidth = DensityUtils.dp2px(mContext, 40);  //40dp
        mLineHeight = DensityUtils.dp2px(mContext, 1);  //1dp
        mCodeLength = 6;  //6
        mCodeSize = getResources().getDimensionPixelSize(R.dimen.dimen_dp30);  //30dp

        //获取自定属性
        TypedArray ta = mContext.obtainStyledAttributes(attrs, R.styleable.CodeEditText);
        mLineWidth = ta.getDimensionPixelSize(R.styleable.CodeEditText_line_width, mLineWidth);
        mLineHeight = ta.getDimensionPixelSize(R.styleable.CodeEditText_line_height, mLineHeight);
        mCodeLength = ta.getInt(R.styleable.CodeEditText_code_length, mCodeLength);
        mCodeSize = ta.getDimensionPixelSize(R.styleable.CodeEditText_code_size, mCodeSize);

        ta.recycle();
    }

    private void initView() {
        //相关设置
        setInputType(InputType.TYPE_CLASS_NUMBER);
        setFilters(new InputFilter[] { new InputFilter.LengthFilter(mCodeLength) });

        setCursorVisible(false);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setEnabled(true);
        setClickable(true);

        setTextColor(getResources().getColor(android.R.color.transparent));

        mGrayLinePaint = new Paint();
        mGrayLinePaint.setAntiAlias(true);
        mGrayLinePaint.setDither(true);
        mGrayLinePaint.setStrokeWidth(mLineHeight);
        mGrayLinePaint.setColor(getResources().getColor(R.color.color_d5d7dc));

        mBlueLinePaint = new Paint();
        mBlueLinePaint.setAntiAlias(true);
        mBlueLinePaint.setDither(true);
        mBlueLinePaint.setStrokeWidth(mLineHeight);
        mBlueLinePaint.setColor(getResources().getColor(R.color.color_337cff));

        mCodePaint = new Paint();
        mCodePaint.setAntiAlias(true);
        mCodePaint.setDither(true);
        mCodePaint.setTextSize(mCodeSize);
        mCodePaint.setColor(getResources().getColor(R.color.color_337cff));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawLine(canvas);
        drawCode(canvas);
    }

    private void drawLine(Canvas canvas) {
        int width = ScreenUtils.getScreenWidth(mContext) - getPaddingLeft() - getPaddingRight();
        //计算每条线之间的间距
        mLinePadding = (width - mLineWidth * mCodeLength) / (mCodeLength - 1);

        //灰色
        for (int i = mInputLength; i < mCodeLength; i++) {
            int x = getPaddingLeft() + (mLineWidth + mLinePadding) * i;

            canvas.drawLine(x, mHeight - mLineHeight, x + mLineWidth, mHeight - mLineHeight, mGrayLinePaint);
        }

        //蓝色
        for (int j = 0; j < mInputLength; j++) {
            int x = getPaddingLeft() + (mLineWidth + mLinePadding) * j;

            canvas.drawLine(x, mHeight - mLineHeight, x + mLineWidth, mHeight - mLineHeight, mBlueLinePaint);
        }
    }

    private void drawCode(Canvas canvas) {
        for (int i = 0; i < mInputLength; i++) {
            float x = getPaddingLeft() + mLineWidth / 2 - mCodePaint.measureText(mInputText.charAt(i) + "") / 2 + (mLineWidth + mLinePadding) * i;

            Rect bounds = new Rect();
            mCodePaint.getTextBounds(mInputText.charAt(i) + "", 0, (mInputText.charAt(i) + "").length(), bounds);
            Paint.FontMetricsInt fontMetrics = mCodePaint.getFontMetricsInt();
            int baseline = (mHeight - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;

            canvas.drawText(mInputText.charAt(i) + "", x, baseline, mCodePaint);
        }

        //重置数字画笔颜色
        mCodePaint.setColor(getResources().getColor(R.color.color_337cff));
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);

        mInputText = text.toString().trim();
        mInputLength = text.toString().trim().length();

        if (mInputLength == mCodeLength) {
            //输入完成
            if (mOnInputFinishedListener != null) {
                mOnInputFinishedListener.onInputFinished(text.toString().trim());
            }
        }

        invalidate();
    }

    /**
     * 设置线宽
     * @param lineWidth 线宽
     */
    public void setLineWidth(int lineWidth) {
        this.mLineWidth = lineWidth;
    }

    /**
     * 设置线高
     * @param lineHeight
     */
    public void setLineHeight(int lineHeight) {
        this.mLineHeight = lineHeight;

        mGrayLinePaint.setStrokeWidth(lineHeight);
        mBlueLinePaint.setStrokeWidth(lineHeight);
    }

    /**
     * 设置长度，默认是6
     * @param codeLength 长度
     */
    public void setCodeLength(int codeLength) {
        this.mCodeLength = codeLength;

        setFilters(new InputFilter[] { new InputFilter.LengthFilter(codeLength) });
    }

    /**
     * 设置字体大小，默认30dp
     * @param codeSize  字体大小
     */
    public void setCodeSize(int codeSize) {
        mCodeSize = codeSize;

        mCodePaint.setTextSize(codeSize);
    }

    /**
     *     设置错误状态
     */
    public void setWrongState() {
        mCodePaint.setColor(getResources().getColor(R.color.color_ff1f1f));

        invalidate();
    }
}
