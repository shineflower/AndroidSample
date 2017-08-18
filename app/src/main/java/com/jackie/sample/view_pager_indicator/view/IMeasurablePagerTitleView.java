package com.jackie.sample.view_pager_indicator.view;

/**
 * 可测量内容区域的指示器标题
 * 博客: http://hackware.lucode.net
 * Created by hackware on 2016/6/26.
 */
public interface IMeasurablePagerTitleView extends IPagerTitleView {
    int getContentLeft();

    int getContentTop();

    int getContentRight();

    int getContentBottom();
}
