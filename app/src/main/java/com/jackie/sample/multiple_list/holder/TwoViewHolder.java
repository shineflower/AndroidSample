package com.jackie.sample.multiple_list.holder;

import android.view.View;
import android.widget.TextView;

import com.jackie.sample.R;
import com.jackie.sample.multiple_list.adapter.MultipleAdapter;
import com.jackie.sample.multiple_list.model.Two;

/**
 * Created by Jackie on 2017/6/12.
 */

public class TwoViewHolder extends BaseViewHolder<Two> {
    public TwoViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setupView(Two two, int position, MultipleAdapter adapter) {
        TextView textView = (TextView) getView(R.id.title_two);
        textView.setText(two.getText());
    }
}
