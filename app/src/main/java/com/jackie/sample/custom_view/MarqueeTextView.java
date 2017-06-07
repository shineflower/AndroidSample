package com.jackie.sample.custom_view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class MarqueeTextView extends TextView {
  
    public MarqueeTextView(Context context) {
        this(context, null);  
    }  
  
    public MarqueeTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);  
    }  
  
    public MarqueeTextView(Context context, AttributeSet attrs, int defStyle) {  
        super(context, attrs, defStyle);  
    }  
      
    @Override  
    public boolean isFocused() {  
//      return super.isFocused();  
        return true;  
    }
}  