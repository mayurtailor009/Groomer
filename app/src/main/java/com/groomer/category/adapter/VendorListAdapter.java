package com.groomer.category.adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.groomer.GroomerApplication;
import com.groomer.R;
import com.groomer.model.VendorListDTO;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.List;

public class VendorListAdapter extends RecyclerView.Adapter<VendorListAdapter.DetailsViewHolder> {

    private Context context;
    private List<VendorListDTO> vendorsList;
    private DisplayImageOptions options;
    private static MyClickListener myClickListener;

    public static class DetailsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView thumbnail, img_fav;
        TextView txt_vendor_name, txt_vendor_rating, txt_vendor_address, txt_vendor_price, txt_vendor_price_unit;


        public DetailsViewHolder(View itemView) {

            super(itemView);

            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            txt_vendor_rating = (TextView) itemView.findViewById(R.id.txt_vendor_rating);
            txt_vendor_name = (TextView) itemView.findViewById(R.id.txt_vendor_address);
            txt_vendor_address = (TextView) itemView.findViewById(R.id.txt_vendor_address);
            txt_vendor_price = (TextView) itemView.findViewById(R.id.txt_vendor_price);
            txt_vendor_price_unit = (TextView) itemView.findViewById(R.id.txt_vendor_price_unit);
            img_fav = (ImageView) itemView.findViewById(R.id.img_fav);


            thumbnail.setOnClickListener(this);
            img_fav.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public VendorListAdapter(Context context, List<VendorListDTO> vendorsList) {
        this.context = context;
        this.vendorsList = vendorsList;

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
    public DetailsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.vendor_row_layouts, parent, false);

        DetailsViewHolder detailsViewHolder = new DetailsViewHolder(v);
        return detailsViewHolder;
    }

    @Override
    public void onBindViewHolder(DetailsViewHolder holder, int position) {
        VendorListDTO vendorListDTO = vendorsList.get(position);
        ImageLoader.getInstance().displayImage(vendorListDTO.getImage(), holder.thumbnail, options);
        holder.txt_vendor_name.setText(vendorListDTO.getStorename_eng());
        holder.txt_vendor_price.setText(vendorListDTO.getPrice());
        holder.txt_vendor_price_unit.setText(vendorListDTO.getCurrency());
        holder.txt_vendor_address.setText(vendorListDTO.getAddress());
        holder.txt_vendor_rating.setText(vendorListDTO.getRating());
        if (vendorListDTO.getFavourite().equalsIgnoreCase("1")) {
            holder.img_fav.setImageResource(R.drawable.fav_active_icon);
        } else {
            holder.img_fav.setImageResource(R.drawable.fav_icon);
        }

    }

    @Override
    public int getItemCount() {
        return vendorsList.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}
