package com.groomer.alert.adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.groomer.R;
import com.groomer.model.NotificationDTO;
import com.groomer.utillity.Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.List;

public class AlertAdapter extends RecyclerView.Adapter<AlertAdapter.DetailsViewHolder> {

    public static class DetailsViewHolder extends RecyclerView.ViewHolder {

        ImageView img_thumbnail;
        TextView txt_date;
        TextView txt_alert_msg;


        public DetailsViewHolder(View itemView) {

            super(itemView);

            img_thumbnail = (ImageView) itemView.findViewById(R.id.img_user_image);
            txt_date = (TextView) itemView.findViewById(R.id.txt_alert_date);
            txt_alert_msg = (TextView) itemView.findViewById(R.id.txt_alert_msg);

        }
    }


    private Context context;
    private List<NotificationDTO> notificationDTOList;
    private DisplayImageOptions options;


    public AlertAdapter(Context context, List<NotificationDTO> notificationDTOList) {

        this.context = context;
        this.notificationDTOList = notificationDTOList;
        options = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new SimpleBitmapDisplayer())
                .showImageOnLoading(R.drawable.avater)
                .showImageOnFail(R.drawable.avater)
                .showImageForEmptyUri(R.drawable.avater)
                .build();


    }

    @Override
    public DetailsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.alert_row_layout, parent, false);

        DetailsViewHolder detailsViewHolder = new DetailsViewHolder(v);
        return detailsViewHolder;
    }

    @Override
    public void onBindViewHolder(DetailsViewHolder holder, int position) {

        if (notificationDTOList.get(position).getImage() != null &&
                ! notificationDTOList.get(position).getImage().equalsIgnoreCase("")) {
            ImageLoader.getInstance().displayImage(notificationDTOList.get(position).getImage(),
                    holder.img_thumbnail,
                    options);
        }

        holder.txt_alert_msg.setText(notificationDTOList.get(position).
                getMessage());
        holder.txt_date.setText(Utils.secondsToDate(notificationDTOList.
                get(position).getTimestamp()));

    }

    @Override
    public int getItemCount() {
        return notificationDTOList.size();
    }


}
