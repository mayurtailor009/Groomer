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
import com.groomer.login.listener.OnTwitterLoginListener;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import org.json.JSONObject;

/**
 * Created by DeepakGupta on 4/14/16.
 */
public class TwitterLogin {

    private OnTwitterLoginListener twitterSelectInterface;
    private static final String TWITTER_KEY = "0WzgEZ838raQlA7BPASXLgsub";
    private static final String TWITTER_SECRET = "szOdlqn9obH0MEMaGnz2dTMMQXIdcbSQvtDcT7YkOjyALQKuEF";
    private Activity mActivity;
    private TwitterLoginButton btnTwitterLogin;
    private TwitterSession session;


    public TwitterLogin(Activity mActivity) {
        this.mActivity = mActivity;
        FacebookSdk.sdkInitialize(mActivity.getApplicationContext());
        initClick();
    }

    public void initClick() {
        ImageView img_twitter_login = (ImageView) mActivity.findViewById(R.id.img_twitter_login);
        btnTwitterLogin = (TwitterLoginButton) mActivity.findViewById(R.id.twitter_login_button);

        img_twitter_login.setOnClickListener(imgFacebookClick);

        setTwitterClick();
    }

    View.OnClickListener imgFacebookClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            btnTwitterLogin.performClick();
            setTwitterClick();
        }
    };

    private void setTwitterClick() {


        btnTwitterLogin.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {

                session = result.data;

                String username = session.getUserName();

                ((OnTwitterLoginListener) mActivity).
                        successfullTwitterLogin("twitter", username,
                                session.getId() + "", username);


            }

            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });

    }




}
