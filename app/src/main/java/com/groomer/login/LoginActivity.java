package com.groomer.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;
import com.groomer.GroomerApplication;
import com.groomer.R;
import com.groomer.activity.BaseActivity;
import com.groomer.activity.SplashActivity;
import com.groomer.forgetpassword.ForgetpasswordActivity;
import com.groomer.gps.GPSTracker;
import com.groomer.home.HomeActivity;
import com.groomer.model.UserDTO;
import com.groomer.signup.SignupActivity;
import com.groomer.utillity.Constants;
import com.groomer.utillity.GroomerPreference;
import com.groomer.utillity.Theme;
import com.groomer.utillity.Utils;
import com.groomer.volley.CustomJsonRequest;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends BaseActivity {

    private static final String TAG = "LoginActivity";
    private Activity mActivity;
    private LoginButton btnFbLogin;
    private CallbackManager callbackmanager;
    private static final String TWITTER_KEY = "0WzgEZ838raQlA7BPASXLgsub";
    private static final String TWITTER_SECRET = "szOdlqn9obH0MEMaGnz2dTMMQXIdcbSQvtDcT7YkOjyALQKuEF";
    private TwitterLoginButton btnTwitterLogin;
    private TwitterSession session;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        mActivity = this;

        //First time set Theme green in preferences
        Utils.putObjectIntoPref(mActivity, Theme.Green, Constants.CURRENT_THEME);

        init();
        GPSTracker gpsTracker = new GPSTracker(mActivity);
    }

    private void init() {


        setTouchNClick(R.id.btn_login);
        setClick(R.id.tv_forgotpassword);
        setClick(R.id.tv_signup);
        setClick(R.id.back_btn);
        setClick(R.id.txt_skip);

        ImageView img_facebook_login = (ImageView) findViewById(R.id.img_facebook_login);
        btnFbLogin = (LoginButton) findViewById(R.id.btnFb);
        img_facebook_login.setOnClickListener(imgFacebookClick);
        setFbClick();

        ImageView img_twitter_login = (ImageView) findViewById(R.id.img_twitter_login);
        btnTwitterLogin = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        img_twitter_login.setOnClickListener(imgTwitterClick);

        btnTwitterLogin.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {

                session = result.data;

                String username = session.getUserName();
                //Long userid = session.getUserId();
                doSocialLogin("twitter", username, session.getId() + "", username, "");
                //getEmailidFromTwitter();

            }

            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });

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
            case R.id.back_btn:
                openSkipScreen();
                break;
            case R.id.txt_skip:
                Intent home = new Intent(mActivity, HomeActivity.class);
                home.putExtra("fragmentNumber", 0);
                startActivity(home);
                finish();
                break;
        }
    }

    private void openSkipScreen() {
        Intent skipIntent = new Intent(mActivity, SplashActivity.class);
        startActivity(skipIntent);
        this.finish();
    }

    public void performLogin() {

        Utils.hideKeyboard(LoginActivity.this);
        if (Utils.isOnline(LoginActivity.this)) {
            if (validateForm()) {
                Map<String, String> params = new HashMap<>();
                params.put("action", Constants.LOGIN_METHOD);
                params.put("email", getEditTextText(R.id.et_emailid));
                params.put("password", getEditTextText(R.id.et_passowrd));
                params.put("device", "Android");
                params.put("device_id", GroomerPreference.getPushRegistrationId(mActivity));
                params.put("lat", GroomerPreference.getLatitude(mActivity) + "");
                params.put("lng", GroomerPreference.getLongitude(mActivity) + "");
                params.put("address", "");

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
                                        Utils.showDialog(mActivity, "Error", Utils.getWebServiceMessage(response));
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
            Utils.showDialog(this, getString(R.string.message_title),
                    getString(R.string.alert_please_enter_emailid));
            return false;
        } else if (!Utils.isValidEmail(getEditTextText(R.id.et_emailid))) {
            Utils.showDialog(this, getString(R.string.message_title),
                    getString(R.string.alert_please_enter_valid_email_id));
            return false;
        } else if (getEditTextText(R.id.et_passowrd).equals("")) {
            Utils.showDialog(this, getString(R.string.message_title), getString(R.string.alert_please_enter_password));
            return false;
        }
        return true;
    }


    public void doSocialLogin(final String socialType, String username, String socialId,
                              String name, String gender) {
        Utils.hideKeyboard(mActivity);

        if (Utils.isOnline(mActivity)) {


            Map<String, String> params = new HashMap<>();
            params.put("action", Constants.SOCIAL_LOGIN_METHOD);
            params.put("name", name);
            params.put("email", username);
            params.put("social_id", socialId);
            params.put("device", "android");
            params.put("social_type", socialType);
            params.put("lat", GroomerPreference.getLatitude(mActivity) + "");
            params.put("lng", GroomerPreference.getLongitude(mActivity) + "");
            params.put("address", username);
            params.put("device_id", GroomerPreference.
                    getPushRegistrationId(mActivity.getApplicationContext()));
            if (!gender.equalsIgnoreCase(""))
                params.put("gender", gender.equalsIgnoreCase("Male") ? "M" : "F");

            final ProgressDialog pdialog = Utils.createProgressDialog(this, null, false);
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, Constants.SERVICE_URL, params,
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
                                    GroomerPreference.putObjectIntoPref(LoginActivity.this,
                                            userDTO, Constants.USER_INFO);

                                    // Logout from facebook
                                    if (socialType.equalsIgnoreCase("facebook")) {
                                        LoginManager.getInstance().logOut();
                                    }

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


    private void setFbClick() {
        callbackmanager = CallbackManager.Factory.create();
        btnFbLogin.setReadPermissions("public_profile", "email", "user_birthday");


        btnFbLogin.registerCallback(callbackmanager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                System.out.println("Success");
                GraphRequest req = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject json,
                                                    GraphResponse response) {
                                if (response.getError() != null) {
                                    // handle error
                                    Log.i("info", "onCompleted Error.");
                                } else {
                                    System.out.println("Success");
                                    //String jsonresult = String.valueOf(json);
                                    try {

                                        // ((OnFacebookLoginListener)mActivity).
                                        //   successfullFbLogin("facebook", json.getString("email"),
                                        //         json.getString("id"), json.getString("name"));

                                        doSocialLogin("facebook", json.getString("email"),
                                                json.getString("id"), json.getString("name"),
                                                json.getString("gender"));


                                    } catch (Exception e) {
                                        e.printStackTrace();
//                                        Utils.sendEmail(getActivity(), "Error", e.getMessage());
                                    }
                                }
                            }
                        }

                );
                Bundle param = new Bundle();
                //, gender, birthday, first_name, last_name, link
                param.putString("fields", "id, name, email,gender, birthday, picture.type(large)");
                req.setParameters(param);
                req.executeAsync();
            }

            @Override
            public void onCancel() {
                Log.d("", "");
            }

            @Override
            public void onError(FacebookException e) {
                Log.d("", "");
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Intent i = new Intent(getContext(), HomeActivity.class);
        // startActivity(i);
        if (requestCode == 64206) {
            callbackmanager.onActivityResult(requestCode, resultCode, data);
        } else {
            //
            btnTwitterLogin.onActivityResult(requestCode, resultCode, data);
        }
    }


    View.OnClickListener imgFacebookClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            btnFbLogin.performClick();
            setFbClick();
        }
    };


    View.OnClickListener imgTwitterClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            btnTwitterLogin.performClick();
        }
    };


}
