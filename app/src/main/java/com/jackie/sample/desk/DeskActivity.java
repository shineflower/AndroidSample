package com.jackie.sample.desk;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jackie.sample.R;
import com.jackie.sample.adapter.SampleAdapter;
import com.jackie.sample.alipay.AliPayActivity;
import com.jackie.sample.android_l.AndroidLCircularRevealActivity;
import com.jackie.sample.android_l.AndroidLTouchFeedbackActivity;
import com.jackie.sample.animator.OpenDoor3DActivity;
import com.jackie.sample.animator.ShoppingCartBezierActivity;
import com.jackie.sample.animator.TurnOffTvActivity;
import com.jackie.sample.animator.ValueAnimatorActivity;
import com.jackie.sample.arc_menu.ArcMenuActivity;
import com.jackie.sample.bean.SampleBean;
import com.jackie.sample.clock.ClockActivity;
import com.jackie.sample.data_binding.DataBindingActivity;
import com.jackie.sample.edit_text.SearchEditTextActivity;
import com.jackie.sample.face_detect.FaceDetectActivity;
import com.jackie.sample.file_stream_recorder.FileStreamRecorderActivity;
import com.jackie.sample.gradient.LinearGradientActivity;
import com.jackie.sample.image_processing.ImageProcessingActivity;
import com.jackie.sample.indexable_contact.IndexableContactActivity;
import com.jackie.sample.jd_tmall_refresh.JdTmallRefreshActivity;
import com.jackie.sample.lock_pattern.LockPatternActivity;
import com.jackie.sample.lucky_wheel.LuckyWheelActivity;
import com.jackie.sample.material_design.MaterialDesignActivity;
import com.jackie.sample.multiple_download.DownloadActivity;
import com.jackie.sample.okhttp3.Okhttp3Activity;
import com.jackie.sample.progress_bar.ProgressBarActivity;
import com.jackie.sample.progress_bar.ProgressLinearLayoutActivity;
import com.jackie.sample.ripple.RippleActivity;
import com.jackie.sample.scratch_card.ScratchCardActivity;
import com.jackie.sample.scroll_view.ScrollViewActivity;
import com.jackie.sample.share_element.PhotoActivity;
import com.jackie.sample.share_element.ShareElementFromActivity;
import com.jackie.sample.sin.SinActivity;
import com.jackie.sample.slide_delete.SlideDeleteActivity;
import com.jackie.sample.sliding_menu.SlidingMenuActivity;
import com.jackie.sample.text_view.TextViewActivity;
import com.jackie.sample.timeline.TimelineActivity;
import com.jackie.sample.tree.TreeActivity;
import com.jackie.sample.view_pager_anim_transfer.ViewPagerTransferAnimActivity;
import com.jackie.sample.wechat_image_picker.WechatImagePickerActivity;
import com.jackie.sample.wechat_recorder.WechatRecorderActivity;
import com.jackie.sample.wechat_tab.WechatTabActivity;
import com.jackie.sample.zoom_image_view.ZoomActivity;

import java.util.LinkedList;

public class DeskActivity extends Activity {
    private RecyclerView mRecyclerView;
    private View mRootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRootView = getLayoutInflater().inflate(R.layout.activity_desk, null);
        setContentView(mRootView);

        initView();
        hideNavigationBar();//控制底部的NavigationBar的显示和隐藏的效果
    }

    private void initView() {
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.rv_desk_content);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        LinkedList<SampleBean> linkedList = new LinkedList<>();
        SampleBean sampleBean;

        sampleBean = new SampleBean();
        sampleBean.setTitle("高仿微信主界面(自定义指示器和消息提醒)");
        sampleBean.setClassName(WechatTabActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("图像处理集合");
        sampleBean.setClassName(ImageProcessingActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("ViewPager切换动画");
        sampleBean.setClassName(ViewPagerTransferAnimActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("图片预览和多点触控");
        sampleBean.setClassName(ZoomActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("自定义索引查询联系人");
        sampleBean.setClassName(IndexableContactActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("自定义实现刮刮卡效果");
        sampleBean.setClassName(ScratchCardActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("自定义树形控件");
        sampleBean.setClassName(TreeActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("弧形菜单");
        sampleBean.setClassName(ArcMenuActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("仿QQ侧滑菜单");
        sampleBean.setClassName(SlidingMenuActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("仿微信语音聊天");
        sampleBean.setClassName(WechatRecorderActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("仿微信图片选择器");
        sampleBean.setClassName(WechatImagePickerActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("自定义图案解锁");
        sampleBean.setClassName(LockPatternActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("多线程断点续传下载");
        sampleBean.setClassName(DownloadActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("人脸检测");
        sampleBean.setClassName(FaceDetectActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("幸运转盘");
        sampleBean.setClassName(LuckyWheelActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("Data Binding");
        sampleBean.setClassName(DataBindingActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("阿里支付密码框");
        sampleBean.setClassName(AliPayActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("Okhttp3");
        sampleBean.setClassName(Okhttp3Activity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("多样式的ProgressBar");
        sampleBean.setClassName(ProgressBarActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("高仿京东天猫下拉刷新");
        sampleBean.setClassName(JdTmallRefreshActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("基于文件模式和字节流模式的录音");
        sampleBean.setClassName(FileStreamRecorderActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("Material Design");
        sampleBean.setClassName(MaterialDesignActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("共享元素的界面跳转");
        sampleBean.setClassName(ShareElementFromActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("线性色彩渐变");
        sampleBean.setClassName(LinearGradientActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("Sin正弦曲线");
        sampleBean.setClassName(SinActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("一个Canvas时钟");
        sampleBean.setClassName(ClockActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("仿理财产品的钱数额的变化动画");
        sampleBean.setClassName(ValueAnimatorActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("RecyclerView滑动删除");
        sampleBean.setClassName(SlideDeleteActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("有界波纹和无界波纹效果(可以放到任何View上面)");
        sampleBean.setClassName(RippleActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("可以上下进行粘性滑动的ScrollView");
        sampleBean.setClassName(ScrollViewActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("三维开门一样的效果");
        sampleBean.setClassName(OpenDoor3DActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("模拟电视的关机动画");
        sampleBean.setClassName(TurnOffTvActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("按钮波纹与波纹色定制");
        sampleBean.setClassName(AndroidLTouchFeedbackActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("Android L 动画新特性");
        sampleBean.setClassName(AndroidLCircularRevealActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("贝塞尔曲线的购物车添加动画");
        sampleBean.setClassName(ShoppingCartBezierActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("文字特效");
        sampleBean.setClassName(TextViewActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("用于搜索的标签添加与管理控件");
        sampleBean.setClassName(SearchEditTextActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("共享元素相关测试加入Fragment");
        sampleBean.setClassName(PhotoActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("自定义柱状图进度条");
        sampleBean.setClassName(ProgressLinearLayoutActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("自定义垂直时间轴布局");
        sampleBean.setClassName(TimelineActivity.class);
        linkedList.add(sampleBean);

        SampleAdapter sampleAdapter = new SampleAdapter(this, linkedList);
        mRecyclerView.setAdapter(sampleAdapter);
        sampleAdapter.notifyDataSetChanged();
    }

    public void hideNavigationBar() {
        int uiFlags = //View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                //View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION; // hide nav bar

        if (android.os.Build.VERSION.SDK_INT >= 19){
            uiFlags |= 0x00001000;    //SYSTEM_UI_FLAG_IMMERSIVE_STICKY: hide navigation bars - compatibility: building API level is lower thatn 19, use magic number directly for higher API target level
        } else {
            uiFlags |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
        }

        getWindow().getDecorView().setSystemUiVisibility(uiFlags);
    }
}
