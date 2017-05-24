package com.jackie.sample.view_pager_anim_transfer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.jackie.sample.R;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ViewPagerTransferAnimActivity extends AppCompatActivity {
    private CustomViewPager mViewPager;

    private int[] mImgResIds = { R.drawable.guide_image1, R.drawable.guide_image2, R.drawable.guide_image3};
    private List<ImageView> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_view_pager_transfer_anim);

        for (int i = 0; i < mImgResIds.length; i++) {
            ImageView imageView = new ImageView(this);
            /**
             * 尽量不要使用setImageBitmap或setImageResource或BitmapFactory.decodeResource来设置一张大图
             * 因为这些函数在完成decode后，最终都是通过java层的createBitmap来完成的，需要消耗更多内存。

             * 因此，改用先通过BitmapFactory.decodeStream方法，创建出一个bitmap，再将其设为ImageView的source，
             * decodeStream最大的秘密在于其直接调用JNI>>nativeDecodeAsset()来完成decode，
             * 无需再使用java层的createBitmap，从而节省了java层的空间。
             * 如果在读取时加上图片的Config参数，可以跟有效减少加载的内存，从而跟有效阻止抛out of Memory异常
             */
//            imageView.setBackgroundResource(mImgResIds[i]);  //在2.3上出现OOM
            imageView.setImageBitmap(generateBitmap(this, mImgResIds[i]));
            mList.add(imageView);
        }
        mViewPager = (CustomViewPager) findViewById(R.id.view_pager);
        //设置ViewPager切换动画(3.0及以上才有效)
//        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
//        mViewPager.setPageTransformer(true, new DepthPageTransformer());
//        mViewPager.setPageTransformer(true, new RotateDownPageTransformer());
        mViewPager.setAdapter(new PagerAdapter() {
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(mList.get(position));
                mViewPager.addViewToPosition(mList.get(position), position);
                return mList.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(mList.get(position));
                mViewPager.removeViewFromPosition(position);
            }

            @Override
            public int getCount() {
                return mList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }
        });
    }

    /**
     * 以最省内存的方式读取本地资源的图片
     * @param context
     * @param resId
     * @return
     */
    public static Bitmap generateBitmap(Context context, int resId){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inPurgeable = true;
        options.inInputShareable = true;
        //获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, options);
    }
}
