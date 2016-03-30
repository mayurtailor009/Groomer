package com.groomer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.groomer.R;
import com.groomer.home.HomeActivity;
import com.groomer.login.LoginActivity;
import com.groomer.model.UserDTO;
import com.groomer.signup.SignupActivity;
import com.groomer.utillity.Constants;
import com.groomer.utillity.Utils;

public class SplashActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        UserDTO userDTO  = Utils.getObjectFromPref(this, Constants.USER_INFO);
        if(userDTO!=null){
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }
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
                finish();
                break;
            case R.id.tv_signup:
                startActivity(new Intent(this, SignupActivity.class));
                finish();
                break;
        }
    }
}
