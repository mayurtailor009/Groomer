package com.groomer.vendordetails.fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.groomer.R;
import com.groomer.fragments.BaseFragment;
import com.groomer.model.VendorReviewsDTO;
import com.groomer.model.VendorServicesDTO;
import com.groomer.vendordetails.adapter.ReviewsAdapter;
import com.groomer.vendordetails.adapter.ServicesAdapter;

import java.util.ArrayList;
import java.util.List;


public class ReviewFragment extends BaseFragment {


    private RecyclerView mReviewList;
    private View view;
    private Activity mActivity;

    public ReviewFragment() {
        // Required empty public constructor
    }

    public static ReviewFragment newInstance() {
        ReviewFragment fragment = new ReviewFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = ReviewFragment.this.getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_review, container, false);

        setUpRecycler();
        return view;
    }


    private void setUpRecycler() {
        mReviewList = (RecyclerView) view.findViewById(R.id.vendor_review_list);
        mReviewList.setLayoutManager(new LinearLayoutManager(mActivity));
        mReviewList.setAdapter(new ReviewsAdapter(mActivity, getReviewsList()));
    }


    private List<VendorReviewsDTO> getReviewsList() {
        List<VendorReviewsDTO> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            VendorReviewsDTO reviewsDTO = new VendorReviewsDTO();
            reviewsDTO.setCategoryName("Hair Cut");
            reviewsDTO.setRating("4.5");
            reviewsDTO.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec ac orci quis odilo bibendum mattis.");
            list.add(reviewsDTO);
        }
        return list;
    }

}
