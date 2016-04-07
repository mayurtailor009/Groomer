package com.groomer.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.groomer.R;
import com.groomer.activity.BaseActivity;
import com.groomer.alert.AlertFragment;
import com.groomer.appointment.AppointmentFragment;
import com.groomer.category.SaloonListFragment;
import com.groomer.customviews.alert.CustomAlert;
import com.groomer.favourite.FavouriteFragment;
import com.groomer.model.UserDTO;
import com.groomer.settings.SettingFragment;
import com.groomer.utillity.Constants;
import com.groomer.utillity.GroomerPreference;
import com.groomer.utillity.SessionManager;


public class HomeActivity extends BaseActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private Toolbar toolbar;
    private Activity mActivity;
    private boolean backPressedToExitOnce = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme();
        setContentView(R.layout.activity_home);
        mActivity = HomeActivity.this;

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        init();

        setUpNavigationViewItemClick();

        findViewById(R.id.hamburgur_img_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        int fragmentNumber = getIntent().getIntExtra("fragmentNumber", 0);

        displayFragment(fragmentNumber);
    }

    private void init() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open,
                R.string.drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        //drawerToggle.syncState();

    }

    private void setUpNavigationViewItemClick() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigationview);
        setHeadersValues(navigationView);

        navigationView.setNavigationItemSelectedListener(new NavigationView
                .OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                drawerLayout.closeDrawer(GravityCompat.START);
                switch (item.getItemId()) {
                    case R.id.nav_services:
                        displayFragment(0);
                        break;
                    case R.id.nav_alerts:
                        displayFragment(1);
                        break;
                    case R.id.nav_appointments:
                        displayFragment(2);
                        break;
                    case R.id.nav_favourite:
                        displayFragment(3);
                        break;
                    case R.id.nav_settings:
                        displayFragment(4);
                        break;
                    case R.id.nav_logout:
                        logoutFromApp();
                        break;
                }
                return true;
            }
        });
    }

    /**
     * this method takes out of the application.
     */
    private void logoutFromApp() {
        new CustomAlert(mActivity)
                .doubleButtonAlertDialog(
                        getString(R.string.you_logout),
                        getString(R.string.ok_button),
                        getString(R.string.canceled), "dblBtnCallbackResponse", 1000);
    }

    /**
     * callback method of double button alert box.
     *
     * @param flag true if Ok button pressed otherwise false.
     * @param code is requestCode.
     */
    public void dblBtnCallbackResponse(Boolean flag, int code) {
        if (flag) {
            SessionManager.logoutUser(mActivity);
        }

    }

    @Override
    public void onBackPressed() {
        if (backPressedToExitOnce) {
            super.onBackPressed();
            SessionManager.logoutUser(mActivity);
        } else {
            this.backPressedToExitOnce = true;
            Toast.makeText(mActivity, "Press again to exit", Toast.LENGTH_SHORT).show();
            new android.os.Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    backPressedToExitOnce = false;
                }
            }, 2000);
        }
    }

    private void displayFragment(int index) {
        Fragment fragment = null;
        String title = "";
        switch (index) {
            case 0:
                fragment = SaloonListFragment.newInstance();
                title = getString(R.string.txt_category);
                break;
            case 1:
                fragment = AlertFragment.newInstance();
                title = getString(R.string.menu_alerts);
                break;
            case 2:
                fragment = AppointmentFragment.newInstance();
                title = getString(R.string.menu_appointments);
                break;
            case 3:
                fragment = FavouriteFragment.newInstance();
                title = getString(R.string.menu_favorite);
                break;
            case 4:
                fragment = SettingFragment.newInstance();
                title = getString(R.string.menu_settings);
                break;
        }

        setHeader(title);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.body_layout, fragment)
                .commit();
    }




    /*private void setHeader(String title) {
        TextView textView = (TextView) toolbar.findViewById(R.id.toolbar_title);
        textView.setText(title);
    }*/

    private void setHeadersValues(NavigationView navigationView) {

        UserDTO userDTO = GroomerPreference.getObjectFromPref(this, Constants.USER_INFO);
        View headerLayout = navigationView.getHeaderView(0);
        TextView txt_name = (TextView) headerLayout.findViewById(R.id.nav_header_user_name);
        txt_name.setText(userDTO.getName_eng());


        TextView txt_age_gender = (TextView) headerLayout.findViewById(R.id.nav_header_age);
        txt_age_gender.setText(userDTO.getGender());


    }


}
