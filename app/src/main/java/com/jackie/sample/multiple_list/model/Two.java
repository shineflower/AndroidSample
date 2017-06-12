package com.jackie.sample.multiple_list.model;

import com.jackie.sample.multiple_list.type.TypeFactory;

/**
 * Created by Jackie on 2017/6/12.
 */

public class Two implements Visitable {
    String text;

    public Two(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public int type(TypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
