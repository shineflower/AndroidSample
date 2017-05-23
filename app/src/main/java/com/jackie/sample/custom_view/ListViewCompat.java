package com.jackie.sample.custom_view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

import com.jackie.sample.bean.MessageItem;

/**
 * Created by Jackie on 2017/5/23.
 */

public class ListViewCompat extends ListView {
    private SlideView mSlideView;

    public ListViewCompat(Context context) {
        super(context);
    }

    public ListViewCompat(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListViewCompat(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void shrinkListItem(int position) {
        View childView = getChildAt(position);

        if (childView != null) {
            try {
                ((SlideView) childView).shrink();
            } catch (ClassCastException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                int x = (int) ev.getX();
                int y = (int) ev.getY();

                int position = pointToPosition(x, y);
                if (position != INVALID_POSITION) {
                    MessageItem messageItem = (MessageItem) getItemAtPosition(position);
                    mSlideView = messageItem.getSlideView();
                }

                break;
        }

        if (mSlideView != null) {
            mSlideView.onRequireTouchEvent(ev);
        }

        return super.onTouchEvent(ev);
    }
}
