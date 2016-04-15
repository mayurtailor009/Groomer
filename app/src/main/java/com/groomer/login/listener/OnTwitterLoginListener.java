package com.groomer.login.listener;

/**
 * Created by DeepakGupta on 4/14/16.
 */
public interface OnTwitterLoginListener {

    public void successfullTwitterLogin(String socialType, String username,
                                   String socialId, String name);
}
