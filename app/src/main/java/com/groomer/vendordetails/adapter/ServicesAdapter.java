package com.groomer.vendordetails.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.groomer.R;
import com.groomer.model.ServiceDTO;
import com.groomer.model.VendorServicesDTO;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.List;


public class ServicesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<ServiceDTO> mList;
    private DisplayImageOptions options;

    public static class ServiceHoler extends RecyclerView.ViewHolder {

        TextView mServiceName;
        TextView mServicePrice;
        TextView mServiceTime;
        Button btnService;
        ImageView thumbnail;

        public ServiceHoler(View view) {
            super(view);
            mServiceName = (TextView) view.findViewById(R.id.txt_service_name);
            mServicePrice = (TextView) view.findViewById(R.id.txt_service_price);
            mServiceTime = (TextView) view.findViewById(R.id.txt_service_time);
            btnService = (Button) view.findViewById(R.id.btn_service_select);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);

            btnService.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button btn = (Button)v;
                    v.setSelected(true);
                    btn.setText("Selected");
                }
            });
        }
    }

    public ServicesAdapter(Context context, List<ServiceDTO> mList) {
        this.context = context;
        this.mList = mList;

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
        View view = inflater.inflate(R.layout.layout_vendor_services_item, parent, false);
        return new ServiceHoler(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ServiceHoler mHolder = (ServiceHoler) holder;
        ServiceDTO servicesDTO = mList.get(position);
        ImageLoader.getInstance().displayImage(servicesDTO.getImage(), mHolder.thumbnail, options);
        mHolder.mServiceName.setText(servicesDTO.getName_eng());
        mHolder.mServicePrice.setText("SAR " + servicesDTO.getPrice());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
