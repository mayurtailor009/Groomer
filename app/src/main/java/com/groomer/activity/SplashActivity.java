package com.groomer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.groomer.R;

public class SplashActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        init();
    }

    private void init(){
        setClick(R.id.tv_signin);
        setClick(R.id.tv_signup);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_signin:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.tv_signup:

                break;
        }
    }
}
