package com.groomer.appointment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.groomer.R;
import com.groomer.model.AppointServicesDTO;
import com.groomer.model.ServiceDTO;
import com.groomer.utillity.HelpMe;

import java.util.List;

/**
 * Created by Deepak Singh on 09-Apr-16.
 */
public class ModifySwipeMenuListViewAdapter extends BaseAdapter {

    private List<AppointServicesDTO> mList;
    private Context context;

    public static class ServiceHolder {
        TextView mServiceName;
        TextView mServicePrice;
        TextView mServiceTime;
    }

    public ModifySwipeMenuListViewAdapter(Context context, List<AppointServicesDTO> mList) {
        this.context = context;
        this.mList = mList;
    }

    @Override
    public int getViewTypeCount() {

        return 1;
    }

    public void setServiceList(List<AppointServicesDTO> mList) {
        this.mList = mList;
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
        ServiceHolder holder = null;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.layout_confirm_appoint_services_item,
                    parent, false);
            holder = new ServiceHolder();
            holder.mServiceName = (TextView) convertView.findViewById(R.id.confirm_appoint_item_service_name);
            holder.mServicePrice = (TextView) convertView.findViewById(R.id.confirm_appoint_item_service_price);
            holder.mServiceTime = (TextView) convertView.findViewById(R.id.confirm_appoint_item_service_time);
            convertView.setTag(holder);
        } else {
            holder = (ServiceHolder) convertView.getTag();
        }

        AppointServicesDTO servicesDTO = mList.get(position);
        holder.mServiceTime.setText(servicesDTO.getDuration());

        if (HelpMe.isArabic(context)) {
            holder.mServiceName.setText(servicesDTO.getService_name_ara());
        } else {
            holder.mServiceName.setText(servicesDTO.getService_name_eng());
        }

        holder.mServicePrice.setText(context.getString(R.string.txt_vendor_price_unit)
                + " " + servicesDTO.getPrice());

        return convertView;
    }
}
