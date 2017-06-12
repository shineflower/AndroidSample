package com.jackie.sample.multiple_list.type;

import android.view.View;

import com.jackie.sample.multiple_list.holder.BaseViewHolder;
import com.jackie.sample.multiple_list.model.Normal;
import com.jackie.sample.multiple_list.model.One;
import com.jackie.sample.multiple_list.model.Three;
import com.jackie.sample.multiple_list.model.Two;

/**
 * Created by Jackie on 2017/6/12.
 */

public interface TypeFactory {
    int type(One one);

    int type(Two two);

    int type(Three three);

    int type(Normal normal);

    BaseViewHolder createViewHolder(int type, View itemView);
}
