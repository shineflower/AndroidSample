// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.jackie.sample.time_picker.loop_view;

// Referenced classes of package com.qingchifan.view:
//            LoopView, LoopListener

final class LoopRunnable implements Runnable {

    final LoopView loopView;

    LoopRunnable(LoopView loopview) {
        super();
        loopView = loopview;

    }

    public final void run() {
        LoopListener listener = loopView.loopListener;
        int i = LoopView.getSelectItem(loopView);
        listener.onItemSelect(i);
    }
}
