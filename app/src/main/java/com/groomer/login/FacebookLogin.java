package com.groomer.login;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.groomer.R;
import com.groomer.login.listener.OnFacebookLoginListener;

import org.json.JSONObject;

/**
 * Created by DeepakGupta on 4/14/16.
 */
public class FacebookLogin {

    private OnFacebookLoginListener fbSelectInterface;
    private Activity mActivity;
    private LoginButton btnFbLogin;
    private CallbackManager callbackmanager;

    public FacebookLogin(Activity mActivity) {
        this.mActivity = mActivity;
      //FacebookSdk.sdkInitialize(mActivity.getApplicationContext());
        initClick();
    }

    public void initClick() {
        ImageView img_facebook_login = (ImageView) mActivity.findViewById(R.id.img_facebook_login);
        btnFbLogin = (LoginButton) mActivity.findViewById(R.id.btnFb);

        img_facebook_login.setOnClickListener(imgFacebookClick);

      //  setFbClick();
    }

    View.OnClickListener imgFacebookClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            btnFbLogin.performClick();
            setFbClick();
        }
    };

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

                                        ((OnFacebookLoginListener)mActivity).
                                                successfullFbLogin("facebook", json.getString("email"),
                                               json.getString("id"), json.getString("name"));
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
                param.putString("fields", "id, name, email");
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




}
