package com.groomer.category;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.groomer.R;
import com.groomer.activity.BaseActivity;
import com.groomer.category.adapter.VendorListAdapter;
import com.groomer.model.CategoryDTO;
import com.groomer.recyclerviewitemclick.MyOnClickListener;
import com.groomer.recyclerviewitemclick.RecyclerTouchListener;
import com.groomer.vendordetails.VendorDetailsActivity;

public class VendorListActivity extends BaseActivity {


    private Context mActivity;
    private CategoryDTO categoryDTO;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme();
        setContentView(R.layout.activity_vendor_list);

        mActivity = VendorListActivity.this;
        init();
    }

    private void init() {

        categoryDTO = (CategoryDTO) getIntent().getExtras().getSerializable("dto");

/*
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
*/

        setHeader(categoryDTO.getName_eng());

        setLeftClick(R.drawable.back_btn);

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

}