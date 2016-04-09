package com.groomer.vendordetails.adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.groomer.R;
import com.groomer.model.ReviewDTO;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<ReviewDTO> reviewsList;
    private DisplayImageOptions options;

    public static class ReviewHolder extends RecyclerView.ViewHolder {
        TextView txt_service_name;
        TextView txt_service_rating;
        TextView txt_service_review;
        ImageView thumbnail;

        public ReviewHolder(View view) {
            super(view);
            txt_service_name = (TextView) view.findViewById(R.id.txt_service_name);
            txt_service_rating = (TextView) view.findViewById(R.id.txt_service_rating);
            txt_service_review = (TextView) view.findViewById(R.id.txt_service_review);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);

        }
    }


    public ReviewsAdapter(Context context, List<ReviewDTO> mList) {
        this.context = context;
        this.reviewsList = mList;

        options = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new SimpleBitmapDisplayer())
                .showImageOnLoading(R.drawable.default_image)
                .showImageOnFail(R.drawable.default_image)
                .showImageForEmptyUri(R.drawable.default_image)
                .build();
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
        ReviewDTO reviewsDTO = reviewsList.get(position);
        ImageLoader.getInstance().displayImage(reviewsDTO.getUser_image(), mHolder.thumbnail, options);
        mHolder.txt_service_name.setText(reviewsDTO.getUser_name());
        mHolder.txt_service_rating.setText(reviewsDTO.getRating());
        mHolder.txt_service_review.setText(reviewsDTO.getReview());
    }

    @Override
    public int getItemCount() {
        return reviewsList.size();
    }

}
