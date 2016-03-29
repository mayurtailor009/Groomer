package com.groomer.home;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.groomer.R;
import com.groomer.alert.AlertFragment;
import com.groomer.category.SaloonListFragment;
import com.groomer.favourite.FavouriteFragment;
import com.groomer.settings.SettingFragment;


public class HomeActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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

        displayFragment(0);
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
                    case R.id.nav_favourite:
                        displayFragment(3);
                        break;
                    case R.id.nav_settings:
                        displayFragment(4);
                        break;
                }
                return true;
            }
        });
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

    private void setHeader(String title) {
        TextView textView = (TextView) toolbar.findViewById(R.id.toolbar_title);
        textView.setText(title);
    }




}
