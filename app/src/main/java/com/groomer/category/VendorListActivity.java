package com.groomer.category;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
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
import com.groomer.category.adapter.VendorListAdapter;
import com.groomer.gps.GPSTracker;
import com.groomer.model.CategoryDTO;
import com.groomer.model.VendorListDTO;
import com.groomer.utillity.Constants;
import com.groomer.utillity.FetchPopUpSelectValue;
import com.groomer.utillity.GroomerPreference;
import com.groomer.utillity.HelpMe;
import com.groomer.utillity.PopUpFragment;
import com.groomer.utillity.SessionManager;
import com.groomer.utillity.Utils;
import com.groomer.vendordetails.VendorDetailsActivity;
import com.groomer.volley.CustomJsonRequest;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VendorListActivity extends BaseActivity implements FetchPopUpSelectValue {


    private Activity mActivity;
    private List<VendorListDTO> vendorList;
    private RecyclerView vendorRecyclerView;
    private VendorListAdapter vendorListAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private CategoryDTO categoryDTO;
    private SeekBar distanceSeekBar;
    private Button btnRatingAsc,
            btnRatingDesc, btnReviewAsc, btnReviewDesc;
    private LinearLayout llFilter;
    private ArrayList<CategoryDTO> categoryList;

    private String distance = "5";
    private String review = "ASC";
    private String rating = "ASC";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme();
        setContentView(R.layout.activity_vendor_list);

        mActivity = VendorListActivity.this;
        GPSTracker gpstracker = new GPSTracker(mActivity);

        categoryDTO = (CategoryDTO) getIntent().getExtras().getSerializable("dto");
        categoryList = (ArrayList<CategoryDTO>) getIntent().getSerializableExtra("dtoList");
        init(categoryDTO);


        getVendorsList(categoryDTO, distance, rating, review);

        if (HelpMe.isArabic(mActivity)) {
            setTextViewText(R.id.txt_category, categoryDTO.getName_ara());
        } else {
            setTextViewText(R.id.txt_category, categoryDTO.getName_eng());
        }
        // Add pull to refresh functionality
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.active_swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                getVendorsList(categoryDTO, distance, rating, review);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        distanceSeekBar = (SeekBar) findViewById(R.id.seekbar_km);
        distanceSeekBar.setProgress(5);
        distanceSeekBar.setOnSeekBarChangeListener(seekBarChangeListener);
    }

    private void init(CategoryDTO categoryDTO) {
/*
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
*/
        if (HelpMe.isArabic(this)) {
            setHeader(categoryDTO.getName_ara());
        } else {
            setHeader(categoryDTO.getName_eng());
        }


        setLeftClick(R.drawable.back_btn, true);

        vendorRecyclerView = (RecyclerView) findViewById(R.id.recycle_vendor);

        LinearLayoutManager llm = new LinearLayoutManager(mActivity);
        vendorRecyclerView.setLayoutManager(llm);
        llFilter = (LinearLayout) findViewById(R.id.ll_filter);
        btnRatingAsc = (Button) findViewById(R.id.btn_rating_asc);
        btnRatingDesc = (Button) findViewById(R.id.btn_rating_desc);
        btnReviewAsc = (Button) findViewById(R.id.btn_review_asc);
        btnReviewDesc = (Button) findViewById(R.id.btn_review_desc);
        btnRatingAsc.setSelected(true);
        btnReviewAsc.setSelected(true);

        setTouchNClick(R.id.btn_review_desc);
        setTouchNClick(R.id.btn_review_asc);
        setTouchNClick(R.id.btn_rating_desc);
        setTouchNClick(R.id.btn_rating_asc);
        setTouchNClick(R.id.btn_apply);
        setTouchNClick(R.id.btn_cancel);
        setTouchNClick(R.id.txt_category);

        llFilter.setVisibility(View.GONE);
    }

    private void setUpListAdapter(final List<VendorListDTO> vendorList) {

        if (vendorListAdapter != null) {
            vendorListAdapter = null;
        }

        setViewVisibility(R.id.no_saloon, View.GONE);
        setViewVisibility(R.id.recycle_vendor, View.VISIBLE);

        vendorListAdapter = new VendorListAdapter(mActivity, vendorList);
        vendorRecyclerView.setAdapter(vendorListAdapter);

//        vendorRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(mActivity,
//                        vendorRecyclerView, new MyOnClickListener() {
//                    @Override
//                    public void onItemClick(View view, int position) {
//                        Intent intent = new Intent(mActivity, VendorDetailsActivity.class);
//                        intent.putExtra("store_id", vendorList.get(position).getStore_id());
//                        mActivity.startActivity(intent);
//                    }
//                })
//        );

//        vendorRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                //setViewVisibility(R.id.km_seekbar_layout, View.VISIBLE);
//                showErrorTextAnimation(R.id.km_seekbar_layout);
//
//
//            }
//        });

        vendorListAdapter.setOnItemClickListener(new VendorListAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                switch (v.getId()) {
                    case R.id.thumbnail:
                        Intent intent = new Intent(mActivity, VendorDetailsActivity.class);
                        intent.putExtra("store_id", vendorList.get(position).getStore_id());
                        mActivity.startActivity(intent);
                        break;

                    case R.id.img_fav:
                        if (Utils.IsSkipLogin(mActivity)) {
                            Utils.showDialog(mActivity,
                                    getString(R.string.message_title),
                                    getString(R.string.for_access_this_please_login),
                                    getString(R.string.txt_login),
                                    getString(R.string.canceled), login);
                        } else {
                            if (vendorList.get(position).getFavourite().equalsIgnoreCase("1")) {

                                addRemoveFromFavourite("0", vendorList.get(position).getStore_id(), position);

                            } else {
                                addRemoveFromFavourite("1", vendorList.get(position).getStore_id(), position);

                            }
                        }
                        break;


                }
            }
        });

    }

    DialogInterface.OnClickListener login = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            SessionManager.logoutUser(mActivity);
            ;
        }
    };


//    public void showErrorTextAnimation(final int id) {
//        final View mView = findViewById(id);
//        mView.setVisibility(View.VISIBLE);
//
//
//        // fade out view nicely after 5 seconds
//        AlphaAnimation alphaAnim = new AlphaAnimation(1.0f, 0.0f);
//        alphaAnim.setStartOffset(5000);
//        alphaAnim.setDuration(400);
//        alphaAnim.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            public void onAnimationEnd(Animation animation) {
//                mView.setVisibility(View.INVISIBLE);
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//
//        mView.setAnimation(alphaAnim);
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vendor_list, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_search:
                if (vendorList == null || vendorList.isEmpty()) {
                    item.setEnabled(false);
                } else {
                    item.setEnabled(true);
                    if (llFilter.getVisibility() == View.GONE)
                        llFilter.setVisibility(View.VISIBLE);
                    else
                        llFilter.setVisibility(View.GONE);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.hamburgur_img_icon:
                finish();
                break;

            case R.id.btn_rating_asc:
                btnRatingDesc.setSelected(false);
                btnRatingAsc.setSelected(true);
                rating = "ASC";
                break;

            case R.id.btn_rating_desc:
                btnRatingDesc.setSelected(true);
                btnRatingAsc.setSelected(false);
                rating = "DESC";
                break;

            case R.id.btn_review_asc:
                btnReviewDesc.setSelected(false);
                btnReviewAsc.setSelected(true);
                review = "ASC";
                break;

            case R.id.btn_review_desc:
                btnReviewDesc.setSelected(true);
                btnReviewAsc.setSelected(false);
                review = "DESC";
                break;

            case R.id.btn_apply:
                llFilter.setVisibility(View.GONE);
                getVendorsList(categoryDTO, distance, rating, review);
                break;

            case R.id.btn_cancel:
                llFilter.setVisibility(View.GONE);
                break;
            case R.id.txt_category:

                PopUpFragment dialogFragment = new PopUpFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("popUpList", categoryList);
                bundle.putString("title", "Select Category");
                dialogFragment.setArguments(bundle);
                dialogFragment.setFetchSelectedInterface(mActivity);
                dialogFragment.show(getFragmentManager(), "");

                break;

        }
    }


    private void getVendorsList(CategoryDTO categoryDTO, String distances, String rating, String review) {
        if (Utils.isOnline(mActivity)) {
            HashMap<String, String> params = new HashMap<>();
            params.put("action", Constants.VENDOR_LIST);
            params.put("lat", GroomerPreference.getLatitude(mActivity) + "");
            params.put("lng", GroomerPreference.getLongitude(mActivity) + "");
            params.put("user_id", Utils.getUserId(mActivity));
            params.put("category_id", categoryDTO.getId());
            params.put("rating", rating);
            params.put("review", review);
            params.put("lang", Utils.getSelectedLanguage(mActivity));
            params.put("distance", distances);

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
                                    Type type = new TypeToken<ArrayList<VendorListDTO>>() {
                                    }.getType();
                                    vendorList = new Gson()
                                            .fromJson(response.getJSONArray("saloon").toString(), type);
                                    setUpListAdapter(vendorList);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                checkForEmptyList();
                                setViewVisibility(R.id.no_saloon, View.VISIBLE);
                                setViewVisibility(R.id.active_swipe_refresh_layout, View.GONE);
                                setTextViewText(R.id.no_saloon, Utils.getWebServiceMessage(response));
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
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

    private void checkForEmptyList() {
        if (vendorList == null || vendorList.isEmpty()) {
            llFilter.setVisibility(View.GONE);
        }
    }


    private void addRemoveFromFavourite(final String status, String storeID, final int pos) {
        if (Utils.isOnline(mActivity)) {
            HashMap<String, String> params = new HashMap<>();
            params.put("action", Constants.ADD_REMOVE_FAVOURITE);
            params.put("user_id", Utils.getUserId(mActivity));
            params.put("store_id", storeID);
            params.put("status", status);
            params.put("lang", GroomerPreference.getAPP_LANG(mActivity));

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
                                    if (status.equalsIgnoreCase("1")) {
                                        vendorList.get(pos).setFavourite("1");
//                                        img_fav.setImageResource(R.drawable.fav_active_icon);
//                                        saloonDetailsDTO.setFavourite("1");
                                    } else {
                                        vendorList.get(pos).setFavourite("0");
//                                        img_fav.setImageResource(R.drawable.fav_icon);
//                                        saloonDetailsDTO.setFavourite("0");
                                    }
                                    vendorListAdapter.setVendorsList(vendorList);
                                    vendorListAdapter.notifyDataSetChanged();
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

    SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            setTextViewText(R.id.txt_km, progress + " Km:");

            distance = progress + "";

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };


    @Override
    public void selectedValue(int text, String tag) {


        categoryDTO = categoryList.get(text);
        if (HelpMe.isArabic(mActivity))
            setTextViewText(R.id.txt_category, categoryDTO.getName_ara());
        else
            setTextViewText(R.id.txt_category, categoryDTO.getName_eng());

    }
}