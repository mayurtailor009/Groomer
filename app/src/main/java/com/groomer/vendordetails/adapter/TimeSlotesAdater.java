package com.groomer.vendordetails.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.groomer.R;
import com.groomer.model.ServiceDTO;
import com.groomer.model.SloteDTO;
import com.groomer.utillity.Constants;
import com.groomer.utillity.HelpMe;
import com.groomer.utillity.SessionManager;
import com.groomer.utillity.Theme;
import com.groomer.utillity.Utils;
import com.groomer.vendordetails.priceserviceinterface.PriceServiceInterface;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YusataInfotech on 8/5/2016.
 */
public class TimeSlotesAdater extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<SloteDTO> serviceDTOList;


    public static class ServiceHolder extends RecyclerView.ViewHolder {

        TextView startTime;
        TextView endTime, to;
        LinearLayout llDateTime;


        public ServiceHolder(View view) {
            super(view);
            startTime = (TextView) view.findViewById(R.id.txt_start_time);
            endTime = (TextView) view.findViewById(R.id.txt_end_time);
            llDateTime = (LinearLayout) view.findViewById(R.id.ll_date_time);
            to = (TextView) view.findViewById(R.id.txt_to);

        }
    }


    public TimeSlotesAdater(Context context, List<SloteDTO> mList) {
        this.context = context;
        serviceDTOList = mList;


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_vendor_time_slot, parent, false);
        return new ServiceHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ServiceHolder mHolder = (ServiceHolder) holder;
        final SloteDTO servicesDTO = serviceDTOList.get(position);
        mHolder.startTime.setText(servicesDTO.getStart_time());
        mHolder.endTime.setText(servicesDTO.getEnd_time());
        if (servicesDTO.getSelected().equalsIgnoreCase("")) {
            buttonSelected(false, mHolder.llDateTime, mHolder.startTime, mHolder.endTime, mHolder.to);
        } else {
            buttonSelected(true, mHolder.llDateTime, mHolder.startTime, mHolder.endTime, mHolder.to);
        }

    }


    private void buttonSelected(boolean isSelected, LinearLayout btn, TextView startDate, TextView endDate, TextView to) {
        if (isSelected) {

            Theme theme = Utils.getObjectFromPref(context, Constants.CURRENT_THEME);
            startDate.setTextColor(context.getResources().getColor(R.color.colorWhite));
            endDate.setTextColor(context.getResources().getColor(R.color.colorWhite));
            to.setTextColor(context.getResources().getColor(R.color.colorWhite));


            if (theme.equals(Theme.Red)) {
                btn.setBackgroundColor(context.getResources().getColor(R.color.red));

            } else if (theme.equals(Theme.Blue)) {
                btn.setBackgroundColor(context.getResources().getColor(R.color.blue_light));
            } else {
                btn.setBackgroundColor(context.getResources().getColor(R.color.green));
            }
        } else {
            btn.setBackgroundColor(context.getResources().getColor(R.color.grey));
            startDate.setTextColor(context.getResources().getColor(R.color.black));
            endDate.setTextColor(context.getResources().getColor(R.color.black));
            to.setTextColor(context.getResources().getColor(R.color.black));

        }
    }


    public void setServiceDTOList(List<SloteDTO> mList)
    {
        serviceDTOList = mList;
    }

    @Override
    public int getItemCount() {
        return serviceDTOList.size();
    }
}

