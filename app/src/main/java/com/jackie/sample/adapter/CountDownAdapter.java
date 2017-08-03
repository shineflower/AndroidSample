package com.jackie.sample.adapter;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jackie.sample.R;
import com.jackie.sample.bean.CountDownBean;

import java.util.List;

public class CountDownAdapter extends RecyclerView.Adapter<CountDownAdapter.ViewHolder> {
    private Context mContext;
    private List<CountDownBean> mCountDownList;
    private SparseArray<CountDownTimer> mCountDownMap;

    public CountDownAdapter(Context context, List<CountDownBean> countDownList) {
        this.mContext = context;
        this.mCountDownList = countDownList;

        mCountDownMap = new SparseArray<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_count_down, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final CountDownBean countDownBean = mCountDownList.get(position);

        Glide.with(mContext).load(countDownBean.getUrl()).into(holder.icon);

        holder.title.setText(countDownBean.getTitle());
        holder.value.setText(countDownBean.getValue());
        holder.number.setText(countDownBean.getNumber());
        holder.name.setText(countDownBean.getName());
        holder.date.setText(countDownBean.getDate());

        //将前一个计时器清除
        if (holder.countDownTimer != null) {
            holder.countDownTimer.cancel();
        }

        long time = countDownBean.getTime();
        time = time - System.currentTimeMillis();

        if (time > 0) {
            holder.countDownTimer = new CountDownTimer(time, 10) {  //倒计时
                @Override
                public void onTick(long millisUntilFinished) {
                    long minute = (millisUntilFinished / 1000) % 3600 / 60;
                    long second = (millisUntilFinished / 1000) % 3600 % 60;
                    long millisecond = millisUntilFinished % 1000 / 10;

                    holder.time.setText(convert(minute) + ":" +  convert(second) + ":" + convert(millisecond));
                }

                @Override
                public void onFinish() {
                    publish(holder);
                }
            }.start();

            mCountDownMap.put(holder.time.hashCode(), holder.countDownTimer);
        } else {
            countDownBean.setPublished(true);  //揭晓
            holder.time.setText("00:00:00");   //时间置空
        }

        if(countDownBean.isPublished()){
            holder.publishLayout.setVisibility(View.VISIBLE);
            holder.timeLayout.setVisibility(View.GONE);

            Glide.with(mContext).load(R.drawable.has_puslished).placeholder(R.drawable.place_holder).into(holder.state);
        } else {
            holder.publishLayout.setVisibility(View.GONE);
            holder.timeLayout.setVisibility(View.VISIBLE);

            Glide.with(mContext).load(R.drawable.to_be_published).placeholder(R.drawable.place_holder).into(holder.state);
        }
    }

    @Override
    public int getItemCount() {
        return mCountDownList !=  null ? mCountDownList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView icon, state;
        private TextView title, value, number, name, time, date;
        private LinearLayout publishLayout, timeLayout;
        private CountDownTimer countDownTimer;

        public ViewHolder(View itemView) {
            super(itemView);

            icon = (ImageView) itemView.findViewById(R.id.icon);
            state = (ImageView) itemView.findViewById(R.id.state);
            title = (TextView) itemView.findViewById(R.id.title);
            value = (TextView) itemView.findViewById(R.id.value);
            number = (TextView) itemView.findViewById(R.id.number);
            name = (TextView) itemView.findViewById(R.id.name);
            time = (TextView) itemView.findViewById(R.id.time);
            date = (TextView) itemView.findViewById(R.id.date);

            publishLayout = (LinearLayout) itemView.findViewById(R.id.publish_layout);
            timeLayout = (LinearLayout) itemView.findViewById(R.id.time_layout);
        }
    }

    private void publish(ViewHolder holder) {
        int duration = 1500;

        Animation scaleAnimation = new ScaleAnimation(1.0f, 1.2f, 1.0f, 1.2f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(duration);

        Animation alphaAnimation = new AlphaAnimation(1.0f, 0.3f);
        alphaAnimation.setDuration(duration);

        AnimationSet animationSet = new AnimationSet(true);

        scaleAnimation.setFillAfter(false);
        alphaAnimation.setFillAfter(false);
        animationSet.setFillAfter(false);

        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);

        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                notifyDataSetChanged();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        holder.timeLayout.startAnimation(animationSet);
    }

    public String convert(long time) {
        if (time < 10) {
            return "0" + time;
        }

        return time + "";
    }

    /**
     * 清空资源
     */
    public void cancelAllTimers() {
        if (mCountDownMap == null) {
            return;
        }

        for (int i = 0, length = mCountDownMap.size(); i < length; i++) {
            CountDownTimer countDownTimer = mCountDownMap.get(mCountDownMap.keyAt(i));

            if (countDownTimer != null) {
                countDownTimer.cancel();
            }
        }
    }
}