package com.groomer.appointment.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.groomer.GroomerApplication;
import com.groomer.R;
import com.groomer.model.AppointServicesDTO;
import com.groomer.model.AppointmentDTO;
import com.groomer.reschedule.RescheduleDialogNewFragment;
import com.groomer.shareexperience.ShareExperienceActivity;
import com.groomer.utillity.Constants;
import com.groomer.utillity.HelpMe;
import com.groomer.utillity.Theme;
import com.groomer.utillity.Utils;
import com.groomer.vendordetails.VendorDetailsActivity;
import com.groomer.volley.CustomJsonRequest;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import org.joda.time.DateTime;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Deepak Singh on 29-Mar-16.
 */
public class AppointmentListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<AppointmentDTO> appointsParentList;
    private ExpandableListView mExpandableListView;
    private DisplayImageOptions options;

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
        TextView review;
        LinearLayout addressLayout;
        LinearLayout timeLayout;
        LinearLayout dateLayout;
        LinearLayout reviewLayout;
        CircleImageView img_user_image;

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
            review = (TextView) view.findViewById(R.id.txt_user_review);
            img_user_image = (CircleImageView) view.findViewById(R.id.img_user_image);

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
        LinearLayout llOpteration;

        public ChildViewHolder(View view) {
            mRecyclerView = (RecyclerView) view.findViewById(R.id.children_services_list);
            cancelLayout = (LinearLayout) view.findViewById(R.id.layout_cancel);
            rescheduleLayout = (LinearLayout) view.findViewById(R.id.layout_reschedule);
            rebookLayout = (LinearLayout) view.findViewById(R.id.layout_rebook);
            llOpteration = (LinearLayout) view.findViewById(R.id.bottom_button_layout);
        }
    }

    public AppointmentListAdapter(Context context,
                                  List<AppointmentDTO> appointmentList,
                                  ExpandableListView mExpandableListView) {
        this.context = context;
        this.appointsParentList = appointmentList;
        this.mExpandableListView = mExpandableListView;

        options = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new SimpleBitmapDisplayer())
                .showImageOnLoading(R.drawable.avater)
                .showImageOnFail(R.drawable.avater)
                .showImageForEmptyUri(R.drawable.avater)
                .build();

    }

    private void setAppointsParentList(List<AppointmentDTO> appointmentList) {
        this.appointsParentList = appointmentList;
    }

    @Override
    public int getGroupCount() {
        return appointsParentList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        //return appointsParentList.get(groupPosition).getService().size();
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

        ImageLoader.getInstance().displayImage(mBean.getImage(),
                gHolder.img_user_image, options);

        if (appointsParentList.get(groupPosition).getStatus().equals(Constants.COMPLETED)) {
            gHolder.addressLayout.setVisibility(View.GONE);
            gHolder.timeLayout.setVisibility(View.GONE);
            gHolder.shareBtn.setVisibility(View.VISIBLE);
            performClickOnShareButton(gHolder.shareBtn, groupPosition);
        } else {
            gHolder.addressLayout.setVisibility(View.VISIBLE);
            gHolder.timeLayout.setVisibility(View.VISIBLE);
            gHolder.shareBtn.setVisibility(View.GONE);
        }

        if (Utils.isFromDateGreater(appointsParentList.get(groupPosition).getDate(), Utils.getCurrentDate())
                || appointsParentList.get(groupPosition).getStatus().equals(Constants.CANCELLED)) {
            gHolder.dateLayout.setEnabled(false);
            //mBean.setPassedDateFlag(true);
            gHolder.dateLayout.setBackgroundColor(context.getResources().getColor(R.color.divider_color));

        } else {
            gHolder.dateLayout.setEnabled(true);
            Theme theme = Utils.getObjectFromPref(context, Constants.CURRENT_THEME);

            if (theme.equals(Theme.Blue)) {
                gHolder.dateLayout.setBackgroundColor(context.getResources().getColor(R.color.blue_light));
            } else if (theme.equals(Theme.Red)) {
                gHolder.dateLayout.setBackgroundColor(context.getResources().getColor(R.color.red));
            } else {
                gHolder.dateLayout.setBackgroundColor(context.getResources().getColor(R.color.green));
            }

        }

        if (mBean.getReview() != null) {

            gHolder.reviewLayout.setVisibility(View.VISIBLE);
            gHolder.addressLayout.setVisibility(View.GONE);
            gHolder.timeLayout.setVisibility(View.GONE);
            gHolder.shareBtn.setVisibility(View.GONE);
            gHolder.rating.setText(mBean.getReview().getRating());
            gHolder.review.setText(mBean.getReview().getReview());

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


        if ((appointsParentList.get(groupPosition).getStatus().equals(Constants.COMPLETED))
                || (appointsParentList.get(groupPosition).getStatus().equals(Constants.CANCELLED))) {
            cHolder.llOpteration.setVisibility(View.GONE);
        }else{
            cHolder.llOpteration.setVisibility(View.VISIBLE);
        }

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
                RescheduleDialogNewFragment dialog = RescheduleDialogNewFragment.getInstance();
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
                shareExperienceIntent.putExtra("order_id",
                        appointsParentList.get(groupPosition).getOrder_id());
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
                                getAppointmentList();
//                                Toast.makeText(context, Utils.getWebServiceMessage(response),
//                                        Toast.LENGTH_SHORT).show();
//                                for (int i = 0; i < appointsParentList.size(); i++) {
//                                    if (orderID.equals(appointsParentList.get(position)
//                                            .getOrder_id())) {
//                                        appointsParentList.remove(i);
//                                        notifyDataSetChanged();
//                                    }
//                                }
//                                int count = getGroupCount();
//                                for (int i = 0; i < count; i++)
//                                    mExpandableListView.collapseGroup(i);
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

    private void getAppointmentList() {
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("action", Constants.APPOINTMENTS);
            params.put("user_id", Utils.getUserId(context));
            params.put("lang", Utils.getSelectedLanguage(context));

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
                                    Type type = new TypeToken<ArrayList<AppointmentDTO>>() {
                                    }.getType();
                                    List<AppointmentDTO> appointmentList = new Gson()
                                            .fromJson(response
                                                    .getJSONArray("appointment").toString(), type);
                                    setAppointsParentList(appointmentList);
                                    notifyDataSetChanged();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
