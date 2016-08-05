package com.groomer.category.adapter;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.groomer.R;
import com.groomer.model.VendorListDTO;
import com.groomer.utillity.HelpMe;
import com.groomer.utillity.Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.ArrayList;
import java.util.List;

public class VendorListAdapter extends RecyclerView.Adapter<VendorListAdapter.DetailsViewHolder> {

    private Context context;
    private List<VendorListDTO> vendorsList;
    private DisplayImageOptions options;
    private List<VendorListDTO> filteredList;
    private static MyClickListener myClickListener;

    public static class DetailsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView thumbnail, img_fav;
        TextView txt_vendor_name, txt_vendor_rating, txt_vendor_address, txt_vendor_price, txt_vendor_price_unit, txt_vendor_distance;


        public DetailsViewHolder(View itemView) {

            super(itemView);

            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            txt_vendor_rating = (TextView) itemView.findViewById(R.id.txt_vendor_rating);
            txt_vendor_name = (TextView) itemView.findViewById(R.id.txt_vendor_name);
            txt_vendor_address = (TextView) itemView.findViewById(R.id.txt_vendor_address);
            txt_vendor_price = (TextView) itemView.findViewById(R.id.txt_vendor_price);
            txt_vendor_price_unit = (TextView) itemView.findViewById(R.id.txt_vendor_price_unit);
            img_fav = (ImageView) itemView.findViewById(R.id.img_fav);
            txt_vendor_distance = (TextView) itemView.findViewById(R.id.txt_vendor_distance);


            thumbnail.setOnClickListener(this);
            img_fav.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        VendorListAdapter.myClickListener = myClickListener;
    }

    public VendorListAdapter(Context context, List<VendorListDTO> vendorsList) {
        this.context = context;
        this.vendorsList = vendorsList;
        filteredList = new ArrayList<>();
        filteredList.addAll(vendorsList);

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

    public void setVendorsList(List<VendorListDTO> vendorList) {
        this.vendorsList = vendorList;
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
        if (HelpMe.isArabic(context)) {
            holder.txt_vendor_name.setText(vendorListDTO.getStorename_ara());
            holder.txt_vendor_price_unit.setText(vendorListDTO.getCurrency_ara());
        } else {
            holder.txt_vendor_name.setText(vendorListDTO.getStorename_eng());
            holder.txt_vendor_price_unit.setText(vendorListDTO.getCurrency());
        }
        holder.txt_vendor_price.setText(vendorListDTO.getPrice());
        holder.txt_vendor_address.setText(vendorListDTO.getAddress());
        holder.txt_vendor_rating.setText(vendorListDTO.getRating());

        holder.txt_vendor_distance.setText("(" + vendorListDTO.getDistance() + " " +
                context.getString(R.string.distance_unit_km) + " )");
        if (!Utils.IsSkipLogin((Activity) context)) {
            holder.img_fav.setVisibility(View.VISIBLE);
            if (vendorListDTO.getFavourite().equalsIgnoreCase("1")) {
                holder.img_fav.setImageResource(R.drawable.fav_active_icon);
            } else {
                holder.img_fav.setImageResource(R.drawable.fav_icon);
            }
        } else {
            holder.img_fav.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return vendorsList.size();
    }

    public interface MyClickListener {
        void onItemClick(int position, View v);
    }


    public void getFilteredList(String text) {
        vendorsList.clear();
        if (text.length() == 0) {
            vendorsList.addAll(filteredList);
        } else {
            for (VendorListDTO vendorListDTO : filteredList) {
                if (vendorListDTO.getStorename_eng().toUpperCase()
                        .contains(text.toUpperCase())) {
                    vendorsList.add(vendorListDTO);
                }
            }
        }
        notifyDataSetChanged();
    }
}
