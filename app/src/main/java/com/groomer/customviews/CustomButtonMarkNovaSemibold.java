package com.groomer.customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by deepak on 16/11/15.
 */

public class CustomButtonMarkNovaSemibold extends Button {

    public CustomButtonMarkNovaSemibold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CustomButtonMarkNovaSemibold(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomButtonMarkNovaSemibold(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Mark Simonson - Proxima Nova Semibold.ttf");
        setTypeface(tf);
    }

}


