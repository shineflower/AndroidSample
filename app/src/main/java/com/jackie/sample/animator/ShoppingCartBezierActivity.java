package com.jackie.sample.animator;

import android.graphics.BitmapFactory;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jackie.sample.R;
import com.jackie.sample.adapter.GoodsAdapter;
import com.jackie.sample.bean.GoodsBean;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ValueAnimator;

import java.util.ArrayList;

/**
 * 贝塞尔曲线的购物车添加动画
 * 2017-02-28
 */
public class ShoppingCartBezierActivity extends AppCompatActivity {
    // 购物车父布局
    private RelativeLayout mShoppingCartContainer;
    // 购物车列表显示
    private ListView mShoppingCartListView;
    // 购物数目显示
    private TextView mTvShoppingCartCount;
    // 购物车图片显示
    private ImageView mIvShoppingCart;
    // 购物车适配器
    private GoodsAdapter mGoodsAdapter;
    // 数据源（购物车商品图片）
    private ArrayList<GoodsBean> mGoodsBeans;
    // 贝塞尔曲线中间过程点坐标
    private float[] mCurrentPosition = new float[2];
    // 路径测量
    private PathMeasure mPathMeasure;
    // 购物车商品数目
    private int goodsCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart_bezier);

        mShoppingCartListView = (ListView) findViewById(R.id.lv_bezier_curve_shopping_cart);
        mTvShoppingCartCount = (TextView) findViewById(R.id.tv_bezier_curve_shopping_cart_count);
        mShoppingCartContainer = (RelativeLayout) findViewById(R.id.rly_bezier_curve_shopping_cart);
        mIvShoppingCart = (ImageView) findViewById(R.id.iv_bezier_curve_shopping_cart);

        // 是否显示购物车商品数目
        isShowCartGoodsCount();
        // 添加数据源
        initData();
        // 设置适配器
        initView();
    }

    private void isShowCartGoodsCount(){
        if (goodsCount == 0){
            mTvShoppingCartCount.setVisibility(View.GONE);
        }else {
            mTvShoppingCartCount.setVisibility(View.VISIBLE);
        }
    }

    private void initData() {
        // 初始化数据源
        mGoodsBeans = new ArrayList<>();
        // 添加数据源
        GoodsBean goodsBean = new GoodsBean();
        goodsBean.setGoodsBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.goods_one));
        mGoodsBeans.add(goodsBean);

        goodsBean = new GoodsBean();
        goodsBean.setGoodsBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.goods_two));
        mGoodsBeans.add(goodsBean);

        goodsBean = new GoodsBean();
        goodsBean.setGoodsBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.goods_three));
        mGoodsBeans.add(goodsBean);
    }

    private void initView() {
        // 初始化适配器
        mGoodsAdapter = new GoodsAdapter(this, mGoodsBeans);
        // 设置适配器监听
        mGoodsAdapter.setOnCartItemClickListener(new GoodsAdapter.OnCartItemClickListener() {
            @Override
            public void onCartItemClick(ImageView goodsImg) {
                // 添加商品到购物车
                addGoodsToCart(goodsImg);
            }
        });

        // 设置适配器
        mShoppingCartListView.setAdapter(mGoodsAdapter);
    }

    private void addGoodsToCart(ImageView goodsImg) {
        // 创造出执行动画的主题goodsImg（这个图片就是执行动画的图片,从开始位置出发,经过一个抛物线（贝塞尔曲线）,移动到购物车里）
        final ImageView goods = new ImageView(this);
        goods.setImageDrawable(goodsImg.getDrawable());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        mShoppingCartContainer.addView(goods, params);

        // 得到父布局的起始点坐标（用于辅助计算动画开始/结束时的点的坐标）
        int[] parentLocation = new int[2];
        mShoppingCartContainer.getLocationInWindow(parentLocation);

        // 得到商品图片的坐标（用于计算动画开始的坐标）
        int startLocation[] = new int[2];
        goodsImg.getLocationInWindow(startLocation);

        // 得到购物车图片的坐标(用于计算动画结束后的坐标)
        int endLocation[] = new int[2];
        mIvShoppingCart.getLocationInWindow(endLocation);

        // 开始掉落的商品的起始点：商品起始点-父布局起始点+该商品图片的一半
        float startX = startLocation[0] - parentLocation[0] + goodsImg.getWidth() / 2;
        float startY = startLocation[1] - parentLocation[1] + goodsImg.getHeight() / 2;

        // 商品掉落后的终点坐标：购物车起始点-父布局起始点+购物车图片的1/5
        float toX = endLocation[0] - parentLocation[0] + mIvShoppingCart.getWidth() / 5;
        float toY = endLocation[1] - parentLocation[1];

        // 开始绘制贝塞尔曲线
        Path path = new Path();
        // 移动到起始点（贝塞尔曲线的起点）
        path.moveTo(startX, startY);
        // 使用二阶贝塞尔曲线：注意第一个起始坐标越大，贝塞尔曲线的横向距离就会越大，一般按照下面的式子取即可
        path.quadTo((startX + toX) / 2, startY, toX, toY);
        // mPathMeasure用来计算贝塞尔曲线的曲线长度和贝塞尔曲线中间插值的坐标，如果是true，path会形成一个闭环
        mPathMeasure = new PathMeasure(path, false);

        // 属性动画实现（从0到贝塞尔曲线的长度之间进行插值计算，获取中间过程的距离值）
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, mPathMeasure.getLength());
        valueAnimator.setDuration(500);

        // 匀速线性插值器
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 当插值计算进行时，获取中间的每个值，
                // 这里这个值是中间过程中的曲线长度（下面根据这个值来得出中间点的坐标值）
                float value = (Float) animation.getAnimatedValue();
                // 获取当前点坐标封装到mCurrentPosition
                // boolean getPosTan(float distance, float[] pos, float[] tan) ：
                // 传入一个距离distance(0 <= distance <= getLength())，然后会计算当前距离的坐标点和切线，pos会自动填充上坐标，这个方法很重要。
                // mCurrentPosition此时就是中间距离点的坐标值
                mPathMeasure.getPosTan(value, mCurrentPosition, null);
                // 移动的商品图片（动画图片）的坐标设置为该中间点的坐标
                goods.setTranslationX(mCurrentPosition[0]);
                goods.setTranslationY(mCurrentPosition[1]);
            }
        });

        // 开始执行动画
        valueAnimator.start();

        // 动画结束后的处理
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // 购物车商品数量加1
                goodsCount++;
                isShowCartGoodsCount();
                mTvShoppingCartCount.setText(String.valueOf(goodsCount));
                // 把执行动画的商品图片从父布局中移除
                mShoppingCartContainer.removeView(goods);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
    }
}
