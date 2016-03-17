package com.groomer.customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by deepak on 16/11/15.
 */

public class CustomTextViewMarkNovaBlack extends TextView {

    public CustomTextViewMarkNovaBlack(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CustomTextViewMarkNovaBlack(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomTextViewMarkNovaBlack(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Mark Simonson - Proxima Nova Black.ttf");
        setTypeface(tf);
    }

}


