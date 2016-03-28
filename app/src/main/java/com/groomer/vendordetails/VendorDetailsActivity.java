package com.groomer.vendordetails;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.groomer.R;
import com.groomer.activity.BaseActivity;
import com.groomer.vendordetails.adapter.ViewPagerAdapter;
import com.groomer.vendordetails.fragments.ServicesFragment;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

public class VendorDetailsActivity extends BaseActivity {

    private DisplayImageOptions options;
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private ArrayList<String> imageList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_details);

        mPager = (ViewPager) findViewById(R.id.vendor_details_viewpager);
        mPager.setAdapter(new ViewPagerAdapter(this, null));

        CirclePageIndicator indicator = (CirclePageIndicator) findViewById(
                R.id.vendor_details_viewpager_indicators);
        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;
        indicator.setRadius(5 * density);

        setClick(R.id.vendor_details_iv_back);
        displayFragment(0);
    }

    private void displayFragment(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = ServicesFragment.newInstance();
                break;
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.vendor_details_container, fragment)
                .commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.vendor_details_iv_back:
                this.finish();
                break;

        }
    }
}
