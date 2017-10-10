// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.jackie.sample.time_picker.picker_view;

// Referenced classes of package com.qingchifan.view:
//            LoopView, OnItemSelectedListener

final class OnItemSelectedRunnable implements Runnable {
    final SingleChooseLoopView loopView;

    OnItemSelectedRunnable(SingleChooseLoopView loopview) {
        loopView = loopview;
    }

    @Override
    public final void run() {
        loopView.onItemSelectedListener.onItemSelected(loopView.getSelectedItem());
    }
}
