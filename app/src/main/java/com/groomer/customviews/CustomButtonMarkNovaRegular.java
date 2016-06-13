package com.groomer.customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

import com.groomer.utillity.HelpMe;

/**
 * Created by deepak on 16/11/15.
 */

public class CustomButtonMarkNovaRegular extends Button {

    public CustomButtonMarkNovaRegular(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public CustomButtonMarkNovaRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomButtonMarkNovaRegular(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        Typeface tf = null;
        //if (!HelpMe.isArabic(context)) {
            tf = Typeface.createFromAsset(getContext().getAssets(),
                    "fonts/Mark Simonson - Proxima Nova Regular.ttf");
//        } else {
//            tf = Typeface.createFromAsset(getContext().getAssets(),
//                    "fonts/majalla.ttf");
//        }
        setTypeface(tf);
    }

}


