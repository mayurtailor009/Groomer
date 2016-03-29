package com.groomer.appointment.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.groomer.R;
import com.groomer.model.AppointmentDTO;
import com.groomer.model.VendorServicesDTO;
import com.groomer.vendordetails.adapter.ServiceInfoAdapter;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Deepak Singh on 29-Mar-16.
 */
public class AppointmentListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<AppointmentDTO> groupHeader;
    private HashMap<Integer, List<VendorServicesDTO>> childrenList;

    /**
     * this class acts like a holder for group of expandable list.
     */
    private class GroupViewHoler {
        TextView mUserName;
        TextView mUserAddress;
        TextView mUserTime;

        public GroupViewHoler(View view) {
            mUserName = (TextView) view.findViewById(R.id.txt_appointed_user_name);
            mUserAddress = (TextView) view.findViewById(R.id.txt_appointed_user_address);
            mUserTime = (TextView) view.findViewById(R.id.txt_appointed_user_time);
        }
    }

    /**
     * this class acts like a holder for children of groups of expandable list.
     */
    private class ChildViewHolder {
        RecyclerView mRecyclerView;

        public ChildViewHolder(View view) {
            mRecyclerView = (RecyclerView) view.findViewById(R.id.children_services_list);
        }
    }

    public AppointmentListAdapter(Context context, List<AppointmentDTO> groupHeader,
                                  HashMap<Integer, List<VendorServicesDTO>> childrenList) {
        this.context = context;
        this.groupHeader = groupHeader;
        this.childrenList = childrenList;
    }


    @Override
    public int getGroupCount() {
        return groupHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childrenList.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHoler gHolder = null;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.layout_appointment_group, null);
            gHolder = new GroupViewHoler(convertView);
            convertView.setTag(gHolder);
        } else {
            gHolder = (GroupViewHoler) convertView.getTag();
        }

        AppointmentDTO mBean = groupHeader.get(groupPosition);
        gHolder.mUserName.setText(mBean.getmUserName());
        gHolder.mUserAddress.setText(mBean.getmUserAddress());
        gHolder.mUserTime.setText(mBean.getmUserTime());

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder cHolder = null;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.layout_appointment_children, null);
            cHolder = new ChildViewHolder(convertView);
            convertView.setTag(cHolder);
        } else {
            cHolder = (ChildViewHolder) convertView.getTag();
        }

        List<VendorServicesDTO> servicesDTO = childrenList.get(groupPosition);
        cHolder.mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        cHolder.mRecyclerView.setAdapter(new ServiceInfoAdapter(context, servicesDTO));

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
