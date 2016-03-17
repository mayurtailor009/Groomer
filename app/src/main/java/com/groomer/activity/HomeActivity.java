package com.groomer.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.groomer.R;
import com.groomer.category.SaloonListFragment;

public class HomeActivity extends BaseActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    SaloonListFragment saloonFragment;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();
        initDrawer();

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        saloonFragment = new SaloonListFragment();
        ft.add(R.id.body_layout, saloonFragment, "");
        //ft.addToBackStack(null);
        ft.commit();

    }

    private void init(){

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        setToolbarTitle("Select Category");
    }
    private void initDrawer() {

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

            }
        };

        drawerToggle.setDrawerIndicatorEnabled(false); //disable "hamburger to arrow" drawable
        drawerToggle.setHomeAsUpIndicator(R.drawable.menu_btn); //set your own
        drawerToggle.syncState();
        drawerLayout.setDrawerListener(drawerToggle);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               toggle();
            }
        });
    }

    private void toggle() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_view:
                // Red item was selected
                if(saloonFragment!=null){
                    saloonFragment.swapeView();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
