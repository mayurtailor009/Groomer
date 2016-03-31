package com.groomer.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.groomer.R;

public class ThemeTestActivity extends AppCompatActivity {

    static  int theme=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme();
        setContentView(R.layout.activity_theme_test);

        ((Button)findViewById(R.id.btn_blue)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                theme = 1;
                startActivity(new Intent(ThemeTestActivity.this, ThemeTestActivity.class));
            }
        });

        ((Button)findViewById(R.id.btn_white)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                theme = 2;
                startActivity(new Intent(ThemeTestActivity.this, ThemeTestActivity.class));
            }
        });

        ((Button)findViewById(R.id.btn_gray)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                theme = 3;
                startActivity(new Intent(ThemeTestActivity.this, ThemeTestActivity.class));
            }
        });

    }

    public void setTheme(){
        switch (theme){
            case 1:
                setTheme(R.style.AppThemeBlue);
                if (Build.VERSION.SDK_INT >= 21) {
                    getWindow().setStatusBarColor(getResources().getColor(R.color.colorBlueDark));
                }
                break;
            case 2:
                setTheme(R.style.AppThemeGreen);
                if (Build.VERSION.SDK_INT >= 21) {
                    getWindow().setStatusBarColor(getResources().getColor(R.color.green));
                }
                break;
            case 3:
                setTheme(R.style.AppThemeRed);
                if (Build.VERSION.SDK_INT >= 21) {
                    getWindow().setStatusBarColor(getResources().getColor(R.color.red));
                }
                break;
        }
    }


}
