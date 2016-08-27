package com.groomer.vendordetails;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.groomer.GroomerApplication;
import com.groomer.R;
import com.groomer.activity.BaseActivity;
import com.groomer.gps.GPSTracker;
import com.groomer.model.ReviewDTO;
import com.groomer.model.SaloonDetailsDTO;
import com.groomer.model.ServiceDTO;
import com.groomer.utillity.Constants;
import com.groomer.utillity.GroomerPreference;
import com.groomer.utillity.HelpMe;
import com.groomer.utillity.SessionManager;
import com.groomer.utillity.Theme;
import com.groomer.utillity.Utils;
import com.groomer.vendordetails.adapter.ViewPagerAdapter;
import com.groomer.vendordetails.fragments.AboutFragment;
import com.groomer.vendordetails.fragments.ReviewFragment;
import com.groomer.vendordetails.fragments.ServicesFragment;
import com.groomer.vendordetails.priceserviceinterface.PriceServiceInterface;
import com.groomer.volley.CustomJsonRequest;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONObject;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class VendorDetailsActivity extends BaseActivity implements PriceServiceInterface {

    private DisplayImageOptions options;
    private Activity mActivity;
    private ArrayList<ReviewDTO> reviewList;
    private List<ServiceDTO> serviceList;
    private SaloonDetailsDTO saloonDetailsDTO;
    private List<ServiceDTO> selectedList;
    private String totalPrice;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private ViewPager mPager;
    private ArrayList<String> listImages;
    private HashMap<String, Fragment> fragmentList = new HashMap<String, Fragment>();

    private final ReviewResponseHandler myHandler =
            new ReviewResponseHandler(VendorDetailsActivity.this);

    private boolean isReviewSelected;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme();
        setContentView(R.layout.activity_vendor_details);

        mActivity = VendorDetailsActivity.this;

        GPSTracker gpstracker = new GPSTracker(mActivity);
        final String storeId = getIntent().getStringExtra("store_id");

        getVendorDetails(storeId);


        //setting click operations on views
        setClick(R.id.vendor_details_iv_back);
        setClick(R.id.btn_services_tab);
        setClick(R.id.btn_about_tab);
        setClick(R.id.btn_reviews_tab);
        setClick(R.id.btn_set_appointment);
        setClick(R.id.directions);

        if (!Utils.IsSkipLogin(mActivity)) {
            setViewVisibility(R.id.img_fav, View.VISIBLE);
            setClick(R.id.img_fav);
        } else {
            setViewVisibility(R.id.img_fav, View.GONE);
        }

        buttonSelected(true, false, false);

        setClick(R.id.vendor_details_zoom);
        setTextColor(R.id.btn_services_tab, R.color.colorWhite);
        setTextColor(R.id.btn_about_tab, R.color.black);
        setTextColor(R.id.btn_reviews_tab, R.color.black);

        Thread t = new Thread(
                new Runnable() {

                    @Override
                    public void run() {

                        // Request for reviews
                        requestForReviews(storeId);
                    }
                });
        t.start();
    }

    private void getVendorDetails(final String storeId) {
        HashMap<String, String> params = new HashMap<>();
        params.put("action", Constants.VENDOR_DETAILS);
        params.put("lat", "" + GroomerPreference.getLatitude(mActivity));
        params.put("lng", "" + GroomerPreference.getLongitude(mActivity));
        params.put("user_id", Utils.getUserId(mActivity));
        params.put("store_id", storeId);
        params.put("lang", Utils.getSelectedLanguage(mActivity));

        final ProgressDialog pdialog = Utils.createProgressDialog(mActivity, null, false);
        CustomJsonRequest jsonRequest = new CustomJsonRequest(Request.Method.POST,
                Constants.SERVICE_URL, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("Groomer info", response.toString());
                        pdialog.dismiss();
                        try {
                            if (Utils.getWebServiceStatus(response)) {

                                //getting saloon details
                                saloonDetailsDTO = new Gson().fromJson(
                                        response.getJSONObject("Saloon").toString(),
                                        SaloonDetailsDTO.class
                                );

                                //adding images urls to imagelist to show in viewpager.
                                setSaloonDetails(); //setting the saloon details
                                setUpViewPager();

                                //getting service list.
                                Type servieType = new TypeToken<ArrayList<ServiceDTO>>() {
                                }.getType();
                                serviceList = new Gson().fromJson(
                                        response.getJSONArray("Service").toString(), servieType
                                );

                                showServiesFragment();

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("Groomer info", error.toString());
                        pdialog.dismiss();
                        Utils.showExceptionDialog(mActivity);
                    }
                }
        );

        pdialog.show();
        GroomerApplication.getInstance().addToRequestQueue(jsonRequest);
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(30000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        );
    }

    /**
     * this method simply calls the displayFrament method and shows the services fragment.
     */
    private void showServiesFragment() {
        //displaying services fragment after getting list of service.

        displayFragment(0);
    }


    /**
     * this method sets the saloon name and address details.
     */
    private void setSaloonDetails() {

        if (HelpMe.isArabic(mActivity)) {
            setViewText(R.id.txt_vendor_name, saloonDetailsDTO.getStorename_ara());
        } else {
            setViewText(R.id.txt_vendor_name, saloonDetailsDTO.getStorename_eng());
        }
        setViewText(R.id.txt_vendor_address, saloonDetailsDTO.getAddress());
        setViewText(R.id.txt_vendor_distance, "(" + saloonDetailsDTO.getDistance() +
                " " + getString(R.string.distance_unit_km) + " )");
        setViewText(R.id.txt_vendor_rating, saloonDetailsDTO.getRating());


        if (saloonDetailsDTO.getRating() != null && !saloonDetailsDTO.getRating().equalsIgnoreCase("")) {
            String strReview = getViewText(R.id.btn_reviews_tab);
            String reviewCount = saloonDetailsDTO.getRating().equalsIgnoreCase("0")
                    ? "" : "(" + saloonDetailsDTO.getRating() + ")";
            setViewText(R.id.btn_reviews_tab, strReview + reviewCount);
        }

        ImageView img_fav = (ImageView) findViewById(R.id.img_fav);
        if (saloonDetailsDTO.getFavourite().equals("1")) {
            img_fav.setImageResource(R.drawable.fav_active_icon);

        } else {

            img_fav.setImageResource(R.drawable.fav_icon);
        }
    }

    /**
     * this method initializes the viewpager and set the images in it.
     */
    private void setUpViewPager() {
        listImages = new ArrayList<>();
//        listImages.add(saloonDetailsDTO.getImage());

        for (int i = 0; i < saloonDetailsDTO.getImages().size(); i++) {
            listImages.add(saloonDetailsDTO.getImages().get(i).getImage());
        }

        mPager = (ViewPager) findViewById(R.id.vendor_details_viewpager);
        mPager.setAdapter(new ViewPagerAdapter(mActivity, listImages));

        CirclePageIndicator indicator = (CirclePageIndicator) findViewById(
                R.id.vendor_details_viewpager_indicators);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                indicator.getLayoutParams();
        if (GroomerPreference.getAPP_LANG(mActivity).equals("eng")) {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        } else {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        }
        indicator.setLayoutParams(layoutParams);
        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;
        indicator.setRadius(3 * density);

        NUM_PAGES = listImages.size();

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage, true);
                currentPage++;
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });


    }

    private void displayFragment(int position) {
        Fragment fragment = null;
        String tag = null;
        switch (position) {
            case 0:
                isReviewSelected = false;

                tag = "service";
                buttonSelected(true, false, false);
                setTextColor(R.id.btn_services_tab, R.color.colorWhite);
                setTextColor(R.id.btn_about_tab, R.color.black);
                setTextColor(R.id.btn_reviews_tab, R.color.black);
                fragment = fragmentList.get(tag);
                if (fragment == null) {
                    fragment = ServicesFragment.newInstance();
                    fragmentList.put(tag, fragment);
                }
                break;

            case 1:
                isReviewSelected = false;

                tag = "about";
                buttonSelected(false, true, false);
                setTextColor(R.id.btn_about_tab, R.color.colorWhite);
                setTextColor(R.id.btn_reviews_tab, R.color.black);
                setTextColor(R.id.btn_services_tab, R.color.black);
                if (HelpMe.isArabic(mActivity)) {
                    fragment = AboutFragment.newInstance(saloonDetailsDTO.getStoredesc_ara());
                } else {
                    fragment = AboutFragment.newInstance(saloonDetailsDTO.getStoredesc_eng());
                }
                break;

            case 2:
                tag = "review";
                isReviewSelected = true;
                fragment = ReviewFragment.newInstance(reviewList);
                break;


        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.vendor_details_container, fragment, tag)
                .commit();
        getSupportFragmentManager().executePendingTransactions();
    }


    public static class ReviewResponseHandler extends Handler {

        public final WeakReference<VendorDetailsActivity> mActivity;

        ReviewResponseHandler(VendorDetailsActivity activity) {
            mActivity = new WeakReference<VendorDetailsActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            VendorDetailsActivity activity = mActivity.get();
            if (msg.what == Constants.REVIEW_LIST_HANDLER) {
                activity.reviewList = ((ArrayList<ReviewDTO>) msg.obj);
                if (activity.isReviewSelected) {
                    activity.displayFragment(2);
                }
            }


        }
    }


    private void requestForReviews(String storeId) {
        HashMap<String, String> params = new HashMap<>();
        params.put("action", Constants.REVIEWLIST);
        params.put("store_id", storeId);
        params.put("lang", Utils.getSelectedLanguage(mActivity));

        //final ProgressDialog pdialog = Utils.createProgressDialog(mActivity, null, false);
        CustomJsonRequest jsonRequest = new CustomJsonRequest(Request.Method.POST,
                Constants.SERVICE_URL, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("Groomer info", response.toString());
                        //pdialog.dismiss();
                        if (Utils.getWebServiceStatus(response)) {
                            try {
                                //getting reviews list.
                                Type reviewType = new TypeToken<ArrayList<ReviewDTO>>() {
                                }.getType();
                                reviewList = new Gson().fromJson(
                                        response.getJSONArray("review").toString(),
                                        reviewType
                                );
                                Message msg = myHandler.obtainMessage(Constants.
                                        REVIEW_LIST_HANDLER, reviewList);
                                myHandler.sendMessage(msg);

//                                ReviewFragment fragment = ReviewFragment.newInstance(reviewList);
//
//
//                                getSupportFragmentManager()
//                                        .beginTransaction()
//                                        .replace(R.id.vendor_details_container, fragment)
//                                        .commit();

                            } catch (Exception e) {
                                e.printStackTrace();
                                Utils.showExceptionDialog(mActivity);
                            }
                        }
//                        else {
//                            ReviewFragment fragment = ReviewFragment.newInstance();
//                            Bundle reviewBundle = new Bundle();
//                            reviewBundle.putSerializable("reviewList", reviewList);
//                            fragment.setArguments(reviewBundle);
//
//                            getSupportFragmentManager()
//                                    .beginTransaction()
//                                    .replace(R.id.vendor_details_container, fragment)
//                                    .commit();
//                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("Groomer info", error.toString());
                        //pdialog.dismiss();
                        Utils.showExceptionDialog(mActivity);
                    }
                }
        );

        //pdialog.show();
        GroomerApplication.getInstance().addToRequestQueue(jsonRequest);
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(30000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.vendor_details_iv_back:
                mActivity.finish();
                break;
            case R.id.btn_services_tab:
                displayFragment(0);
                break;
            case R.id.btn_about_tab:

                displayFragment(1);
                break;
            case R.id.btn_reviews_tab:
                //if (reviewList != null && reviewList.size() != 0) {

                displayFragment(2);
                //}
//                else {
//
//                    requestForReviews();
//                }
                buttonSelected(false, false, true);
                setTextColor(R.id.btn_reviews_tab, R.color.colorWhite);
                setTextColor(R.id.btn_about_tab, R.color.black);
                setTextColor(R.id.btn_services_tab, R.color.black);


                break;
            case R.id.btn_set_appointment:
                if (Utils.IsSkipLogin(mActivity)) {
                    Utils.showDialog(mActivity,
                            getString(R.string.message_title),
                            getString(R.string.for_access_this_please_login),
                            getString(R.string.txt_login),
                            getString(R.string.canceled), login);
                } else {
                    if (selectedList != null && selectedList.size() > 0) {
                        Intent intent = new Intent(mActivity, ConfirmAppointmentActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("serviceDTO", (Serializable) selectedList);
                        intent.putExtras(bundle);
                        if (!HelpMe.isArabic(mActivity)) {
                            intent.putExtra("saloonName", saloonDetailsDTO.getStorename_eng());
                        } else {
                            intent.putExtra("saloonName", saloonDetailsDTO.getStorename_ara());
                        }
                        intent.putExtra("saloonAddress", saloonDetailsDTO.getAddress());
                        intent.putExtra("totalPrice", totalPrice);
                        intent.putExtra("store_id", getIntent().getStringExtra("store_id"));
                        mActivity.startActivity(intent);
                    } else {
                        Toast.makeText(mActivity, "Select atleast one service.", Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case R.id.img_fav:
                if (Utils.IsSkipLogin(mActivity)) {
                    Utils.showDialog(mActivity,
                            getString(R.string.message_title),
                            getString(R.string.for_access_this_please_login),
                            getString(R.string.txt_login),
                            getString(R.string.canceled), login);
                } else {
                    if (saloonDetailsDTO.getFavourite().equalsIgnoreCase("1")) {
                        addRemoveFromFavourite("0", saloonDetailsDTO.getStore_id());
                    } else {
                        addRemoveFromFavourite("1", saloonDetailsDTO.getStore_id());
                    }
                }

                break;

            case R.id.directions:

                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(
                        "geo:" + saloonDetailsDTO.getLat() +
                                "," + saloonDetailsDTO.getLng() +
                                "?q=" + saloonDetailsDTO.getLat() +
                                "," + saloonDetailsDTO.getLng() +
                                "(" + saloonDetailsDTO.getStorename_eng() + ")"));

                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                startActivity(intent);
                break;

            case R.id.vendor_details_zoom:
                Intent i = new Intent(this, FullScreenImageActivity.class);
                i.putExtra("images", listImages);
                startActivity(i);

                break;
        }
    }

    private void buttonSelected(boolean servicesSelected,
                                boolean aboutSelected,
                                boolean reviewSelected) {


        Theme theme = Utils.getObjectFromPref(mActivity, Constants.CURRENT_THEME);
        if (reviewSelected) {

            if (theme.equals(Theme.Blue)) {
                findViewById(R.id.btn_reviews_tab).
                        setBackgroundColor(getResources().getColor(R.color.theme_blue));

            } else if (theme.equals(Theme.Red)) {
                findViewById(R.id.btn_reviews_tab).
                        setBackgroundColor(getResources().getColor(R.color.theme_red));
            } else {
                findViewById(R.id.btn_reviews_tab).
                        setBackgroundColor(getResources().getColor(R.color.theme_green));
            }
        } else {
            findViewById(R.id.btn_reviews_tab).
                    setBackgroundColor(getResources().getColor(R.color.grey));

        }
        if (aboutSelected) {

            if (theme.equals(Theme.Blue)) {
                findViewById(R.id.btn_about_tab).
                        setBackgroundColor(getResources().getColor(R.color.theme_blue));

            } else if (theme.equals(Theme.Red)) {
                findViewById(R.id.btn_about_tab).
                        setBackgroundColor(getResources().getColor(R.color.theme_red));
            } else {
                findViewById(R.id.btn_about_tab).
                        setBackgroundColor(getResources().getColor(R.color.theme_green));
            }

        } else {
            findViewById(R.id.btn_about_tab).
                    setBackgroundColor(getResources().getColor(R.color.grey));
        }
        if (servicesSelected) {
            if (theme.equals(Theme.Blue)) {
                findViewById(R.id.btn_services_tab).
                        setBackgroundColor(getResources().getColor(R.color.theme_blue));

            } else if (theme.equals(Theme.Red)) {
                findViewById(R.id.btn_services_tab).
                        setBackgroundColor(getResources().getColor(R.color.theme_red));
            } else {
                findViewById(R.id.btn_services_tab).
                        setBackgroundColor(getResources().getColor(R.color.theme_green));
            }

        } else {
            findViewById(R.id.btn_services_tab).
                    setBackgroundColor(getResources().getColor(R.color.grey));

        }
    }


    private void addRemoveFromFavourite(final String status, String storeID) {
        if (Utils.isOnline(mActivity)) {
            HashMap<String, String> params = new HashMap<>();
            params.put("action", Constants.ADD_REMOVE_FAVOURITE);
            params.put("user_id", Utils.getUserId(mActivity));
            params.put("store_id", storeID);
            params.put("status", status);
            params.put("lat", GroomerPreference.getLatitude(mActivity) + "");
            params.put("lng", GroomerPreference.getLongitude(mActivity) + "");
            params.put("lang", Utils.getSelectedLanguage(mActivity));

            final ProgressDialog pdialog = Utils.createProgressDialog(mActivity, null, false);
            CustomJsonRequest jsonRequest = new CustomJsonRequest(Request.Method.POST,
                    Constants.SERVICE_URL, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.i("Groomer info", response.toString());
                            pdialog.dismiss();
                            if (Utils.getWebServiceStatus(response)) {
                                try {
                                    ImageView img_fav = (ImageView) findViewById(R.id.img_fav);
                                    if (status.equalsIgnoreCase("1")) {
                                        img_fav.setImageResource(R.drawable.fav_active_icon);
                                        saloonDetailsDTO.setFavourite("1");
                                    } else {
                                        img_fav.setImageResource(R.drawable.fav_icon);
                                        saloonDetailsDTO.setFavourite("0");
                                    }
//                                    Toast.makeText(mActivity,
//                                            Utils.getWebServiceMessage(response),
//                                            Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {

                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("Groomer info", error.toString());
                            pdialog.dismiss();
                            Utils.showExceptionDialog(mActivity);
                        }
                    }
            );

            pdialog.show();
            GroomerApplication.getInstance().addToRequestQueue(jsonRequest);
            jsonRequest.setRetryPolicy(new DefaultRetryPolicy(30000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        }


    }

    public List<ServiceDTO> getServiceList() {
        return serviceList;
    }

    public void setServiceList(List<ServiceDTO> serviceList) {
        this.serviceList = serviceList;
    }

    @Override
    public void getPriceSum(String sum) {
        totalPrice = getString(R.string.txt_vendor_price_unit) + " " + sum;
        setViewText(R.id.services_total_amount, getString(R.string.txt_vendor_price_unit) + " " + sum);

    }

    @Override
    public void getServiceCount(String serviceCount) {
        if (!GroomerPreference.getAPP_LANG(mActivity).equals("ara")) {
            setViewText(R.id.service_count, serviceCount
                    + (serviceCount.equals("1") ? " " + getString(R.string.txt_service) :
                    " " + getString(R.string.txt_services)));
        } else {
            setViewText(R.id.service_count, " خدمات " + serviceCount);
        }
    }

    @Override
    public void getSelectedServiceList(List<ServiceDTO> serviceDTOList) {
        double buyDouble = 0.0;
        serviceList = serviceDTOList;
        selectedList = new ArrayList<>();
        for (ServiceDTO dto : serviceDTOList) {
            if (dto.isSelected()) {
                selectedList.add(dto);
                //buyDouble =buyDouble+ dto.getPrice()
            }
        }

        setViewText(R.id.service_count, selectedList.size() +
                (selectedList.size() == 1 ? " " + getString(R.string.txt_service) :
                        " " + getString(R.string.txt_services)));
    }

    DialogInterface.OnClickListener login = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            SessionManager.logoutUser(mActivity);
            ;
        }
    };

}
