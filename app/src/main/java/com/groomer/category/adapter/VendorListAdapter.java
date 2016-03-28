package com.groomer.category.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.groomer.R;

public class VendorListAdapter extends RecyclerView.Adapter<VendorListAdapter.DetailsViewHolder> {

    private Context context;

    public static class DetailsViewHolder extends RecyclerView.ViewHolder {

        ImageView thumbnail;
        TextView txt_vendor_name, txt_vendor_rating, txt_vendor_address, txt_vendor_price, txt_vendor_price_unit;


        public DetailsViewHolder(View itemView) {

            super(itemView);

            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            txt_vendor_rating = (TextView) itemView.findViewById(R.id.txt_vendor_rating);
            txt_vendor_name = (TextView) itemView.findViewById(R.id.txt_vendor_address);
            txt_vendor_address = (TextView) itemView.findViewById(R.id.txt_alert_date);
            txt_vendor_price = (TextView) itemView.findViewById(R.id.txt_vendor_price);

            txt_vendor_price_unit = (TextView) itemView.findViewById(R.id.txt_vendor_price_unit);

        }
    }

    public VendorListAdapter(Context context) {

        this.context = context;

    }

    @Override
    public DetailsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.vendor_row_layouts, parent, false);

        DetailsViewHolder detailsViewHolder = new DetailsViewHolder(v);
        return detailsViewHolder;
    }

    @Override
    public void onBindViewHolder(DetailsViewHolder holder, int position) {


    }

    @Override
    public int getItemCount() {
        return 2;
    }


}
