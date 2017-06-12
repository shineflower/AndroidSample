package com.jackie.sample.multiple_list.holder;

import android.view.View;
import android.widget.TextView;

import com.jackie.sample.R;
import com.jackie.sample.multiple_list.adapter.MultipleAdapter;
import com.jackie.sample.multiple_list.model.Three;

/**
 * Created by Jackie on 2017/6/12.
 */

public class ThreeViewHolder extends BaseViewHolder<Three> {
    public ThreeViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setupView(Three three, int position, MultipleAdapter adapter) {
        TextView textView = (TextView) getView(R.id.title_three);
        textView.setText(three.getText());
    }
}
