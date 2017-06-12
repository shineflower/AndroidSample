package com.jackie.sample.multiple_list.type;

import android.view.View;

import com.jackie.sample.R;
import com.jackie.sample.multiple_list.holder.BaseViewHolder;
import com.jackie.sample.multiple_list.holder.NormalViewHolder;
import com.jackie.sample.multiple_list.holder.OneViewHolder;
import com.jackie.sample.multiple_list.holder.ThreeViewHolder;
import com.jackie.sample.multiple_list.holder.TwoViewHolder;
import com.jackie.sample.multiple_list.model.Normal;
import com.jackie.sample.multiple_list.model.One;
import com.jackie.sample.multiple_list.model.Three;
import com.jackie.sample.multiple_list.model.Two;

/**
 * Created by Jackie on 2017/6/12.
 */

public class TypeFactoryForList implements TypeFactory {
    private final int TYPE_RESOURCE_ONE = R.layout.item_multiple_one;
    private final int TYPE_RESOURCE_TWO = R.layout.item_multiple_two;
    private final int TYPE_RESOURCE_THREE = R.layout.item_multiple_three;
    private final int TYPE_RESOURCE_NORMAL = R.layout.item_multiple_normal;

    @Override
    public int type(One one) {
        return TYPE_RESOURCE_ONE;
    }

    @Override
    public int type(Two two) {
        return TYPE_RESOURCE_TWO;
    }

    @Override
    public int type(Three three) {
        return TYPE_RESOURCE_THREE;
    }

    @Override
    public int type(Normal normal) {
        return TYPE_RESOURCE_NORMAL;
    }

    @Override
    public BaseViewHolder createViewHolder(int type, View itemView) {
        if (TYPE_RESOURCE_ONE == type) {
            return new OneViewHolder(itemView);
        } else if (TYPE_RESOURCE_TWO == type) {
            return new TwoViewHolder(itemView);
        } else if (TYPE_RESOURCE_THREE == type){
            return new ThreeViewHolder(itemView);
        } else if (TYPE_RESOURCE_NORMAL == type){
            return new NormalViewHolder(itemView);
        }

        return null;
    }
}
