package com.jackie.sample.custom_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.jackie.sample.R;

/**
 * Created by Jackie on 2018/1/23.
 * 文字分页器
 */
public class TextPageIndicator extends AppCompatTextView {
    // 当前页数
    private int mCurrentPage = 1;  //默认从第1页显示
    // 总页数
    private int mTotalPage;

    private CustomViewPagerInternal mCustomViewPagerInternal;

    public interface OnPageChangeListener {
        void onPageSelected(int position);
    }

    private OnPageChangeListener mOnPageChangeListener;

    public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        this.mOnPageChangeListener = onPageChangeListener;
    }

    public TextPageIndicator(Context context) {
        this(context, null);
    }

    public TextPageIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextPageIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView();
        initAttrs(context, attrs);
    }

    private void initView() {
        // https://stackoverflow.com/questions/4768738/android-textview-remove-spacing-and-padding-on-top-and-bottom
        setIncludeFontPadding(false);
    }

    /**
     * 初始化自定义属性
     * @param context   上下文
     * @param attrs     属性集合
     */
    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TextPageIndicator);

        int indicatorType = typedArray.getInteger(R.styleable.TextPageIndicator_indicatorType, 1);  //默认类型是1，不带任何背景

        if (indicatorType == 1) {

        } else if (indicatorType == 2) {
            // 黑色背景
            setBackgroundResource(R.drawable.shape_text_page_indicator_bg);
        }

        typedArray.recycle();
    }

    public void setViewPager(CustomViewPagerInternal customViewPagerInternal) {
        this.mCustomViewPagerInternal = customViewPagerInternal;

        setTotalPage(customViewPagerInternal.getAdapter().getCount());

        setTextPageIndicator();

        customViewPagerInternal.setOnPageChangeListener(new CustomViewPagerInternal.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setCurrentPage(position + 1);

                setTextPageIndicator();

                if (mOnPageChangeListener != null) {
                    mOnPageChangeListener.onPageSelected(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void setViewPager(CustomViewPagerInternal customViewPagerInternal, int initialPosition) {
        setViewPager(customViewPagerInternal);
        setCurrentPage(initialPosition);
    }

    /**
     * 设置文本指示器
     */
    private void setTextPageIndicator() {
        setText(mCurrentPage + "/" + mTotalPage);
    }

    /**
     * 设置当前页数
     * @param currentPage  当前页数
     */
    public void setCurrentPage(int currentPage) {
        this.mCurrentPage = currentPage;

        // ViewPager也切换到当前页数
        mCustomViewPagerInternal.setCurrentItem((currentPage - 1) <= 0 ? 0 : (currentPage - 1));
    }

    /**
     * 设置总页数
     * @param totalPage   总页数
     */
    public void setTotalPage(int totalPage) {
        this.mTotalPage = totalPage;
    }
}
