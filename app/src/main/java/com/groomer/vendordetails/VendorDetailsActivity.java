package com.groomer.vendordetails;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.groomer.R;
import com.groomer.activity.BaseActivity;
import com.groomer.vendordetails.adapter.ViewPagerAdapter;
import com.groomer.vendordetails.fragments.AboutFragment;
import com.groomer.vendordetails.fragments.ReviewFragment;
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
    private Activity mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme();
        setContentView(R.layout.activity_vendor_details);

        mActivity = VendorDetailsActivity.this;

        mPager = (ViewPager) findViewById(R.id.vendor_details_viewpager);
        mPager.setAdapter(new ViewPagerAdapter(this, null));

        CirclePageIndicator indicator = (CirclePageIndicator) findViewById(
                R.id.vendor_details_viewpager_indicators);
        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;
        indicator.setRadius(5 * density);

        displayFragment(0);

        //setting click operations on views
        setClick(R.id.vendor_details_iv_back);
        setClick(R.id.btn_services_tab);
        setClick(R.id.btn_about_tab);
        setClick(R.id.btn_reviews_tab);
        setClick(R.id.btn_set_appointment);
    }

    private void displayFragment(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                setButtonSelected(R.id.btn_services_tab, true);
                setButtonSelected(R.id.btn_about_tab, false);
                setButtonSelected(R.id.btn_reviews_tab, false);
                setTextColor(R.id.btn_services_tab, R.color.colorWhite);
                setTextColor(R.id.btn_about_tab, R.color.black);
                setTextColor(R.id.btn_reviews_tab, R.color.black);
                fragment = ServicesFragment.newInstance();
                break;
            case 1:
                setButtonSelected(R.id.btn_about_tab, true);
                setButtonSelected(R.id.btn_reviews_tab, false);
                setButtonSelected(R.id.btn_services_tab, false);
                setTextColor(R.id.btn_about_tab, R.color.colorWhite);
                setTextColor(R.id.btn_reviews_tab, R.color.black);
                setTextColor(R.id.btn_services_tab, R.color.black);
                fragment = AboutFragment.newInstance();
                break;
            case 2:
                setButtonSelected(R.id.btn_reviews_tab, true);
                setButtonSelected(R.id.btn_about_tab, false);
                setButtonSelected(R.id.btn_services_tab, false);
                setTextColor(R.id.btn_reviews_tab, R.color.colorWhite);
                setTextColor(R.id.btn_about_tab, R.color.black);
                setTextColor(R.id.btn_services_tab, R.color.black);
                fragment = ReviewFragment.newInstance();
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
            case R.id.btn_services_tab:
                displayFragment(0);
                break;
            case R.id.btn_about_tab:
                displayFragment(1);
                break;
            case R.id.btn_reviews_tab:
                displayFragment(2);
                break;
            case R.id.btn_set_appointment:
                Intent intent = new Intent(mActivity, ConfirmAppointmentActivity.class);
                mActivity.startActivity(intent);
                break;
        }
    }
}
