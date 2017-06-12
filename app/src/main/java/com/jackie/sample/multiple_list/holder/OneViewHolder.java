package com.jackie.sample.multiple_list.holder;

import android.view.View;
import android.widget.TextView;

import com.jackie.sample.R;
import com.jackie.sample.multiple_list.adapter.MultipleAdapter;
import com.jackie.sample.multiple_list.model.One;

/**
 * Created by Jackie on 2017/6/12.
 */

public class OneViewHolder extends BaseViewHolder<One> {
    public OneViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setupView(One one, int position, MultipleAdapter adapter) {
        TextView textView = (TextView) getView(R.id.title_one);
        textView.setText(one.getText());
    }
}
