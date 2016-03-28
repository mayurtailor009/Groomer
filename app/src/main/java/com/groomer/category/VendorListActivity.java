package com.groomer.category;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;

import com.groomer.R;
import com.groomer.activity.BaseActivity;
import com.groomer.category.adapter.VendorListAdapter;
import com.groomer.recyclerviewitemclick.MyOnClickListener;
import com.groomer.recyclerviewitemclick.RecyclerTouchListener;
import com.groomer.vendordetails.VendorDetailsActivity;

public class VendorListActivity extends BaseActivity {


    private Context mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_list);

        mActivity = VendorListActivity.this;
        init();
    }

    private void init() {
        RecyclerView vendorRecyclerView = (RecyclerView) findViewById(R.id.recycle_vendor);

        LinearLayoutManager llm = new LinearLayoutManager(mActivity);
        vendorRecyclerView.setLayoutManager(llm);

        VendorListAdapter vendorListAdapter = new VendorListAdapter(mActivity);
        vendorRecyclerView.setAdapter(vendorListAdapter);

        vendorRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(mActivity,
                        vendorRecyclerView, new MyOnClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(mActivity, VendorDetailsActivity.class);
                        mActivity.startActivity(intent);
                    }
                })
        );

    }
}
