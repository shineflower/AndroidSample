package com.jackie.sample.custom_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jackie.sample.R;
import com.jackie.sample.utils.ScreenUtils;

import java.util.List;

/**
 * Created by Jackie on 2017/5/18.
 */

public class TrianglePagerIndicator extends LinearLayout {
    private Context mContext;

    //绘制三角形的画笔
    private Paint mPaint;
    //path构成一个三角形
    private Path mPath;
    //三角形的宽度
    private int mTriangleWidth;
    //三角形的高度
    private int mTriangleHeight;

    //三角形的宽度为单个Tab的1/6
    private static final float RADIO_TRIANGLE = 1.0f / 6;
    //三角形的最大宽度
    private int mTriangeMaxWidth;

    //初始时，三角形指示器的偏移量
    private int mInitTranslationX;
    //手指滑动的偏移量
    private float mTranslationX;
    //默认的Tab的数量
    private static final int COUNT_DEFAULT_TAB = 4;
    //Tab数量
    private int mTabVisibleCount = COUNT_DEFAULT_TAB;
    //Tab上的内容
    private List<String> mTabTitleList;
    //与之绑定的ViewPager
    private ViewPager mViewPager;
    //标题正常的颜色
    private static final int COLOR_TEXT_NORMAL = 0x77FFFFFF;
    //标题选中的颜色
    private static final int COLOR_TEXT_HIGHLIGHT = 0xFFFFFFFF;

    //对外的ViewPager的回调接口
    public interface OnPageChangeListener {
        void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);
        void onPageSelected(int position);
        void onPageScrollStateChanged(int state);
    }

    // 对外的ViewPager的回调接口
    private OnPageChangeListener mOnPageChangeListener;

    // 对外的ViewPager的回调接口的设置
    public void setOnPageChangeListener(OnPageChangeListener onPgeChangeListener) {
        this.mOnPageChangeListener = onPgeChangeListener;
    }

    public TrianglePagerIndicator(Context context) {
        this(context, null);
    }

    public TrianglePagerIndicator(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TrianglePagerIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;

        mTriangeMaxWidth = (int) (ScreenUtils.getScreenWidth(mContext) / 3 * RADIO_TRIANGLE);

        // 获得自定义属性，Tab的数量
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ViewPagerIndicator);
        mTabVisibleCount = ta.getInt(R.styleable.ViewPagerTriangleIndicator_indicator_item_count, COUNT_DEFAULT_TAB);
        if (mTabVisibleCount < 0) {
            mTabVisibleCount = COUNT_DEFAULT_TAB;
        }

        ta.recycle();

        //初始化画笔
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.parseColor("#FFFFFFFF"));
        mPaint.setStyle(Paint.Style.FILL);
        /**
         * http://www.cnblogs.com/tianzhijiexian/p/4297783.html
         * CornerPathEffect则可以将路径的转角变得圆滑，
         * CornerPathEffect的构造方法只接受一个参数radius，意思就是转角处的圆滑程度
         */
        mPaint.setPathEffect(new CornerPathEffect(3));
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        int childCount = getChildCount();
        if (childCount == 0) {
            return;
        }

        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            LinearLayout.LayoutParams params = (LayoutParams) childView.getLayoutParams();
            params.width = ScreenUtils.getScreenWidth(getContext()) / mTabVisibleCount;
            params.weight = 0;
            childView.setLayoutParams(params);
        }

        setItemClickEvent();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mTriangleWidth = (int) (w / mTabVisibleCount * RADIO_TRIANGLE);
        mTriangleWidth = Math.min(mTriangeMaxWidth, mTriangleWidth);

        //初始化三角形
        initTriangle();

        //初始时的偏移量
        mInitTranslationX = getWidth() / mTabVisibleCount / 2 - mTriangleWidth / 2;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.save();
        //画笔平移到正确的位置
        canvas.translate(mInitTranslationX + mTranslationX, getHeight() + 1);
        canvas.drawPath(mPath, mPaint);
        canvas.restore();

        super.dispatchDraw(canvas);
    }

    private void setItemClickEvent() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final int position = i;

            View childView = getChildAt(i);
            childView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewPager.setCurrentItem(position);
                }
            });
        }
    }

    private void initTriangle() {
        mPath = new Path();

        mTriangleHeight = (int) (mTriangleWidth / 2 / Math.sqrt(2));
        mPath.moveTo(0, 0);
        mPath.lineTo(mTriangleWidth, 0);
        mPath.lineTo(mTriangleWidth / 2, -mTriangleHeight);
        mPath.close();
    }

    //设置关联的ViewPager
    public void setViewPager(ViewPager viewPager, int position) {
        this.mViewPager = viewPager;

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // 滚动
                scroll(position, positionOffset);

                if (mOnPageChangeListener != null) {
                    mOnPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
                }
            }

            @Override
            public void onPageSelected(int position) {
                //设置字体颜色高亮
                resetTextViewColor();
                highlightTextView(position);

                // 回调
                if (mOnPageChangeListener != null) {
                    mOnPageChangeListener.onPageSelected(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // 回调
                if (mOnPageChangeListener != null) {
                    mOnPageChangeListener.onPageScrollStateChanged(state);
                }
            }
        });

        // 设置当前页
        mViewPager.setCurrentItem(position);
        // 高亮
        highlightTextView(position);
    }

    /**
     * 指示器跟随手指滚动，以及容器滚动
     * @param position
     * @param positionOffset
     */
    private void scroll(int position, float positionOffset) {
        //不断改变偏移量
        mTranslationX = getWidth() / mTabVisibleCount * (position + positionOffset);
        int tabWidth = ScreenUtils.getScreenWidth(mContext) / mTabVisibleCount;

        //容器滚动，当移动到倒数最后一个的时候，开始滚动
        if (positionOffset > 0 && position >= (mTabVisibleCount - 2) &&
                getChildCount() > mTabVisibleCount) {
            if (mTabVisibleCount != 1) {
                this.scrollTo((position - (mTabVisibleCount - 2)) * tabWidth + (int) (tabWidth * positionOffset), 0);
            } else {
                // 为count为1时 的特殊处理
                this.scrollTo(position * tabWidth + (int) (tabWidth * positionOffset), 0);
            }
        }

        invalidate();
    }

    /**
     * 重置文本颜色
     */
    private void resetTextViewColor() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if (childView instanceof TextView) {
                ((TextView) childView).setTextColor(COLOR_TEXT_NORMAL);
            }
        }
    }

    /**
     * 高亮选中的文本
     */
    private void highlightTextView(int position) {
        View childView = getChildAt(position);
        if (childView instanceof TextView) {
            ((TextView) childView).setTextColor(COLOR_TEXT_HIGHLIGHT);
        }
    }

    /**
     * 设置可见的tab的数量
     * @param count
     */
    public void setVisibleTabCount(int count) {
        this.mTabVisibleCount = count;
    }

    /**
     * 设置tab的标题内容 可选，可以自己在布局文件中写死
     * @param tabTitleList
     */
    public void setTabTitleList(List<String> tabTitleList) {
        // 如果传入的list有值，则移除布局文件中设置的view
        if (tabTitleList != null && tabTitleList.size() > 0) {
            this.removeAllViews();
            this.mTabTitleList = tabTitleList;

            for (String tabTitle : tabTitleList) {
                // 添加view
                addView(generateTextView(tabTitle));
            }
            // 设置item的click事件
            setItemClickEvent();
        }
    }

    /**
     * 根据标题生成我们的TextView
     * @param tabTitle
     * @return
     */
    private TextView generateTextView(String tabTitle) {
        TextView textView = new TextView(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        params.width = ScreenUtils.getScreenWidth(mContext) / mTabVisibleCount;
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(COLOR_TEXT_NORMAL);
        textView.setText(tabTitle);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        textView.setLayoutParams(params);
        return textView;
    }
}
