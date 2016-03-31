package com.groomer.shareexperience;

import android.os.Bundle;

import com.groomer.R;
import com.groomer.activity.BaseActivity;

public class ShareExperienceActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_experience);

        setHeader(getString(R.string.share_title));
        setLeftClick(R.drawable.back_btn);
    }
}
