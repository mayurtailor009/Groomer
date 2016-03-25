package com.groomer.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.groomer.R;
import com.groomer.activity.BaseActivity;
import com.groomer.alert.AlertFragment;
import com.groomer.category.SaloonListFragment;
import com.groomer.settings.SettingFragment;

public class HomeActivity extends BaseActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ResideMenuSecond resideMenu;
    private ResideMenuItem itemServices, itemAlerts, itemAppointments,
            itemFavorite, itemSetting, itemLogout;

    private ResideMenuSecond.OnMenuListener menuListener = new ResideMenuSecond.OnMenuListener() {
        @Override
        public void openMenu() {
        }

        @Override
        public void closeMenu() {
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setViewVisibility(R.id.hamburgur_img_icon, View.VISIBLE);
        setClick(R.id.hamburgur_img_icon);
        setUpMenu();


    }

    private void setUpMenu() {
        resideMenu = new ResideMenuSecond(this);
        resideMenu.setBackgroundColor(getResources().getColor(R.color.dark_green));
        resideMenu.attachToActivity(this);
        resideMenu.setMenuListener(menuListener);

        itemServices = new ResideMenuItem(this, R.drawable.place_icon, getString(R.string.menu_services), "");
        itemAppointments = new ResideMenuItem(this, R.drawable.thumb_icon, getString(R.string.menu_appointments), "");
        itemAlerts = new ResideMenuItem(this, R.drawable.notification_btn, getString(R.string.menu_alerts), "");
        itemLogout = new ResideMenuItem(this, R.drawable.logout_btn, getString(R.string.menu_logout), "");
        itemFavorite = new ResideMenuItem(this, R.drawable.fav_btn, getString(R.string.menu_favorite), "");
        itemSetting = new ResideMenuItem(this, R.drawable.setting_btn, getString(R.string.menu_settings), "");

        itemServices.setOnClickListener(this);
        itemAppointments.setOnClickListener(this);
        itemAlerts.setOnClickListener(this);
        itemLogout.setOnClickListener(this);
        itemFavorite.setOnClickListener(this);
        itemSetting.setOnClickListener(this);


        resideMenu.addMenuItem(itemServices, ResideMenuSecond.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemAlerts, ResideMenuSecond.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemAppointments, ResideMenuSecond.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemFavorite, ResideMenuSecond.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemSetting, ResideMenuSecond.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemLogout, ResideMenuSecond.DIRECTION_LEFT);


        changeFragment(SaloonListFragment.newInstance());

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }


    @Override
    public void onClick(View view) {

        if (view == itemServices) {
            resideMenu.closeMenu();
            changeFragment(SaloonListFragment.newInstance());
            // setHeader(getString(R.string.menu_services));
        } else if (view == itemSetting) {
            resideMenu.closeMenu();
            changeFragment(SettingFragment.newInstance());
            //   setHeader(getString(R.string.menu_settings));
        } else if (view == itemAlerts) {
            resideMenu.closeMenu();
            changeFragment(AlertFragment.newInstance());
            //   setHeader(getString(R.string.menu_settings));
        }


        switch (view.getId()) {
            case R.id.hamburgur_img_icon:
                if (resideMenu.isOpened()) {
                    resideMenu.closeMenu();
                    resideMenu.setOpened(false);
                } else {
                    resideMenu.openMenu(ResideMenuSecond.DIRECTION_LEFT);
                    resideMenu.setOpened(true);
                }
                break;
        }


    }


    private void changeFragment(Fragment targetFragment) {


        resideMenu.clearIgnoredViewList();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.body_layout, targetFragment, "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

}
