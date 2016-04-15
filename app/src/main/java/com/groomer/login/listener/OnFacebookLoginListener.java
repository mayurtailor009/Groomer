package com.groomer.login.listener;

/**
 * Created by DeepakGupta on 4/14/16.
 */
public interface OnFacebookLoginListener {

    public void successfullFbLogin(String socialType, String username,
                                   String socialId, String name);
}
