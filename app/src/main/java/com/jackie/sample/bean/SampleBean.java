package com.jackie.sample.bean;

/**
 * Created by Jackie on 2017/5/5.
 * Sample列表的实体类
 */

public class SampleBean<T> {
    private String title;
    private Class<T> className;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Class<T> getClassName() {
        return className;
    }

    public void setClassName(Class<T> className) {
        this.className = className;
    }
}
