package com.jackie.sample.multiple_list.holder;

import android.view.View;
import android.widget.TextView;

import com.jackie.sample.R;
import com.jackie.sample.multiple_list.adapter.MultipleAdapter;
import com.jackie.sample.multiple_list.model.Normal;

/**
 * Created by Jackie on 2017/6/12.
 */

public class NormalViewHolder extends BaseViewHolder<Normal> {
    public NormalViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setupView(Normal normal, int position, MultipleAdapter adapter) {
        TextView textView = (TextView) getView(R.id.title_normal);
        textView.setText(normal.getText());
    }
}
