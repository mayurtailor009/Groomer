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
import com.groomer.vendordetails.priceserviceinterface.PriceServiceInterface;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.ArrayList;
import java.util.List;


public class ServicesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<ServiceDTO> mList;
    private DisplayImageOptions options;
    private double priceSum = 0.0;
    private int serviceCount = 0;
    private List<ServiceDTO> selectedList;
    private PriceServiceInterface mInterface;

    public static class ServiceHoler extends RecyclerView.ViewHolder {

        TextView mServiceName;
        TextView mServicePrice;
        TextView mServiceTime;
        Button btnService;
        ImageView thumbnail;
        boolean clicked;

        public ServiceHoler(View view) {
            super(view);
            mServiceName = (TextView) view.findViewById(R.id.txt_service_name);
            mServicePrice = (TextView) view.findViewById(R.id.txt_service_price);
            mServiceTime = (TextView) view.findViewById(R.id.txt_service_time);
            btnService = (Button) view.findViewById(R.id.btn_service_select);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            clicked = false;
        }
    }

    public ServicesAdapter(Context context, List<ServiceDTO> mList) {
        this.context = context;
        this.mList = mList;
        selectedList = new ArrayList<>();

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
        final ServiceHoler mHolder = (ServiceHoler) holder;
        mInterface = (PriceServiceInterface) context;
        final ServiceDTO servicesDTO = mList.get(position);
        ImageLoader.getInstance().displayImage(servicesDTO.getImage(), mHolder.thumbnail, options);
        mHolder.mServiceName.setText(servicesDTO.getName_eng());
        mHolder.mServicePrice.setText("SAR " + servicesDTO.getPrice());

        mHolder.btnService.setTag(position);
        mHolder.btnService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button btn = (Button) v;
                mHolder.clicked = mHolder.clicked ? false : true;
                if (mHolder.clicked) {
                    v.setSelected(true);
                    btn.setText("Selected");
                    priceSum += Double.parseDouble(servicesDTO.getPrice());
                    serviceCount++;
                    selectedList.add(servicesDTO);

                } else {
                    v.setSelected(false);
                    btn.setText("Select");
                    if (priceSum > 0 && serviceCount > 0) {
                        priceSum -= Double.parseDouble(servicesDTO.getPrice());
                        serviceCount--;
                        selectedList.remove(servicesDTO);
                    }
                }
                mInterface.getPriceSum(priceSum + "");
                mInterface.getServiceCount(serviceCount + "");
                mInterface.getSelectedServiceList(selectedList);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
