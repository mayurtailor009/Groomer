package com.groomer.category;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
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
import com.groomer.model.CategoryDTO;
import com.groomer.model.VendorListDTO;
import com.groomer.recyclerviewitemclick.MyOnClickListener;
import com.groomer.recyclerviewitemclick.RecyclerTouchListener;
import com.groomer.utillity.Constants;
import com.groomer.utillity.Utils;
import com.groomer.vendordetails.VendorDetailsActivity;
import com.groomer.volley.CustomJsonRequest;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VendorListActivity extends BaseActivity {


    private Context mActivity;
    private RecyclerView vendorRecyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme();
        setContentView(R.layout.activity_vendor_list);

        mActivity = VendorListActivity.this;

        CategoryDTO categoryDTO = (CategoryDTO) getIntent().getExtras().getSerializable("dto");
        init(categoryDTO);

        getVendorsList(categoryDTO);
    }

    private void init(CategoryDTO categoryDTO) {
/*
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
*/

        setHeader(categoryDTO.getName_eng());

        setLeftClick(R.drawable.back_btn);

        vendorRecyclerView = (RecyclerView) findViewById(R.id.recycle_vendor);

        LinearLayoutManager llm = new LinearLayoutManager(mActivity);
        vendorRecyclerView.setLayoutManager(llm);
    }

    private void setUpListAdapter(final List<VendorListDTO> vendorList) {
        VendorListAdapter vendorListAdapter = new VendorListAdapter(mActivity, vendorList);
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


        ((VendorListAdapter) vendorListAdapter).setOnItemClickListener(new VendorListAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                switch (v.getId()) {
                    case R.id.thumbnail:
                        Intent intent = new Intent(mActivity, VendorDetailsActivity.class);
                        intent.putExtra("store_id", vendorList.get(position).getStore_id());
                        mActivity.startActivity(intent);
                        break;

                    case R.id.img_fav:

                        if (vendorList.get(position).getFavourite().equalsIgnoreCase("1")) {

                            addRemoveFromFavourite("0", vendorList.get(position).getStore_id());

                        } else {
                            addRemoveFromFavourite("1", vendorList.get(position).getStore_id());

                        }

                        break;


                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vendor_list, menu);
        return true;
    }


    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.hamburgur_img_icon:
                finish();
                break;
        }
    }


    private void getVendorsList(CategoryDTO categoryDTO) {
        if (Utils.isOnline(mActivity)) {
            HashMap<String, String> params = new HashMap<>();
            params.put("action", Constants.VENDOR_LIST);
            params.put("lat", "");
            params.put("lng", "");
            params.put("user_id", Utils.getUserId(mActivity));
            params.put("category_id", categoryDTO.getId());
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
                                    Type type = new TypeToken<ArrayList<VendorListDTO>>() {
                                    }.getType();
                                    List<VendorListDTO> vendorList = new Gson()
                                            .fromJson(response.getJSONArray("saloon").toString(), type);
                                    setUpListAdapter(vendorList);
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
                        }
                    }
            );

            pdialog.show();
            GroomerApplication.getInstance().addToRequestQueue(jsonRequest);
            jsonRequest.setRetryPolicy(new DefaultRetryPolicy(30000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
                        }
                    }
            );

            pdialog.show();
            GroomerApplication.getInstance().addToRequestQueue(jsonRequest);
            jsonRequest.setRetryPolicy(new DefaultRetryPolicy(30000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        }


    }


}