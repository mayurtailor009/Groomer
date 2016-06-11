package com.groomer.favourite;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.groomer.GroomerApplication;
import com.groomer.R;
import com.groomer.category.adapter.VendorListAdapter;
import com.groomer.fragments.BaseFragment;
import com.groomer.home.HomeActivity;
import com.groomer.model.VendorListDTO;
import com.groomer.utillity.Constants;
import com.groomer.utillity.Utils;
import com.groomer.vendordetails.VendorDetailsActivity;
import com.groomer.volley.CustomJsonRequest;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class FavouriteFragment extends BaseFragment {


    private View view;
    private Context mActivity;
    private RecyclerView favouriteRecyclerView;
    private VendorListAdapter vendorListAdapter;

    public FavouriteFragment() {
        // Required empty public constructor
    }

    public static FavouriteFragment newInstance() {
        FavouriteFragment fragment = new FavouriteFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        view = inflater.inflate(R.layout.fragment_favourite, container, false);
        mActivity = FavouriteFragment.this.getActivity();
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        favouriteRecyclerView = (RecyclerView) view.findViewById(R.id.recycle_favourite);

        LinearLayoutManager llm = new LinearLayoutManager(mActivity);
        favouriteRecyclerView.setLayoutManager(llm);
        getFavoriteList();

    }


    private void getFavoriteList() {
        if (Utils.isOnline(mActivity)) {
            HashMap<String, String> params = new HashMap<>();
            params.put("action", Constants.FAVOURITE_LIST);
            params.put("user_id", Utils.getUserId(mActivity));
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
                                    Type type = new TypeToken<ArrayList<VendorListDTO>>() {
                                    }.getType();

                                    List<VendorListDTO> vendorList = new Gson()
                                            .fromJson(response.getJSONArray("saloon").toString(), type);
                                    setUpListAdapter(vendorList);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                setViewVisibility(R.id.no_data, view, View.VISIBLE);
                                setViewVisibility(R.id.recycle_favourite, view, View.GONE);
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


    private void setUpListAdapter(final List<VendorListDTO> vendorList) {
        setViewVisibility(R.id.no_data, view, View.GONE);
        setViewVisibility(R.id.recycle_favourite, view, View.VISIBLE);

        vendorListAdapter = new VendorListAdapter(mActivity, vendorList);
        favouriteRecyclerView.setAdapter(vendorListAdapter);


        vendorListAdapter.setOnItemClickListener(new VendorListAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                switch (v.getId()) {


                    case R.id.img_fav:

                        if (vendorList.get(position).getFavourite().equalsIgnoreCase("1")) {

                            addRemoveFromFavourite("0", vendorList.get(position).getStore_id());

                        } else {
                            addRemoveFromFavourite("1", vendorList.get(position).getStore_id());

                        }

                        break;

                    case R.id.thumbnail:

                        Intent intent = new Intent(mActivity, VendorDetailsActivity.class);
                        intent.putExtra("store_id", vendorList.get(position).getStore_id());
                        mActivity.startActivity(intent);

                        break;


                }
            }
        });
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

                                    Intent intent = new Intent(mActivity, HomeActivity.class);
                                    intent.putExtra("fragmentNumber", 3);
                                    startActivity(intent);

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


}
