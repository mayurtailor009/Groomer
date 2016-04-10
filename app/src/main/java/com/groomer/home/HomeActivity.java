package com.groomer.home;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.groomer.R;
import com.groomer.activity.BaseActivity;
import com.groomer.alert.AlertFragment;
import com.groomer.appointment.AppointmentFragment;
import com.groomer.category.SaloonListFragment;
import com.groomer.customviews.alert.CustomAlert;
import com.groomer.favourite.FavouriteFragment;
import com.groomer.menucount.MenuCountHandler;
import com.groomer.model.MenuDTO;
import com.groomer.model.UserDTO;
import com.groomer.settings.SettingFragment;
import com.groomer.utillity.Constants;
import com.groomer.utillity.GroomerPreference;
import com.groomer.utillity.SessionManager;
import com.groomer.utillity.Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.lang.ref.WeakReference;


public class HomeActivity extends BaseActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private Toolbar toolbar;
    private Activity mActivity;
    private boolean backPressedToExitOnce = false;
    private final MenuHandler menuHandler =
            new MenuHandler(HomeActivity.this);
    private MenuDTO menuDTO;
    private NavigationView navigationView;
    private DisplayImageOptions options;


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
        drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {


            }

            @Override
            public void onDrawerOpened(View drawerView) {
                new Thread(new MenuCountHandler(menuHandler,
                        HomeActivity.this)).start();
            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });


        options = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new SimpleBitmapDisplayer())
                .showImageOnLoading(R.drawable.avater)
                .showImageOnFail(R.drawable.avater)
                .showImageForEmptyUri(R.drawable.avater)
                .build();
        //drawerToggle.syncState();

    }

    private void setUpNavigationViewItemClick() {
        navigationView = (NavigationView) findViewById(R.id.navigationview);
        setHeadersValues();

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

    public static class MenuHandler extends android.os.Handler {

        private static final String TAG = "MenuHandler";
        public final WeakReference<HomeActivity> mActivity;

        MenuHandler(HomeActivity activity) {
            mActivity = new WeakReference<HomeActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            Utils.ShowLog(TAG, "handleMessage in MenuHandler");
            HomeActivity activity = mActivity.get();
            activity.menuDTO = ((MenuDTO) msg.obj);
            activity.changeMenuCount(((MenuDTO) msg.obj));


        }
    }

    public void changeMenuCount(MenuDTO menuDto) {


        setMenuCounter(R.id.nav_alerts, Integer.parseInt(menuDto.getAlert()));
        setMenuCounter(R.id.nav_appointments, Integer.parseInt(menuDto.getAppointment()));
        setMenuCounter(R.id.nav_favourite, Integer.parseInt(menuDto.getFavorite()));
        //TODO
        // Set menu count
//        menuListAdapter.setAlertCount(menuDTO.getAlert());
//        menuListAdapter.notifyDataSetChanged();

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

    private void setHeadersValues() {

        UserDTO userDTO = GroomerPreference.getObjectFromPref(this, Constants.USER_INFO);
        View headerLayout = navigationView.getHeaderView(0);
        TextView txt_name = (TextView) headerLayout.findViewById(R.id.nav_header_user_name);
        txt_name.setText(userDTO.getName_eng());

        StringBuffer stringBuffer = new StringBuffer();
        if (userDTO.getGender() != null && !userDTO.getGender().equalsIgnoreCase("")) {
            stringBuffer.append(userDTO.getGender());
        }
        if (userDTO.getAge() != null && !userDTO.getAge().equalsIgnoreCase("")) {
            stringBuffer.append(" " + userDTO.getAge());
        }
        if (userDTO.getIs_location_service() != null
                && !userDTO.getIs_location_service().equalsIgnoreCase("")) {
            if (!stringBuffer.equals("")) {
                stringBuffer.append(" | ");
            }
            stringBuffer.append(userDTO.getIs_location_service());
        }

        ImageView imageView = (ImageView) headerLayout.findViewById(R.id.img_user_image);
        ImageLoader.getInstance().displayImage(userDTO.getImage(), imageView,
                options);


        TextView txt_age_gender = (TextView) headerLayout.findViewById(R.id.nav_header_age);
        txt_age_gender.setText(stringBuffer.toString());


    }


    private void setMenuCounter(@IdRes int itemId, int count) {
        TextView view = (TextView) navigationView.getMenu().findItem(itemId).getActionView();
        view.setText(String.valueOf(count));
    }

}
