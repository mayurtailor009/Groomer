package com.groomer.activity;

import android.os.Bundle;
import android.view.View;

import com.groomer.R;

public class ForgetpasswordActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);

        init();
    }

    private void init() {
        setTouchNClick(R.id.btn_forgetpassword);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_forgetpassword:

                break;
        }
    }

}
