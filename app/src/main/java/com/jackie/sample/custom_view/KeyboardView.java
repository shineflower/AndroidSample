package com.jackie.sample.custom_view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.jackie.sample.R;

/**
 * Created by Jackie on 2019/3/14.
 * 数字输入键盘
 */
public class KeyboardView extends FrameLayout implements View.OnClickListener {
    private CodeView codeView;
    private Listener listener;

    public KeyboardView(@NonNull Context context) {
        this(context, null);
    }

    public KeyboardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KeyboardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_pwd_keyboard, null);
        view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(view);

        view.findViewById(R.id.keyboard_0).setOnClickListener(this);
        view.findViewById(R.id.keyboard_1).setOnClickListener(this);
        view.findViewById(R.id.keyboard_2).setOnClickListener(this);
        view.findViewById(R.id.keyboard_3).setOnClickListener(this);
        view.findViewById(R.id.keyboard_4).setOnClickListener(this);
        view.findViewById(R.id.keyboard_5).setOnClickListener(this);
        view.findViewById(R.id.keyboard_6).setOnClickListener(this);
        view.findViewById(R.id.keyboard_7).setOnClickListener(this);
        view.findViewById(R.id.keyboard_8).setOnClickListener(this);
        view.findViewById(R.id.keyboard_9).setOnClickListener(this);
        view.findViewById(R.id.keyboard_hide).setOnClickListener(this);
        view.findViewById(R.id.keyboard_delete).setOnClickListener(this);
    }

    public void hide() {
        setVisibility(View.GONE);
    }

    public void show() {
        setVisibility(View.VISIBLE);
    }

    public void setCodeView(CodeView codeView) {
        this.codeView = codeView;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        String tag = (String) v.getTag();

        if (tag != null) {
            switch (tag) {
                case "hide":
                    hide();
                    break;
                case "delete":
                    if (codeView != null) {
                        codeView.delete();
                    }

                    if (listener != null) {
                        listener.onDelete();
                    }
                    break;
                default:
                    if (codeView != null) {
                        codeView.input(tag);
                    }

                    if (listener != null) {
                        listener.onInput(tag);
                    }
                    break;
            }
        }
    }

    public interface Listener {
        void onInput(String s);
        void onDelete();
    }
}
