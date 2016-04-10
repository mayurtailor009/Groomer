package com.groomer.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.groomer.R;
import com.groomer.home.HomeActivity;
import com.groomer.login.LoginActivity;
import com.groomer.model.UserDTO;
import com.groomer.signup.SignupActivity;
import com.groomer.utillity.Constants;
import com.groomer.utillity.GroomerPreference;
import com.groomer.utillity.HelpMe;
import com.groomer.utillity.Utils;

public class SplashActivity extends BaseActivity {

    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mContext = SplashActivity.this;

        UserDTO userDTO = GroomerPreference.getObjectFromPref(mContext, Constants.USER_INFO);
        HelpMe.setLocale(GroomerPreference.getAPP_LANG(mContext), getApplicationContext());
        if (userDTO != null) {
            startActivity(new Intent(mContext, HomeActivity.class));
            finish();
        }
        init();
    }

    private void init() {
        setClick(R.id.tv_signin);
        setClick(R.id.tv_signup);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_signin:
                startActivity(new Intent(mContext, LoginActivity.class));
                finish();
                break;
            case R.id.tv_signup:
                startActivity(new Intent(mContext, SignupActivity.class));
                finish();
                break;
        }
    }
}
