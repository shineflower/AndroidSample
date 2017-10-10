package com.jackie.sample.time_picker.loop_view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.jackie.sample.R;

import java.util.List;
import java.util.Timer;

public class LoopView extends View {

    Timer mTimer;
    int totalScrollY;
    Handler handler;
    LoopListener loopListener;
    private GestureDetector gestureDetector;
    private int mSelectItem;
    private GestureDetector.SimpleOnGestureListener simpleOnGestureListener;
    Context context;
    Paint paintA;
    Paint paintB;
    Paint paintC;
    List arrayList;
    int textSize;
    int g;
    int h;
    int colorGray;
    int colorBlack;
    int colorGrayLight;
    float l;
    boolean isLoop;
    int n;
    int o;
    int mCurrentItem;
    int positon;
    int r;
    int s;
    int t;
    int u;
    int v;
    int w;
    float x;
    float y;
    float z;

    public LoopView(Context context) {
        super(context);
        initLoopView(context);
    }

    public LoopView(Context context, AttributeSet attributeset) {
        super(context, attributeset);
        initLoopView(context);
    }

    public LoopView(Context context, AttributeSet attributeset, int i1) {
        super(context, attributeset, i1);
        initLoopView(context);
    }

    private void initLoopView(Context context) {
        textSize = 0;
        colorGray = 0xffa7a7a7; //0xffafafaf;
        //colorBlack = 0xff572dc9;// 0xff313131;
        colorBlack = 0x0971ce;
        colorGrayLight = 0xffdcdcdc; //0xffc5c5c5;
        l = 2.0F;
        isLoop = true;
        positon = -1;
        r = 9;
        x = 0.0F;
        y = 0.0F;
        z = 0.0F;
        totalScrollY = 0;
        simpleOnGestureListener = new LoopViewGestureListener(this);
        handler = new MessageHandler(this);
        this.context = context;
        setTextSize(16F);
    }

    static int getSelectItem(LoopView loopview) {
        return loopview.getCurrentItem();
    }

    static void b(LoopView loopview) {
        loopview.f();
    }

    private void d() {
        if (arrayList == null) {
            return;
        }
        paintA = new Paint();
        paintA.setColor(colorGray);
        paintA.setAntiAlias(true);
        paintA.setTypeface(Typeface.MONOSPACE);
        paintA.setTextSize(textSize);
        paintA.setTextAlign(Paint.Align.CENTER);
        paintB = new Paint();
        paintB.setColor(getResources().getColor(R.color.color_337cff));
        paintB.setAntiAlias(true);
        paintB.setTextScaleX(1.05F);
        paintB.setTypeface(Typeface.MONOSPACE);
        paintB.setTextSize(textSize);
        paintB.setTextAlign(Paint.Align.CENTER);
        paintC = new Paint();
        paintC.setTextAlign(Paint.Align.CENTER);
        paintC.setColor(colorGrayLight);
        paintC.setAntiAlias(true);
        paintC.setTypeface(Typeface.MONOSPACE);
        paintC.setTextSize(textSize);
        if (android.os.Build.VERSION.SDK_INT >= 11) {
            setLayerType(1, null);
        }
        gestureDetector = new GestureDetector(context, simpleOnGestureListener);
        gestureDetector.setIsLongpressEnabled(false);
        e();
        t = (int) ((float) h * l * (float) (r - 1));
        s = (int) ((double) (t * 2) / Math.PI);
        u = (int) ((double) t / Math.PI);
        v = g + textSize;
        n = (int) (((float) s - l * (float) h) / 2.0F);
        o = (int) (((float) s + l * (float) h) / 2.0F);
        if (positon == -1) {
            if (isLoop) {
                positon = (arrayList.size() + 1) / 2;
            } else {
                positon = 0;
            }
        }
        mCurrentItem = positon;
    }

    private void e() {
        Rect rect = new Rect();
//        for (int i1 = 0; i1 < arrayList.size(); i1++) {
//            // String s1 = (String) arrayList.get(i1);
//            // wangpeng:调整可触摸区域的宽度为4个字符
//            paintB.getTextBounds("00000", 0, "00000".length(), rect);
//            int j1 = rect.width();
//            // wangpeng:调整区域增大2倍，提升体验。
//            j1 = (int) (j1 * 2.0f);
//            if (j1 > g) {
//                g = j1;
//            }
//            paintB.getTextBounds("\u661F\u671F", 0, 2, rect);
//            j1 = rect.height() + 20;
//            if (j1 > h) {
//                h = j1;
//            }
//        }

        for (int i = 0; i < arrayList.size(); i++) {
            String s1 = (String) arrayList.get(i);
            paintB.getTextBounds("0000", 0, "0000".length(), rect);
            int textWidth = rect.width();
            if (textWidth > g) {
                g = (int) (textWidth * 1.8f);
            }
            paintB.getTextBounds("\u661F\u671F", 0, 2, rect); // 星期
            int textHeight = rect.height() + 20;
            if (textHeight > h) {
                h = textHeight;
            }
        }

    }

    private void f() {
        int i1 = (int) ((float) totalScrollY % (l * (float) h));
        Timer timer = new Timer();
        mTimer = timer;
        timer.schedule(new MTimer(this, i1, timer), 0L, 10L);
    }

    public final void setNotLoop() {
        isLoop = false;
    }

    /**
     * 设置是否循环滚动
     *
     * @param cyclic
     */
    public final void setCyclic(boolean cyclic) {
        isLoop = cyclic;
    }

    public final void setTextSize(float size) {
        if (size > 0.0F) {
            textSize = (int) (context.getResources().getDisplayMetrics().density * size);
        }
    }

    public final void setCurrentItem(int position) {
        this.positon = position;
        totalScrollY = 0;
        f();
        invalidate();
    }

    public final void setListener(LoopListener LoopListener) {
        loopListener = LoopListener;
    }

    public final void setArrayList(List arraylist) {
        arrayList = arraylist;
        d();
        invalidate();
    }

    public final int getCurrentItem() {
        if (mCurrentItem <= 0) {
            return 0;
        }
        return mCurrentItem;
    }

    public final String getCurrentItemValue(){
        return String.valueOf(arrayList.get(getCurrentItem())).trim();
    }

    protected final void b(float f1) {
        Timer timer = new Timer();
        mTimer = timer;
        timer.schedule(new LoopTimerTask(this, f1, timer), 0L, 20L);
    }

    protected final void b(int i1) {
        Timer timer = new Timer();
        mTimer = timer;
        timer.schedule(new MyTimerTask(this, i1, timer), 0L, 20L);
    }

    protected final void c() {
        if (loopListener != null) {
            (new Handler()).postDelayed(new LoopRunnable(this), 200L);
        }
    }

    protected void onDraw(Canvas canvas) {
        // wangpeng: 触摸分析辅助线
//         canvas.drawLine( 0 ,getHeight() / 2, getWidth(), getHeight() / 2, paintA);
//         canvas.drawLine( getWidth() -70  ,0 , getWidth() - 80, getHeight() , paintA);

        String as[];
        if (arrayList == null) {
            super.onDraw(canvas);
            return;
        }
        as = new String[r];
        w = (int) ((float) totalScrollY / (l * (float) h));
        mCurrentItem = positon + w % arrayList.size();
        int i1;
        if (!isLoop) {
            if (mCurrentItem < 0) {
                mCurrentItem = 0;
            }
            if (mCurrentItem > arrayList.size() - 1) {
                mCurrentItem = arrayList.size() - 1;
            }
            // break;
        } else {
            if (mCurrentItem < 0) {
                mCurrentItem = arrayList.size() + mCurrentItem;
            }
            if (mCurrentItem > arrayList.size() - 1) {
                mCurrentItem = mCurrentItem - arrayList.size();
            }
            // continue;
        }
        do {
            int j2 = (int) ((float) totalScrollY % (l * (float) h));
            int k1 = 0;
            while (k1 < r) {
                int l1 = mCurrentItem - (4 - k1);
                if (isLoop) {
                    i1 = l1;
                    if (l1 < 0) {
                        i1 = l1 + arrayList.size();
                    }
                    l1 = i1;
                    if (i1 > arrayList.size() - 1) {
                        l1 = i1 - arrayList.size();
                    }
                    as[k1] = (String) arrayList.get(l1);
                } else if (l1 < 0) {
                    as[k1] = "";
                } else if (l1 > arrayList.size() - 1) {
                    as[k1] = "";
                } else {
                    as[k1] = (String) arrayList.get(l1);
                }
                k1++;
            }
            k1 = (v - g) / 2;
//             canvas.drawLine(0.0F, n, v, n, paintC);
//             canvas.drawLine(0.0F, o, v, o, paintC);
            int j1 = 0;
            while (j1 < r) {
                canvas.save();
                double d1 = ((double) ((float) (h * j1) * l - (float) j2) * 3.1415926535897931D) / (double) t;
                float f1 = (float) (90D - (d1 / 3.1415926535897931D) * 180D);
                if (f1 >= 90F || f1 <= -90F) {
                    canvas.restore();
                } else {
                    int i2 = (int) ((double) u - Math.cos(d1) * (double) u - (Math.sin(d1) * (double) h) / 2D);
                    canvas.translate(0f, i2);
                    canvas.scale(1.0F, (float) Math.sin(d1));

                    String str = as[j1];

                    // 字体大小自适应
                    String text = as[j1];
                    int textLength = text.isEmpty() ? 1 : text.length();
                    int textSize = Math.min(this.textSize, ((View)getParent()).getWidth()/(textLength + 1));
//                    paintA.setTextSize(zoomTextSize);
                    paintB.setTextSize(textSize + 10);

                    int startX = (int) (n + (getLeft() * 0.5));

                    Rect rect = new Rect();
                    paintB.getTextBounds(str, 0, str.length(), rect);

                    int itemWidth = rect.width();
                    int maxWidth = getWidth();

                    maxWidth -= 2 * startX;

                    if (itemWidth > 0) {
                        startX += (maxWidth - itemWidth) * 0.3;
                    }

                    if (i2 <= n && h + i2 >= n) {
                        canvas.save();
                        canvas.clipRect(0, 0, v, n - i2);
                        canvas.drawText(as[j1], startX, h, paintA);
                        canvas.restore();
                        canvas.save();
                        canvas.clipRect(0, n - i2, v, (int) ((float) h * l));
                        canvas.drawText(as[j1], startX, h, paintB);
                        canvas.restore();
                    } else if (i2 <= o && h + i2 >= o) {
                        canvas.save();
                        canvas.clipRect(0, 0, v, o - i2);
                        canvas.drawText(as[j1], startX, h, paintB);
                        canvas.restore();
                        canvas.save();
                        canvas.clipRect(0, o - i2, v, (int) ((float) h * l));
                        canvas.drawText(as[j1], startX, h, paintA);
                        canvas.restore();
                    } else if (i2 >= n && h + i2 <= o) {
                        canvas.clipRect(0, 0, v, (int) ((float) h * l));
                        canvas.drawText(as[j1], startX, h, paintB);
                        mSelectItem = arrayList.indexOf(as[j1]);
                    } else {
                        canvas.clipRect(0, 0, v, (int) ((float) h * l));
                        canvas.drawText(as[j1], startX, h, paintA);
                    }
                    canvas.restore();
                }
                j1++;
            }
            super.onDraw(canvas);
            return;
        } while (true);
    }

    protected void onMeasure(int i1, int j1) {
        d();
        setMeasuredDimension(v, s);
    }

    public boolean onTouchEvent(MotionEvent motionevent) {
        switch (motionevent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = motionevent.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                y = motionevent.getRawY();
                z = x - y;
                x = y;
                totalScrollY = (int) ((float) totalScrollY + z);
                if (!isLoop) {
                    if (totalScrollY > (int) ((float) (-positon) * (l * (float) h))) {
                        break; /* Loop/switch isn't completed */
                    }
                    totalScrollY = (int) ((float) (-positon) * (l * (float) h));
                }
                break;
            case MotionEvent.ACTION_UP:
            default:
                if (!gestureDetector.onTouchEvent(motionevent) && motionevent.getAction() == 1) {
                    f();
                }
                return true;
        }

        if (totalScrollY < (int) ((float) (arrayList.size() - 1 - positon) * (l * (float) h))) {
            invalidate();
        } else {
            totalScrollY = (int) ((float) (arrayList.size() - 1 - positon) * (l * (float) h));
            invalidate();
        }

        if (!gestureDetector.onTouchEvent(motionevent) && motionevent.getAction() == 1) {
            f();
        }
        return true;
    }
}
