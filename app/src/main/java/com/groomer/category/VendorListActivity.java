package com.groomer.category;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
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
import com.groomer.utillity.EndlessRecyclerOnScrollListener;
import com.groomer.utillity.FetchPopUpSelectValue;
import com.groomer.utillity.GroomerPreference;
import com.groomer.utillity.HelpMe;
import com.groomer.utillity.PopUpFragment;
import com.groomer.utillity.SessionManager;
import com.groomer.utillity.Utils;
import com.groomer.vendordetails.VendorDetailsActivity;
import com.groomer.volley.CustomJsonRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
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
    //private SeekBar distanceSeekBar;
    private Button btnRatingAsc;
    private Button btnRatingDesc;
    private Button btnPriceAsc;
    private Button btnPriceDesc;
    private Button btnDistanceAsc;
    private Button btnDistanceDesc;
    private LinearLayout llFilter;
    private ArrayList<CategoryDTO> categoryList;
    private LinearLayoutManager llm;

    private String distance = "";
    private String price = "";
    private String rating = "";
    private String searchKeyword = "";
    private int totalCount = 0;
    private boolean flag = false;


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

        vendorList = new ArrayList<>();
        getVendorsList(categoryDTO.getId(), distance, rating, price, searchKeyword, "1");

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
                flag = false;
                vendorList.clear();
                getVendorsList(categoryDTO.getId(), distance, rating, price, searchKeyword, "1");
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
//        distanceSeekBar = (SeekBar) findViewById(R.id.seekbar_km);
//        distanceSeekBar.setProgress(5);
//        distanceSeekBar.setOnSeekBarChangeListener(seekBarChangeListener);
    }

    private void init(CategoryDTO categoryDTO) {

        if (HelpMe.isArabic(this)) {
            setHeader(categoryDTO.getName_ara());
            setLeftClick(R.drawable.back_btn_right, true);

        } else {
            setHeader(categoryDTO.getName_eng());
            setLeftClick(R.drawable.back_btn, true);

        }


        vendorRecyclerView = (RecyclerView) findViewById(R.id.recycle_vendor);

        llm = new LinearLayoutManager(mActivity);
        vendorRecyclerView.setLayoutManager(llm);
        llFilter = (LinearLayout) findViewById(R.id.ll_filter);
        btnRatingAsc = (Button) findViewById(R.id.btn_rating_asc);
        btnRatingDesc = (Button) findViewById(R.id.btn_rating_desc);
        btnPriceAsc = (Button) findViewById(R.id.btn_price_asc);
        btnPriceDesc = (Button) findViewById(R.id.btn_price_desc);
        btnDistanceAsc = (Button) findViewById(R.id.btn_distance_asc);
        btnDistanceDesc = (Button) findViewById(R.id.btn_distance_desc);

        setTouchNClick(R.id.btn_price_asc);
        setTouchNClick(R.id.btn_price_desc);
        setTouchNClick(R.id.btn_rating_desc);
        setTouchNClick(R.id.btn_rating_asc);
        setTouchNClick(R.id.btn_distance_asc);
        setTouchNClick(R.id.btn_distance_desc);
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
        vendorRecyclerView.setHasFixedSize(true);
        vendorRecyclerView.setAdapter(vendorListAdapter);


        vendorRecyclerView.setOnScrollListener(new EndlessRecyclerOnScrollListener(
                llm) {
            @Override
            public void onLoadMore(int current_page) {
                int lastPos = llm.findLastCompletelyVisibleItemPosition();
                int itemCount = llm.getItemCount();
                if (lastPos != -1
                        && itemCount == (lastPos + 1)) {
                    if (totalCount > vendorList.size())
                        getVendorsList(categoryDTO.getId(), distance, rating,
                                price, searchKeyword, current_page + "");
                }
            }

        });


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
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vendor_list, menu);
        SearchManager searchManager = (SearchManager) mActivity.getSystemService(SEARCH_SERVICE);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(this.getComponentName()));

        //adding a button in search view
        searchView.setSubmitButtonEnabled(false);
        LinearLayout mLayout = (LinearLayout) searchView.getChildAt(0);
        mLayout.setGravity(Gravity.CENTER_VERTICAL);
        ImageView mImageview = new ImageView(mActivity);
        mImageview.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.search_btn));
        mLayout.addView(mImageview);
        mImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                vendorList.clear();
                flag = false;
                getVendorsList(categoryDTO.getId(), distance, rating, price, searchView.getQuery() + "", "1");
                //Toast.makeText(mActivity, searchView.getQuery(), Toast.LENGTH_SHORT).show();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                vendorListAdapter.getFilteredList(newText);
                return true;
            }
        });
        searchView.setQueryHint(Html.fromHtml("<font color = #d7e6f0>"
                + "Search..." + "</font>"));
        changeSearchViewTextColor(searchView);
        return true;
    }

    private void changeSearchViewTextColor(View view) {
        if (view != null) {
            if (view instanceof TextView) {
                ((TextView) view).setTextColor(Color.BLACK);
                return;
            } else if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    changeSearchViewTextColor(viewGroup.getChildAt(i));
                }
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_filter:
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

            case R.id.btn_price_asc:
                btnPriceDesc.setSelected(false);
                btnPriceAsc.setSelected(true);
                price = "ASC";
                break;

            case R.id.btn_price_desc:
                btnPriceDesc.setSelected(true);
                btnPriceAsc.setSelected(false);
                price = "DESC";
                break;

            case R.id.btn_distance_asc:
                btnDistanceDesc.setSelected(false);
                btnDistanceAsc.setSelected(true);
                distance = "ASC";
                break;

            case R.id.btn_distance_desc:
                btnDistanceDesc.setSelected(true);
                btnDistanceAsc.setSelected(false);
                distance = "DESC";
                break;

            case R.id.btn_apply:
                llFilter.setVisibility(View.GONE);
                if (HelpMe.isArabic(this)) {
                    setHeader(categoryDTO.getName_ara());
                } else {
                    setHeader(categoryDTO.getName_eng());
                }

                flag = false;
                vendorList.clear();
                getVendorsList(categoryDTO.getId(), distance, rating, price, searchKeyword, "1");
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


    private void getVendorsList(String categoryId,
                                String distance,
                                String rating, String price, String searchKeyword, String pageNumber) {
        try {
            if (Utils.isOnline(mActivity)) {
                HashMap<String, String> params = new HashMap<>();
                params.put("action", Constants.VENDOR_LIST);
                params.put("lat", GroomerPreference.getLatitude(mActivity) + "");
                params.put("lng", GroomerPreference.getLongitude(mActivity) + "");
                params.put("user_id", Utils.getUserId(mActivity));
                params.put("category_id", categoryId);
                params.put("rating", rating);
                params.put("lang", Utils.getSelectedLanguage(mActivity));
                params.put("distance", distance);
                params.put("price", price);
                params.put("page", pageNumber);
                params.put("keyword", searchKeyword);

                final ProgressDialog pdialog = Utils.createProgressDialog(mActivity, null, false);
                CustomJsonRequest jsonRequest = new CustomJsonRequest(Request.Method.POST,
                        Constants.SERVICE_URL, params,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                Log.i("Groomer info", response.toString());
                                pdialog.dismiss();
                                showSaloonListResponse(response);

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
            } else {


                Cache.Entry entry = GroomerApplication.getInstance().getRequestQueue().
                        getCache().get(Constants.VENDOR_LIST);
                if (entry != null) {
                    try {
                        String data = new String(entry.data, "UTF-8");
                        JSONObject response = new JSONObject(data);
                        showSaloonListResponse(response);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Utils.showNoNetworkDialog(mActivity);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void showSaloonListResponse(JSONObject response) {
        if (Utils.getWebServiceStatus(response)) {
            try {
                totalCount = response.getInt("count");
                Type type = new TypeToken<ArrayList<VendorListDTO>>() {
                }.getType();
                List<VendorListDTO> vendorsList = new Gson()
                        .fromJson(response.getJSONArray("saloon").toString(), type);

                if (!flag) {
                    vendorList.addAll(vendorsList);
                    setUpListAdapter(vendorList);
                    flag = true;
                } else {
                    vendorList.addAll(vendorsList);
                    vendorListAdapter.setVendorsList(vendorList);
                    vendorListAdapter.notifyDataSetChanged();
                }
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
                                    vendorListAdapter.notifyItemChanged(pos);
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
        } else {

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

    public class MyThread extends Thread {

        private String categoryId;
        private String distance;
        private String rating;
        private String price;
        private String searchKeyword;

        public MyThread(String categoryId, String distance,
                        String rating, String price,
                        String searchKeyword) {
            this.categoryId = categoryId;
            this.distance = distance;
            this.rating = rating;
            this.price = price;
            this.searchKeyword = searchKeyword;

        }

        @Override
        public void run() {
            try {
                vendorList.clear();
                getVendorsList(categoryId, distance, rating, price, searchKeyword, "1");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}