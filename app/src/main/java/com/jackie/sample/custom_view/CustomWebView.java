package com.jackie.sample.custom_view;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.jackie.sample.listener.OnActionSelectListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jackie on 2017/7/3.
 */

public class CustomWebView extends WebView {
    private ActionMode mActionMode;
    private List<String> mActionList = new ArrayList<>();
    private OnActionSelectListener mOnActionSelectListener;

    public CustomWebView(Context context) {
        super(context);
    }

    public CustomWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 处理item，处理点击
     * @param actionMode
     * @return
     */
    private ActionMode resolveActionMode(ActionMode actionMode) {
        if (actionMode != null) {
            Menu menu = actionMode.getMenu();
            mActionMode = actionMode;
            menu.clear();

            for (int i = 0; i < mActionList.size(); i++) {
                menu.add(mActionList.get(i));
            }

            for (int i = 0; i < menu.size(); i++) {
                MenuItem menuItem = menu.getItem(i);

                menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        getSelectedData((String) item.getTitle());
                        releaseAction();
                        return true;
                    }
                });
            }
        }

        mActionMode = actionMode;
        return actionMode;
    }

    @Override
    public ActionMode startActionMode(ActionMode.Callback callback) {
        ActionMode actionMode = super.startActionMode(callback);
        return resolveActionMode(actionMode);
    }

    @Override
    public ActionMode startActionMode(ActionMode.Callback callback, int type) {
        ActionMode actionMode = super.startActionMode(callback, type);
        return resolveActionMode(actionMode);
    }

    private void releaseAction() {
        if (mActionMode != null) {
            mActionMode.finish();
            mActionMode = null;
        }
    }

    /**
     * 点击的时候，获取网页选择的文本，回调到原生中的js接口
     * @param title 传入点击的item文本，一起通过js返回给原生接口
     */
    private void getSelectedData(String title) {
        String js = "(function getSelectedText() {" +
                "var title = \"" + title + "\";" +
                "var txt;" +
                "if (window.getSelection) {" +
                "txt = window.getSelection().toString();" +
                "} else if (window.document.getSelection) {" +
                "txt = window.document.getSelection().toString();" +
                "} else if (window.document.selection) {" +
                "txt = window.document.selection.createRange().text;" +
                "}" +
                "JSInterface.callback(title, txt);" +
                "})()";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            evaluateJavascript("javascript:" + js, null);
        } else {
            loadUrl("javascript:" + js);
        }
    }

    public void linkJSInterface() {
        addJavascriptInterface(new ActionSelectInterface(), "JSInterface");
    }

    /**
     * 设置弹出action列表
     * @param actionList
     */
    public void setActionList(List<String> actionList) {
        mActionList = actionList;
    }

    /**
     * 设置点击回调
     * @param onActionSelectListener
     */
    public void setOnActionSelectListener(OnActionSelectListener onActionSelectListener) {
        this.mOnActionSelectListener = onActionSelectListener;
    }

    /**
     * 隐藏消失Action
     */
    public void dismissAction() {
        releaseAction();
    }

    /**
     * js选中的回掉接口
     */
    private class ActionSelectInterface {

        @JavascriptInterface
        public void callback(String title, String value) {
            if(mOnActionSelectListener != null) {
                mOnActionSelectListener.onClick(title, value);
            }
        }
    }
}
