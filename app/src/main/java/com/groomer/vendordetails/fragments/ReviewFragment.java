package com.groomer.vendordetails.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.groomer.R;
import com.groomer.fragments.BaseFragment;
import com.groomer.model.ReviewDTO;
import com.groomer.vendordetails.adapter.ReviewsAdapter;

import java.util.ArrayList;
import java.util.List;


public class ReviewFragment extends BaseFragment {


    private RecyclerView mRecyclerView;
    private View view;
    private Activity mActivity;

    public ReviewFragment() {
        // Required empty public constructor
    }

    public static ReviewFragment newInstance(ArrayList<ReviewDTO> reviewList) {
        ReviewFragment fragment = new ReviewFragment();
        Bundle reviewBundle = new Bundle();
        reviewBundle.putSerializable("reviewList", reviewList);
        fragment.setArguments(reviewBundle);
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
        mRecyclerView = (RecyclerView) view.findViewById(R.id.vendor_review_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        List<ReviewDTO> reviewDTOList = getReviewsList();
        if (reviewDTOList != null && reviewDTOList.size() != 0) {
            mRecyclerView.setVisibility(View.VISIBLE);
            setViewVisibility(R.id.txt_no_data_found, view, View.GONE);
            mRecyclerView.setAdapter(new ReviewsAdapter(mActivity, getReviewsList()));
        } else {
            mRecyclerView.setVisibility(View.GONE);
            setViewVisibility(R.id.txt_no_data_found, view, View.VISIBLE);
        }
    }


    private List<ReviewDTO> getReviewsList() {
        List<ReviewDTO> list = (List<ReviewDTO>) getArguments()
                .getSerializable("reviewList");
        return list;
    }

}
