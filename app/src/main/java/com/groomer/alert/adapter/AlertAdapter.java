package com.groomer.alert.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.groomer.R;

public class AlertAdapter extends RecyclerView.Adapter<AlertAdapter.DetailsViewHolder> {

    public static class DetailsViewHolder extends RecyclerView.ViewHolder {

        ImageView profileImage;
        TextView txt_date, txt_alert_msg;


        public DetailsViewHolder(View itemView) {

            super(itemView);

            profileImage = (ImageView) itemView.findViewById(R.id.img_user_image);
            txt_date = (TextView) itemView.findViewById(R.id.txt_alert_date);
            txt_alert_msg = (TextView) itemView.findViewById(R.id.txt_alert_msg);

        }
    }


    private Context context;


    public AlertAdapter(Context context) {

        this.context = context;

    }

    @Override
    public DetailsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.alert_row_layout, parent, false);

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
