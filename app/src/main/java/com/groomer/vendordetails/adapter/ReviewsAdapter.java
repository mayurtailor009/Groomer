package com.groomer.vendordetails.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.groomer.R;
import com.groomer.model.VendorReviewsDTO;

import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<VendorReviewsDTO> reviewsList;

    public static class ReviewHolder extends RecyclerView.ViewHolder {
        TextView txt_service_name;
        TextView txt_service_rating;
        TextView txt_service_review;

        public ReviewHolder(View view) {
            super(view);
            txt_service_name = (TextView) view.findViewById(R.id.txt_service_name);
            txt_service_rating = (TextView) view.findViewById(R.id.txt_service_rating);
            txt_service_review = (TextView) view.findViewById(R.id.txt_service_review);
        }
    }


    public ReviewsAdapter(Context context, List<VendorReviewsDTO> mList) {
        this.context = context;
        this.reviewsList = mList;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_vendor_review_item, parent, false);
        return new ReviewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ReviewHolder mHolder = (ReviewHolder) holder;
        VendorReviewsDTO reviewsDTO = reviewsList.get(position);
        mHolder.txt_service_name.setText(reviewsDTO.getCategoryName());
        mHolder.txt_service_rating.setText(reviewsDTO.getRating());
        mHolder.txt_service_review.setText(reviewsDTO.getDescription());
    }

    @Override
    public int getItemCount() {
        return reviewsList.size();
    }

}
