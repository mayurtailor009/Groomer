package com.groomer.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.groomer.R;
import com.groomer.home.HomeActivity;
import com.groomer.login.LoginActivity;
import com.groomer.model.UserDTO;
import com.groomer.signup.SignupActivity;
import com.groomer.utillity.Constants;
import com.groomer.utillity.GroomerPreference;
import com.groomer.utillity.HelpMe;
import com.groomer.utillity.Utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
        showHashKey(mContext);
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


    public void showHashKey(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo("groomer.com.groomer",
                    PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());

                String sign = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.e("KeyHash:", sign);
                Toast.makeText(context, sign, Toast.LENGTH_LONG).show();
            }
            Log.d("KeyHash:", "****------------***");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
