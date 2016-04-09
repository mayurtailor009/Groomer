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
        if (getReviewsList() != null) {
            mReviewList.setAdapter(new ReviewsAdapter(mActivity, getReviewsList()));
        } else {
            Toast.makeText(mActivity, "List is empty.", Toast.LENGTH_SHORT).show();
        }
    }


    private List<ReviewDTO> getReviewsList() {
        List<ReviewDTO> list = (List<ReviewDTO>) getArguments()
                .getSerializable("reviewList");
        return list;
    }

}
