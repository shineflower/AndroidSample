package com.jackie.sample.desk;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jackie.sample.R;
import com.jackie.sample.adapter.SampleAdapter;
import com.jackie.sample.adder_subtractor.AdderSubtractorActivity;
import com.jackie.sample.address.AddressSelectActivity;
import com.jackie.sample.alipay.AliPayActivity;
import com.jackie.sample.android_l.AndroidLCircularRevealActivity;
import com.jackie.sample.android_l.AndroidLTouchFeedbackActivity;
import com.jackie.sample.animator.OpenDoor3DActivity;
import com.jackie.sample.animator.ShoppingCartBezierActivity;
import com.jackie.sample.animator.TurnOffTvActivity;
import com.jackie.sample.animator.ValueAnimatorActivity;
import com.jackie.sample.arc_menu.ArcMenuActivity;
import com.jackie.sample.bean.SampleBean;
import com.jackie.sample.blur.BlurActivity;
import com.jackie.sample.card_piece.CardPieceActivity;
import com.jackie.sample.card_swipe.CardSwipeActivity;
import com.jackie.sample.circle_menu.CircleMenuActivity;
import com.jackie.sample.circle_range.CircleRangeActivity;
import com.jackie.sample.clip_image.ClipImageActivity;
import com.jackie.sample.clock.ClockActivity;
import com.jackie.sample.contact.IndexableContactActivity;
import com.jackie.sample.contact.MeizuContactActivity;
import com.jackie.sample.count_down.BusinessCountDownActivity;
import com.jackie.sample.count_down.SMSCountDownActivity;
import com.jackie.sample.data_binding.DataBindingActivity;
import com.jackie.sample.divider.DividerActivity;
import com.jackie.sample.drag_exchange.ExchangeActivity;
import com.jackie.sample.edit_text.EditTextActivity;
import com.jackie.sample.face_detect.FaceDetectActivity;
import com.jackie.sample.falling.FallingActivity;
import com.jackie.sample.file_stream_recorder.FileStreamRecorderActivity;
import com.jackie.sample.finger.FingerActivity;
import com.jackie.sample.float_window.FloatWindowActivity;
import com.jackie.sample.flow_layout.FlowLayoutActivity;
import com.jackie.sample.framework.FrameworkActivity;
import com.jackie.sample.gmail.GmailActivity;
import com.jackie.sample.gradient.LinearGradientActivity;
import com.jackie.sample.image_code.ImageCodeActivity;
import com.jackie.sample.image_filling.ImageFillingActivity;
import com.jackie.sample.image_processing.ImageProcessingActivity;
import com.jackie.sample.jd_tmall_refresh.JdTmallRefreshActivity;
import com.jackie.sample.keyboard.KeyboardActivity;
import com.jackie.sample.list_view.LoopCompletenessListViewActivity;
import com.jackie.sample.lock_pattern.LockPatternActivity;
import com.jackie.sample.lucky_wheel.LuckyWheelActivity;
import com.jackie.sample.map.AMapActivity;
import com.jackie.sample.map.BaiduMapActivity;
import com.jackie.sample.material_design.MaterialDesignActivity;
import com.jackie.sample.material_design.SwipeRefreshLayoutActivity;
import com.jackie.sample.meituan_location.MeiTuanLocationActivity;
import com.jackie.sample.metro.MetroActivity;
import com.jackie.sample.mm_alert.SendToWXActivity;
import com.jackie.sample.multiple_download.DownloadActivity;
import com.jackie.sample.multiple_list.MultipleListActivity;
import com.jackie.sample.newer_guide.NewerGuideActivity;
import com.jackie.sample.notification.NotificationActivity;
import com.jackie.sample.popup_window.PopupWindowActivity;
import com.jackie.sample.progress_bar.ProgressBarActivity;
import com.jackie.sample.progress_bar.ProgressLinearLayoutActivity;
import com.jackie.sample.recorder_timing.RecorderTimingActivity;
import com.jackie.sample.ripple.RippleActivity;
import com.jackie.sample.scratch_card.ScratchCardActivity;
import com.jackie.sample.scroll_view.ElasticScrollViewActivity;
import com.jackie.sample.scroll_view.LoopCompletenessScrollViewActivity;
import com.jackie.sample.select_contact.SelectContactActivity;
import com.jackie.sample.sesame_credit.SesameCreditActivity;
import com.jackie.sample.share_element.PhotoActivity;
import com.jackie.sample.share_element.ShareElementFromActivity;
import com.jackie.sample.sin.SinActivity;
import com.jackie.sample.skin_support.SkinSupportActivity;
import com.jackie.sample.slide_delete.SlideDeleteActivity;
import com.jackie.sample.sliding_menu.SlidingMenuActivity;
import com.jackie.sample.switcher.SwitcherActivity;
import com.jackie.sample.text_view.TextViewActivity;
import com.jackie.sample.time_picker.TimePickerActivity;
import com.jackie.sample.timeline.TimelineActivity;
import com.jackie.sample.tree.TreeActivity;
import com.jackie.sample.utils.ScreenUtils;
import com.jackie.sample.video_player.VideoPlayerActivity;
import com.jackie.sample.view_pager_anim_transfer.ViewPagerTransferBannerAnimActivity;
import com.jackie.sample.view_pager_anim_transfer.ViewPagerTransferGuideAnimActivity;
import com.jackie.sample.view_pager_indicator.ViewPagerColorTrackIndicatorActivity;
import com.jackie.sample.view_pager_indicator.ViewPagerReboundIndicatorActivity;
import com.jackie.sample.view_pager_indicator.ViewPagerTriangleIndicatorActivity;
import com.jackie.sample.volume_control.VolumeControlActivity;
import com.jackie.sample.web_view.WebViewActivity;
import com.jackie.sample.wechat_camera.WechatCameraActivity;
import com.jackie.sample.wechat_image_picker.WechatImagePickerActivity;
import com.jackie.sample.wechat_image_upload.WechatImageUploadActivity;
import com.jackie.sample.wechat_recorder.WechatRecorderActivity;
import com.jackie.sample.wechat_slide.WechatSlideActivity;
import com.jackie.sample.wechat_tab.WechatTabWithIndicatorActivity;
import com.jackie.sample.wechat_tab.WechatTabWithoutIndicatorActivity;
import com.jackie.sample.zhihu.ZhiHuAdActivity;
import com.jackie.sample.zoom_image.LargeImageActivity;
import com.jackie.sample.zoom_image.ZoomImageActivity;
import com.jackie.sample.zxing.ZxingActivity;

import java.util.LinkedList;

public class DeskActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_desk);

        initView();

        ScreenUtils.hideNavigationBar(this);
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_desk_content);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        LinkedList<SampleBean> linkedList = new LinkedList<>();
        SampleBean sampleBean;

        sampleBean = new SampleBean();
        sampleBean.setTitle("短信验证码倒计时");
        sampleBean.setClassName(SMSCountDownActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("电商抢购倒计时");
        sampleBean.setClassName(BusinessCountDownActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("图片验证码");
        sampleBean.setClassName(ImageCodeActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("自定义Switch组件");
        sampleBean.setClassName(SwitcherActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("选择联系人列表");
        sampleBean.setClassName(SelectContactActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("仿微信主界面(自定义指示器和消息提醒)");
        sampleBean.setClassName(WechatTabWithIndicatorActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("仿微信主界面(图标颜色追踪)");
        sampleBean.setClassName(WechatTabWithoutIndicatorActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("打造炫酷ViewPager字体颜色追踪指示器");
        sampleBean.setClassName(ViewPagerColorTrackIndicatorActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("打造炫酷ViewPager三角形指示器");
        sampleBean.setClassName(ViewPagerTriangleIndicatorActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("打造炫酷ViewPager回弹指示器");
        sampleBean.setClassName(ViewPagerReboundIndicatorActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("仿IOS滑动分页器");
        sampleBean.setClassName(DividerActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("图像处理集合");
        sampleBean.setClassName(ImageProcessingActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("ViewPager炫酷切换效果(适用于Guide)");
        sampleBean.setClassName(ViewPagerTransferGuideAnimActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("ViewPager炫酷切换效果(适用于Banner)");
        sampleBean.setClassName(ViewPagerTransferBannerAnimActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("图片预览和多点触控");
        sampleBean.setClassName(ZoomImageActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("高清加载巨图方案");
        sampleBean.setClassName(LargeImageActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("仿微信头像裁切");
        sampleBean.setClassName(ClipImageActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("自定义索引查询联系人");
        sampleBean.setClassName(IndexableContactActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("仿魅族手机通讯录界面");
        sampleBean.setClassName(MeizuContactActivity.class);
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
        sampleBean.setTitle("仿建行圆形菜单");
        sampleBean.setClassName(CircleMenuActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("流式布局");
        sampleBean.setClassName(FlowLayoutActivity.class);
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
        sampleBean.setTitle("仿微信对话列表滑动删除效果");
        sampleBean.setClassName(WechatSlideActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("仿微信拍照效果");
        sampleBean.setClassName(WechatCameraActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("仿微信朋友圈9图上传选择器");
        sampleBean.setClassName(WechatImageUploadActivity.class);
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
        sampleBean.setTitle("各种网络加载框架");
        sampleBean.setClassName(FrameworkActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("多样式的ProgressBar");
        sampleBean.setClassName(ProgressBarActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("仿京东天猫下拉刷新");
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
        sampleBean.setTitle("GridView长按交换位置");
        sampleBean.setClassName(ExchangeActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("有界波纹和无界波纹效果(可以放到任何View上面)");
        sampleBean.setClassName(RippleActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("可以上下进行粘性滑动的ScrollView");
        sampleBean.setClassName(ElasticScrollViewActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("可以循环滑动并且保证条目完整性的ScrollView");
        sampleBean.setClassName(LoopCompletenessScrollViewActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("可以循环滑动并且保证条目完整性的ListView");
        sampleBean.setClassName(LoopCompletenessListViewActivity.class);
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
        sampleBean.setTitle("自定义TextView文字特效");
        sampleBean.setClassName(TextViewActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("自定义EditText文字特效");
        sampleBean.setClassName(EditTextActivity.class);
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

        sampleBean = new SampleBean();
        sampleBean.setTitle("快速实现动态模糊效果");
        sampleBean.setClassName(BlurActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("仿芝麻信用雷达分布图");
        sampleBean.setClassName(SesameCreditActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("视频音量调控");
        sampleBean.setClassName(VolumeControlActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("基于Google Zxing实现二维码、条形码扫描");
        sampleBean.setClassName(ZxingActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("圆形仪表盘，实现展示不同级别范围");
        sampleBean.setClassName(CircleRangeActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("仿QQ手机管家小火箭效果实现");
        sampleBean.setClassName(FloatWindowActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("仿IOS微信分享对话框");
        sampleBean.setClassName(SendToWXActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("仿Win8的metro的UI界面");
        sampleBean.setClassName(MetroActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("不规则图像填充");
        sampleBean.setClassName(ImageFillingActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("百度地图");
        sampleBean.setClassName(BaiduMapActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("高德地图");
        sampleBean.setClassName(AMapActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("优雅的实现多类型列表");
        sampleBean.setClassName(MultipleListActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("实现Gmail的收件箱效果");
        sampleBean.setClassName(GmailActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("封装一个通用的PopupWindow");
        sampleBean.setClassName(PopupWindowActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("通知");
        sampleBean.setClassName(NotificationActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("自定义视频播放器");
        sampleBean.setClassName(VideoPlayerActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("自定义倒计时录音交互动效");
        sampleBean.setClassName(RecorderTimingActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("自定义键盘");
        sampleBean.setClassName(KeyboardActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("数字加减器");
        sampleBean.setClassName(AdderSubtractorActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("四级地址选择器");
        sampleBean.setClassName(AddressSelectActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("WebView自定义长按选择，实现收藏 / 分享选中文本");
        sampleBean.setClassName(WebViewActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("碎片飘落效果控件");
        sampleBean.setClassName(FallingActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("项目拼卡效果");
        sampleBean.setClassName(CardPieceActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("仿探探层叠卡片效果");
        sampleBean.setClassName(CardSwipeActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("指纹识别");
        sampleBean.setClassName(FingerActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("新手引导");
        sampleBean.setClassName(NewerGuideActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("自定义滚轮选择器");
        sampleBean.setClassName(TimePickerActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("自定义换肤");
        sampleBean.setClassName(SkinSupportActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("仿知乎列表广告展示效果");
        sampleBean.setClassName(ZhiHuAdActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("自定义下拉刷新上拉加载控件");
        sampleBean.setClassName(SwipeRefreshLayoutActivity.class);
        linkedList.add(sampleBean);

        sampleBean = new SampleBean();
        sampleBean.setTitle("仿美团城市定位");
        sampleBean.setClassName(MeiTuanLocationActivity.class);
        linkedList.add(sampleBean);

        SampleAdapter sampleAdapter = new SampleAdapter(this, linkedList);
        mRecyclerView.setAdapter(sampleAdapter);
    }
}
