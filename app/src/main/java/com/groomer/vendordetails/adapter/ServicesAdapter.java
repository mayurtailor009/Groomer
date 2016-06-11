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
import android.widget.TextView;

import com.groomer.R;
import com.groomer.model.ServiceDTO;
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


public class ServicesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<ServiceDTO> serviceDTOList;
    private DisplayImageOptions options;
    private double priceSum = 0.0;
    private int serviceCount = 0;
    private List<ServiceDTO> selectedList;
    private PriceServiceInterface mInterface;

    public static class ServiceHolder extends RecyclerView.ViewHolder {

        TextView mServiceName;
        TextView mServicePrice;
        TextView mServiceTime;
        Button btnService;
        ImageView thumbnail;
        boolean clicked;

        public ServiceHolder(View view) {
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
        serviceDTOList = mList;
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
        return new ServiceHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ServiceHolder mHolder = (ServiceHolder) holder;
        mInterface = (PriceServiceInterface) context;
        final ServiceDTO servicesDTO = serviceDTOList.get(position);
        ImageLoader.getInstance().displayImage(servicesDTO.getImage(), mHolder.thumbnail, options);
        if (HelpMe.isArabic(context)) {
            mHolder.mServiceName.setText(servicesDTO.getName_ara());
        } else {
            mHolder.mServiceName.setText(servicesDTO.getName_eng());
        }

        mHolder.mServicePrice.setText("SAR " + servicesDTO.getPrice());
        mHolder.mServiceTime.setText(servicesDTO.getDuration());
        if (serviceDTOList.get(position).isSelected()) {
            buttonSelected(true, mHolder.btnService);
            mHolder.btnService.setText(context.getString(R.string.txt_selected));
            //serviceCount++;
        } else {
            buttonSelected(false, mHolder.btnService);
            mHolder.btnService.setText(context.getString(R.string.txt_select));
        }
        mHolder.btnService.setTag(position);
        mHolder.btnService.setOnClickListener(selectClick);
    }

    View.OnClickListener selectClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (Utils.IsSkipLogin((Activity) context)) {
                Utils.showDialog(context,
                        context.getString(R.string.message_title),
                        context.getString(R.string.for_access_this_please_login),
                        context.getString(R.string.txt_login),
                        context.getString(R.string.canceled), login);
            } else {
                Button btn = (Button) v;
                int pos = Integer.parseInt(v.getTag().toString());
                ServiceDTO servicesDTO = serviceDTOList.get(pos);
                //mHolder.clicked = mHolder.clicked ? false : true;
                if (!serviceDTOList.get(pos).isSelected()) {
                    // v.setSelected(true);
                    buttonSelected(true, btn);

                    btn.setText(context.getString(R.string.txt_selected));
                    priceSum += Double.parseDouble(servicesDTO.getPrice());
                    serviceCount++;
                    selectedList.add(servicesDTO);
                    serviceDTOList.get(pos).setIsSelected(true);

                } else {
                    buttonSelected(false, btn);
                    //v.setSelected(false);
                    btn.setText(context.getString(R.string.txt_select));
                    if (priceSum > 0 && serviceCount > 0) {
                        priceSum -= Double.parseDouble(servicesDTO.getPrice());
                        serviceCount--;
                        selectedList.remove(servicesDTO);
                        serviceDTOList.get(pos).setIsSelected(false);
                    }
                }
                mInterface.getPriceSum(priceSum + "");
                mInterface.getServiceCount(serviceCount + "");

                mInterface.getSelectedServiceList(serviceDTOList);
                //serviceCount=0;
                notifyDataSetChanged();
            }
        }
    };

    DialogInterface.OnClickListener login = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            SessionManager.logoutUser(context);
            ;
        }
    };


    // TODO for change theme based color
    private void buttonSelected(boolean isSelected, Button btn) {
        if (isSelected) {
            btn.setBackgroundColor(context.getResources().getColor(R.color.grey));
            btn.setTextColor(context.getResources().getColor(R.color.black));

        } else {
            Theme theme = Utils.getObjectFromPref(context, Constants.CURRENT_THEME);

            if (theme.equals(Theme.Red)) {
                btn.setBackgroundColor(context.getResources().getColor(R.color.red));

            } else if (theme.equals(Theme.Blue)) {
                btn.setBackgroundColor(context.getResources().getColor(R.color.blue_light));
            } else {
                btn.setBackgroundColor(context.getResources().getColor(R.color.green));
            }


            btn.setTextColor(context.getResources().getColor(R.color.colorWhite));
        }
    }

    @Override
    public int getItemCount() {
        return serviceDTOList.size();
    }
}
