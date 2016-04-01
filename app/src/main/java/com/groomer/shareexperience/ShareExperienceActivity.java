package com.groomer.shareexperience;

import android.os.Bundle;
import android.view.View;

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

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.hamburgur_img_icon:
                onBackPressed();
                break;
        }
    }


    @Override
    public void onBackPressed() {
        this.finish();
    }
}
