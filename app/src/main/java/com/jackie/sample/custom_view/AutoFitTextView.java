package com.jackie.sample.custom_view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.TextView;

/**
 * Created by Jackie Jackie on 2017/3/20.
 * 显示不下，根据高度自动适配字体大小的TextView
 */

public class AutoFitTextView extends TextView {
    // Attributes
    private TextPaint mTextPaint;
    private float mMinTextSize;
    private float mMaxTextSize;

    // Unit px
    private static float DEFAULT_MIN_TEXT_SIZE = 15;
    private static float DEFAULT_MAX_TEXT_SIZE = 50;

    public AutoFitTextView(Context context) {
        this(context, null);
    }

    public AutoFitTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoFitTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initialise();
    }

    private void initialise() {
        mTextPaint = new TextPaint();
        mTextPaint.set(this.getPaint());

        // max size defaults to the intially specified text size unless it is too small
        mMaxTextSize = this.getTextSize();

        if (mMaxTextSize <= DEFAULT_MIN_TEXT_SIZE) {
            mMaxTextSize = DEFAULT_MAX_TEXT_SIZE;
        }

        mMinTextSize = DEFAULT_MIN_TEXT_SIZE;
    }

    /**
     * Resize the font so the specified text fits in the text box * assuming the text box is the specified width.
     */
    private void refitText(String text, int textWidth, int textHeight) {
        if (textWidth > 0 && textHeight > 0) {
            //allow display rect
            int availableWidth = textWidth - this.getPaddingLeft() - this.getPaddingRight();
            int availableHeight = textHeight - this.getPaddingBottom() - this.getPaddingTop();
            //by the line calculate allow displayWidth
            int autoWidth = availableWidth;
            float multiplier = 1f;
            float add = 0;

            if (Build.VERSION.SDK_INT > 16) {
                multiplier = getLineSpacingMultiplier();
                add = getLineSpacingExtra();
            } else {
                //the multiplier default is 1.0f,if you need change ,you can reflect invoke this field;
            }

            float trySize = mMaxTextSize;
            mTextPaint.setTextSize(trySize);
            int oldLine = 1, newLine = 1;
            while ((trySize > mMinTextSize)) {
                //calculate text single line width。
                int displayW = (int) mTextPaint.measureText(text);
                //calculate text single line height。
                int displayH = round(mTextPaint.getFontMetricsInt(null) * multiplier + add);

                if (displayW < autoWidth) {
                    break;
                }

                //calculate maxLines
                newLine = availableHeight / displayH;
                //if line change ,calculate new autoWidth
                if (newLine > oldLine) {
                    oldLine = newLine;
                    autoWidth = availableWidth * newLine;
                    continue;
                }

                //try more small TextSize
                trySize -= 1;

                if (trySize <= mMinTextSize) {
                    trySize = mMinTextSize;
                    break;
                }

                mTextPaint.setTextSize(trySize);
            }

            //setMultiLine
            if (newLine >= 2) {
                this.setSingleLine(false);
                this.setMaxLines(newLine);
            }

            this.setTextSize(TypedValue.COMPLEX_UNIT_PX, trySize);
        }
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int before, int after) {
        super.onTextChanged(text, start, before, after);
        refitText(text.toString(), this.getWidth(), this.getHeight());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Log.e("TagSizeChange", "new(" + w + "," + h + ") old(" + oldw + "" + oldh + ")");
        if (w != oldw || h != oldh) {
            refitText(this.getText().toString(), w, h);
        }
    }
    //FastMath.round()
    public static int round(float value) {
        long lx = (long) (value * (65536 * 256f));
        return (int) ((lx + 0x800000) >> 24);
    }
}
