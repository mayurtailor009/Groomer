package com.groomer.customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by deepak on 16/11/15.
 */

public class CustomTextViewMarkNovaExtrabold extends TextView {

    public CustomTextViewMarkNovaExtrabold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CustomTextViewMarkNovaExtrabold(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomTextViewMarkNovaExtrabold(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Mark Simonson - Proxima Nova Extrabold.ttf");
        setTypeface(tf);
    }

}


