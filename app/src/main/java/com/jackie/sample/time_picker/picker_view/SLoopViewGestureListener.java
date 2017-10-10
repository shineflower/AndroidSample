// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.jackie.sample.time_picker.picker_view;

import android.view.MotionEvent;

// Referenced classes of package com.qingchifan.view:
//            LoopView

final class SLoopViewGestureListener extends android.view.GestureDetector.SimpleOnGestureListener {

    final SingleChooseLoopView loopView;

    SLoopViewGestureListener(SingleChooseLoopView loopview) {
        loopView = loopview;
    }

    @Override
    public final boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        loopView.scrollBy(velocityY);
        return true;
    }
}
