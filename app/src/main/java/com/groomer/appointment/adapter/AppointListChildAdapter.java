package com.groomer.appointment.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.groomer.R;
import com.groomer.model.AppointServicesDTO;
import com.groomer.utillity.GroomerPreference;
import com.groomer.utillity.HelpMe;

import java.util.List;

/**
 * Created by Deepak Singh on 10-Apr-16.
 */
public class AppointListChildAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<AppointServicesDTO> mList;
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


    public AppointListChildAdapter(Context context, List<AppointServicesDTO> mList) {
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
        AppointServicesDTO servicesDTO = mList.get(position);
        mHolder.mServiceTime.setText(servicesDTO.getDuration() + " MIN");
        if (HelpMe.isArabic(context)) {
            mHolder.mServiceName.setText(servicesDTO.getService_name_ara());
        } else {
            mHolder.mServiceName.setText(servicesDTO.getService_name_eng());

        }
        mHolder.mServicePrice.setText("SRA " + servicesDTO.getPrice());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
