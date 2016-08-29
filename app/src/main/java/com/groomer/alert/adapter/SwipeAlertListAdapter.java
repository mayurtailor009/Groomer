package com.groomer.alert.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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

/**
 * Created by Deepak Singh on 29-08-2016.
 */
public class SwipeAlertListAdapter extends BaseAdapter {

    private Context context;
    private List<NotificationDTO> mList;
    private DisplayImageOptions options;

    public static class DetailsViewHolder {

        ImageView img_thumbnail;
        TextView txt_date;
        TextView txt_alert_msg;

        public DetailsViewHolder(View itemView) {
            img_thumbnail = (ImageView) itemView.findViewById(R.id.img_user_image);
            txt_date = (TextView) itemView.findViewById(R.id.txt_alert_date);
            txt_alert_msg = (TextView) itemView.findViewById(R.id.txt_alert_msg);

        }
    }

    public SwipeAlertListAdapter(Context context, List<NotificationDTO> mList) {
        this.context = context;
        this.mList = mList;
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
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DetailsViewHolder holder = null;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.alert_row_layout, parent, false);
            holder = new DetailsViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (DetailsViewHolder) convertView.getTag();
        }

        if (mList.get(position).getImage() != null &&
                ! mList.get(position).getImage().equalsIgnoreCase("")) {
            ImageLoader.getInstance().displayImage(mList.get(position).getImage(),
                    holder.img_thumbnail,
                    options);
        }

        holder.txt_alert_msg.setText(mList.get(position).
                getMessage());
        holder.txt_date.setText(Utils.secondsToDate(mList.
                get(position).getTimestamp()));

        return convertView;
    }
}
