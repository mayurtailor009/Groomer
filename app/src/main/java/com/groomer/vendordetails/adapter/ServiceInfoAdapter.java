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
 * Created by Deepak Singh on 28-Mar-16.
 */
public class ServiceInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<VendorServicesDTO> mList;
    private Context context;

    public static class ServiceInfoHolder extends RecyclerView.ViewHolder {
        TextView mServiceName;
        TextView mServicePrice;
        TextView mServiceTime;

        public ServiceInfoHolder(View view) {
            super(view);
            mServiceName = (TextView) view.findViewById(R.id.confirm_appoint_item_service_name);
            mServicePrice = (TextView) view.findViewById(R.id.confirm_appoint_item_service_price);
            mServiceTime = (TextView) view.findViewById(R.id.confirm_appoint_item_service_time);
        }
    }


    public ServiceInfoAdapter(Context context, List<VendorServicesDTO> mList) {
        this.context = context;
        this.mList = mList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_confirm_appoint_services_item, parent, false);
        return new ServiceInfoHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ServiceInfoHolder mHolder = (ServiceInfoHolder) holder;
        VendorServicesDTO servicesDTO = mList.get(position);
        mHolder.mServiceTime.setText(servicesDTO.getServiceTime());
        mHolder.mServiceName.setText(servicesDTO.getServiceName());
        mHolder.mServicePrice.setText(servicesDTO.getServicePrice());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
