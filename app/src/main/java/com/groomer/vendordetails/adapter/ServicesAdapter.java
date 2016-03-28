package com.groomer.vendordetails.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.groomer.R;
import com.groomer.model.VendorServicesDTO;

import java.util.List;

/**
 * Created by Deepak Singh on 27-Mar-16.
 */
public class ServicesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<VendorServicesDTO> mList;

    public static class ServiceHoler extends RecyclerView.ViewHolder {

        TextView mServiceName;
        TextView mServicePrice;
        TextView mServiceTime;

        public ServiceHoler(View view) {
            super(view);
            mServiceName = (TextView) view.findViewById(R.id.txt_service_name);
            mServicePrice = (TextView) view.findViewById(R.id.txt_service_price);
            mServiceTime = (TextView) view.findViewById(R.id.txt_service_time);
        }
    }

    public ServicesAdapter(Context context, List<VendorServicesDTO> mList) {
        this.context = context;
        this.mList = mList;
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
        VendorServicesDTO servicesDTO = mList.get(position);
        mHolder.mServiceName.setText(servicesDTO.getServiceName());
        mHolder.mServicePrice.setText(servicesDTO.getServicePrice());
        mHolder.mServiceTime.setText(servicesDTO.getServiceTime());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
