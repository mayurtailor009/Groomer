package com.groomer.vendordetails;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
import com.groomer.model.ReviewDTO;
import com.groomer.model.SaloonDetailsDTO;
import com.groomer.model.ServiceDTO;
import com.groomer.utillity.Constants;
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
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VendorDetailsActivity extends BaseActivity implements PriceServiceInterface {

    private DisplayImageOptions options;
    private ArrayList<String> imageList;
    private Activity mActivity;
    private ArrayList<ReviewDTO> reviewList;
    private ArrayList<ServiceDTO> serviceList;
    private SaloonDetailsDTO saloonDetailsDTO;
    private List<ServiceDTO> selectedList;
    private String totalPrice;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme();
        setContentView(R.layout.activity_vendor_details);

        mActivity = VendorDetailsActivity.this;

        imageList = new ArrayList<>();

        getVendorDetails();

        //setting click operations on views
        setClick(R.id.vendor_details_iv_back);
        setClick(R.id.btn_services_tab);
        setClick(R.id.btn_about_tab);
        setClick(R.id.btn_reviews_tab);
        setClick(R.id.btn_set_appointment);
        setClick(R.id.img_fav);
    }

    private void getVendorDetails() {
        HashMap<String, String> params = new HashMap<>();
        params.put("action", Constants.VENDOR_DETAILS);
        params.put("lat", "23.444444");
        params.put("lng", "76.555555");
        params.put("user_id", Utils.getUserId(mActivity));
        params.put("store_id", getIntent().getStringExtra("store_id"));
        params.put("lang", "eng");

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
                                imageList.add(saloonDetailsDTO.getImage());
                                setSaloonDetails(); //setting the saloon details
                                setUpViewPager();

                                //getting reviews list.
                                Type reviewType = new TypeToken<ArrayList<ReviewDTO>>() {
                                }.getType();
                                reviewList = new Gson().fromJson(
                                        response.getJSONArray("Review").toString(),
                                        reviewType
                                );

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
        setViewText(R.id.txt_vendor_name, saloonDetailsDTO.getStorename_eng());
        setViewText(R.id.txt_vendor_address, saloonDetailsDTO.getAddress());

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
        ViewPager mPager = (ViewPager) findViewById(R.id.vendor_details_viewpager);
        mPager.setAdapter(new ViewPagerAdapter(this, imageList));

        CirclePageIndicator indicator = (CirclePageIndicator) findViewById(
                R.id.vendor_details_viewpager_indicators);
        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;
        indicator.setRadius(3 * density);
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
                Bundle serviceBundle = new Bundle();
                serviceBundle.putSerializable("serviceList", serviceList);
                fragment.setArguments(serviceBundle);
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
                Bundle reviewBundle = new Bundle();
                reviewBundle.putSerializable("reviewList", reviewList);
                fragment.setArguments(reviewBundle);
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
                Bundle bundle = new Bundle();
                bundle.putSerializable("serviceDTO", (Serializable) selectedList);
                intent.putExtras(bundle);
                intent.putExtra("saloonName", saloonDetailsDTO.getStorename_eng());
                intent.putExtra("saloonAddress", saloonDetailsDTO.getAddress());
                intent.putExtra("totalPrice", totalPrice);
                intent.putExtra("store_id", getIntent().getStringExtra("store_id"));
                mActivity.startActivity(intent);
                break;

            case R.id.img_fav:
                if (saloonDetailsDTO.getFavourite().equalsIgnoreCase("1")) {
                    addRemoveFromFavourite("0", saloonDetailsDTO.getStore_id());
                } else {
                    addRemoveFromFavourite("1", saloonDetailsDTO.getStore_id());
                }

                break;
        }
    }


    private void addRemoveFromFavourite(String status, String storeID) {
        if (Utils.isOnline(mActivity)) {
            HashMap<String, String> params = new HashMap<>();
            params.put("action", Constants.ADD_REMOVE_FAVOURITE);
            params.put("user_id", Utils.getUserId(mActivity));
            params.put("store_id", storeID);
            params.put("status", status);
            params.put("lang", "eng");

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

                                    Toast.makeText(mActivity, Utils.getWebServiceMessage(response), Toast.LENGTH_SHORT).show();
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


    @Override
    public void getPriceSum(String sum) {
        totalPrice = "SAR " + sum;
        setViewText(R.id.services_total_amount, "SAR " + sum);

    }

    @Override
    public void getServiceCount(String serviceCount) {
        setViewText(R.id.service_count, serviceCount
                + (serviceCount.equals("1") ? " Service" : " Services"));
    }

    @Override
    public void getSelectedServiceList(List<ServiceDTO> serviceDTOList) {
        selectedList = serviceDTOList;
    }
}
