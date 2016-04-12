package com.groomer.appointment.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.groomer.GroomerApplication;
import com.groomer.R;
import com.groomer.model.AppointServicesDTO;
import com.groomer.model.AppointmentDTO;
import com.groomer.reschedule.RescheduleDialogFragment;
import com.groomer.shareexperience.ShareExperienceActivity;
import com.groomer.utillity.Constants;
import com.groomer.utillity.HelpMe;
import com.groomer.utillity.Utils;
import com.groomer.vendordetails.VendorDetailsActivity;
import com.groomer.volley.CustomJsonRequest;

import org.joda.time.DateTime;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Deepak Singh on 29-Mar-16.
 */
public class AppointmentListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<AppointmentDTO> appointsParentList;
    private ExpandableListView mExpandableListView;

    /**
     * this class acts like a holder for group of expandable list.
     */
    private class GroupViewHoler {
        TextView mUserName;
        TextView address;
        TextView time;
        TextView dayName;
        TextView dayNumber;
        TextView month;
        Button shareBtn;
        TextView rating;
        LinearLayout addressLayout;
        LinearLayout timeLayout;
        LinearLayout dateLayout;
        LinearLayout reviewLayout;

        public GroupViewHoler(View view) {
            mUserName = (TextView) view.findViewById(R.id.txt_appointed_user_name);
            address = (TextView) view.findViewById(R.id.txt_appointed_user_address);
            time = (TextView) view.findViewById(R.id.txt_appointed_user_time);
            shareBtn = (Button) view.findViewById(R.id.btn_share_experience);
            addressLayout = (LinearLayout) view.findViewById(R.id.appointed_user_address_layout);
            timeLayout = (LinearLayout) view.findViewById(R.id.layout_appointed_time);
            dateLayout = (LinearLayout) view.findViewById(R.id.date_layout);
            dayName = (TextView) view.findViewById(R.id.txt_day);
            dayNumber = (TextView) view.findViewById(R.id.txt_day_in_number);
            month = (TextView) view.findViewById(R.id.txt_month);
            reviewLayout = (LinearLayout) view.findViewById(R.id.layout_rating_review);
            rating = (TextView) view.findViewById(R.id.txt_user_rating);

        }
    }

    /**
     * this class acts like a holder for children of groups of expandable list.
     */
    private class ChildViewHolder {
        RecyclerView mRecyclerView;
        LinearLayout rescheduleLayout;
        LinearLayout rebookLayout;
        LinearLayout cancelLayout;

        public ChildViewHolder(View view) {
            mRecyclerView = (RecyclerView) view.findViewById(R.id.children_services_list);
            cancelLayout = (LinearLayout) view.findViewById(R.id.layout_cancel);
            rescheduleLayout = (LinearLayout) view.findViewById(R.id.layout_reschedule);
            rebookLayout = (LinearLayout) view.findViewById(R.id.layout_rebook);
        }
    }

    public AppointmentListAdapter(Context context, List<AppointmentDTO> appointmentList, ExpandableListView mExpandableListView) {
        this.context = context;
        this.appointsParentList = appointmentList;
        this.mExpandableListView = mExpandableListView;
    }


    @Override
    public int getGroupCount() {
        return appointsParentList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return appointsParentList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return appointsParentList.get(groupPosition).getService().get(childPosition);
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

        AppointmentDTO mBean = appointsParentList.get(groupPosition);
        if (HelpMe.isArabic(context)) {
            gHolder.mUserName.setText(mBean.getStorename_ara());
        } else {
            gHolder.mUserName.setText(mBean.getStorename_eng());

        }
        gHolder.address.setText(mBean.getAddress());
        gHolder.time.setText(mBean.getTime());

        if (appointsParentList.get(groupPosition).getStatus().equals("2")) {
            gHolder.addressLayout.setVisibility(View.GONE);
            gHolder.timeLayout.setVisibility(View.GONE);
            gHolder.shareBtn.setVisibility(View.VISIBLE);
            performClickOnShareButton(gHolder.shareBtn, groupPosition);
        } else {
            gHolder.addressLayout.setVisibility(View.VISIBLE);
            gHolder.timeLayout.setVisibility(View.VISIBLE);
            gHolder.shareBtn.setVisibility(View.GONE);
        }

        if (Utils.isFromDateGreater(appointsParentList.get(groupPosition).getDate(), Utils.getCurrentDate())) {
            gHolder.dateLayout.setEnabled(false);
            gHolder.dateLayout.setBackgroundColor(context.getResources().getColor(R.color.divider_color));

        } else {
            gHolder.dateLayout.setEnabled(true);
            gHolder.dateLayout.setBackgroundColor(context.getResources().getColor(R.color.green));
        }

        if (mBean.getReview() != null) {
            gHolder.reviewLayout.setVisibility(View.VISIBLE);
            gHolder.addressLayout.setVisibility(View.GONE);
            gHolder.timeLayout.setVisibility(View.GONE);
            gHolder.shareBtn.setVisibility(View.GONE);
            gHolder.rating.setText(mBean.getReview().getRating());

        } else {
            gHolder.reviewLayout.setVisibility(View.GONE);
        }


        DateTime dateTime = DateTime.parse(mBean.getDate());
        gHolder.month.setText(dateTime.toString("MMM"));
        gHolder.dayName.setText(dateTime.toString("E"));
        gHolder.dayNumber.setText(dateTime.toString("dd"));

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

        List<AppointServicesDTO> servicesDTO = appointsParentList.get(groupPosition).getService();

        cHolder.mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        cHolder.mRecyclerView.setAdapter(new AppointListChildAdapter(context, servicesDTO));

        performClickOnRebook(cHolder.rebookLayout, groupPosition);
        performClickOnCancel(cHolder.cancelLayout, groupPosition);
        performClickOnReschedule(cHolder.rescheduleLayout, groupPosition);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    // handles clikc on rebook layout
    private void performClickOnRebook(LinearLayout rebookLayout, final int groupPosition) {
        rebookLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent vendorListIntent = new Intent(context, VendorDetailsActivity.class);
                vendorListIntent.putExtra("store_id", appointsParentList.get(groupPosition).getStore_id());
                context.startActivity(vendorListIntent);
            }
        });
    }

    // handles clikc on reschedule layout
    private void performClickOnReschedule(LinearLayout rescheduleLayout, final int groupPosition) {
        rescheduleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RescheduleDialogFragment dialog = RescheduleDialogFragment.getInstance();
                Bundle bundle = new Bundle();
                bundle.putString("order_id", appointsParentList.get(groupPosition).getOrder_id());
                dialog.setArguments(bundle);
                dialog.show(((Activity) context).getFragmentManager(), "");
            }
        });
    }


    // handles clikc on cancel layout
    private void performClickOnCancel(LinearLayout cancelLayout, final int groupPosition) {
        cancelLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestForCancel(groupPosition);
            }
        });
    }

    //handles click on share button.
    private void performClickOnShareButton(Button shareBtn, final int groupPosition) {
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareExperienceIntent = new Intent(context, ShareExperienceActivity.class);
                shareExperienceIntent.putExtra("store_id",
                        appointsParentList.get(groupPosition).getStore_id());
                context.startActivity(shareExperienceIntent);
            }
        });

    }

    private void requestForCancel(final int position) {
        final String orderID = appointsParentList.get(position).getOrder_id();
        HashMap<String, String> params = new HashMap<>();
        params.put("action", Constants.CANCEL_APPOINTMENT);
        params.put("user_id", Utils.getUserId(context));
        params.put("lang", Utils.getSelectedLanguage(context));
        params.put("order_id", orderID);

        final ProgressDialog pdialog = Utils.createProgressDialog(context, null, false);
        CustomJsonRequest jsonRequest = new CustomJsonRequest(Request.Method.POST,
                Constants.SERVICE_URL, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("Groomer info", response.toString());
                        pdialog.dismiss();
                        if (Utils.getWebServiceStatus(response)) {
                            try {
                                Toast.makeText(context, Utils.getWebServiceMessage(response),
                                        Toast.LENGTH_SHORT).show();
                                for (int i = 0; i < appointsParentList.size(); i++) {
                                    if (orderID.equals(appointsParentList.get(position)
                                            .getOrder_id())) {
                                        appointsParentList.remove(i);
                                        notifyDataSetChanged();
                                    }
                                }
                                int count = getGroupCount();
                                for (int i = 0; i < count; i++)
                                    mExpandableListView.collapseGroup(i);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("Groomer info", error.toString());
                        pdialog.dismiss();
                        Utils.showExceptionDialog(context);
                    }
                }
        );

        pdialog.show();
        GroomerApplication.getInstance().addToRequestQueue(jsonRequest);
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(30000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
}
