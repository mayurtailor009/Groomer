package com.groomer.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.facebook.FacebookSdk;
import com.google.gson.Gson;
import com.groomer.GroomerApplication;
import com.groomer.R;
import com.groomer.activity.BaseActivity;
import com.groomer.gps.GPSTracker;
import com.groomer.home.HomeActivity;
import com.groomer.forgetpassword.ForgetpasswordActivity;
import com.groomer.login.listener.OnFacebookLoginListener;
import com.groomer.login.listener.OnTwitterLoginListener;
import com.groomer.model.UserDTO;
import com.groomer.signup.SignupActivity;
import com.groomer.utillity.Constants;
import com.groomer.utillity.GroomerPreference;
import com.groomer.utillity.Utils;
import com.groomer.volley.CustomJsonRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends BaseActivity implements OnFacebookLoginListener, OnTwitterLoginListener {

    private static final String TAG = "LoginActivity";
    private Activity mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        mActivity = this;
        FacebookLogin fbLogin = new FacebookLogin(mActivity);
        init();


        GPSTracker gpsTracker = new GPSTracker(mActivity);
    }

    private void init() {

        setTouchNClick(R.id.btn_login);
        setClick(R.id.tv_forgotpassword);
        setClick(R.id.tv_signup);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                performLogin();
                break;
            case R.id.tv_forgotpassword:
                startActivity(new Intent(LoginActivity.this, ForgetpasswordActivity.class));
                break;
            case R.id.tv_signup:
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
                finish();
                break;
        }
    }

    public void performLogin() {

        Utils.hideKeyboard(LoginActivity.this);
        if (Utils.isOnline(LoginActivity.this)) {
            if (validateForm()) {
                Map<String, String> params = new HashMap<>();
                params.put("action", Constants.LOGIN_METHOD);
                params.put("email", getEditTextText(R.id.et_emailid));
                params.put("password", getEditTextText(R.id.et_passowrd));
                params.put("device", "android");
                params.put("device_id", "abc");
                params.put("lat", GroomerPreference.getLatitude(mActivity) + "");
                params.put("lng", GroomerPreference.getLongitude(mActivity) + "");
                params.put("address", "abc");

                final ProgressDialog pdialog = Utils.createProgressDialog(this, null, false);
                CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, Constants.SERVICE_URL, params,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Utils.ShowLog(Constants.TAG, "Response -> " + response.toString());
                                pdialog.dismiss();
                                try {
                                    if (Utils.getWebServiceStatus(response)) {

                                        UserDTO userDTO = new Gson().fromJson(response.getJSONObject("user").toString(), UserDTO.class);
                                        GroomerPreference.putObjectIntoPref(LoginActivity.this, userDTO, Constants.USER_INFO);

                                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                        intent.putExtra("fragmentNumber", 0);
                                        startActivity(intent);


                                        finish();
                                    } else {
                                        Utils.showDialog(LoginActivity.this, "Error", Utils.getWebServiceMessage(response));
                                    }

                                } catch (Exception e) {
                                    pdialog.dismiss();
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pdialog.dismiss();
                        Utils.showExceptionDialog(LoginActivity.this);
                    }
                });
                pdialog.show();
                GroomerApplication.getInstance().getRequestQueue().add(postReq);
            }
        } else {
            Utils.showNoNetworkDialog(LoginActivity.this);
        }


    }


    public boolean validateForm() {

        if (getEditTextText(R.id.et_emailid).equals("")) {
            Utils.showDialog(this, "Message", "Please enter emailid");
            return false;
        } else if (getEditTextText(R.id.et_passowrd).equals("")) {
            Utils.showDialog(this, "Message", "Please enter password");
            return false;
        }
        return true;
    }


    public void doSocialLogin(String socialType, String username, String socialId, String name) {
        Utils.hideKeyboard(mActivity);

        if (Utils.isOnline(mActivity)) {


            Map<String, String> params = new HashMap<>();
            params.put("action", Constants.SOCIAL_LOGIN_METHOD);
            params.put("name", name);
            params.put("email", username);
            params.put("social_id", socialId);
            params.put("device", "android");
            params.put("social_type", socialType);
            params.put("lat", name);
            params.put("lng", username);
            params.put("address", username);
            params.put("device_id", GroomerPreference.
                    getPushRegistrationId(mActivity.getApplicationContext()));

            final ProgressDialog pdialog = Utils.createProgressDialog(mActivity, null, false);
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST,
                    Constants.SERVICE_URL, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Utils.ShowLog(TAG, "Response -> " + response.toString());
                            pdialog.dismiss();
                            try {
                                if (Utils.getWebServiceStatus(response)) {
                                    UserDTO userDTO = new Gson().
                                            fromJson(response.getJSONObject("user").toString(),
                                                    UserDTO.class);
                                    GroomerPreference.putObjectIntoPref(LoginActivity.this, userDTO, Constants.USER_INFO);

                                    Intent intent = new Intent(mActivity, HomeActivity.class);
                                    intent.putExtra("fragmentNumber", 0);
                                    startActivity(intent);
                                } else {
                                    Utils.showDialog(mActivity, "Error",
                                            Utils.getWebServiceMessage(response));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    pdialog.dismiss();
                    Utils.showExceptionDialog(mActivity);
                }
            });
            pdialog.show();

            GroomerApplication.getInstance().getRequestQueue().add(postReq);
            postReq.setRetryPolicy(new DefaultRetryPolicy(
                    30000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        } else {
            Utils.showNoNetworkDialog(mActivity);
        }
    }


    @Override
    public void successfullFbLogin(String socialType, String username,
                                   String socialId, String name) {

        doSocialLogin(socialType, username, socialId, name);
    }

    @Override
    public void successfullTwitterLogin(String socialType, String username,
                                        String socialId, String name) {
        doSocialLogin(socialType, username, socialId, name);
    }
}
