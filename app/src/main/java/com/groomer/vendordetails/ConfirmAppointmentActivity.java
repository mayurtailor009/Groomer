package com.groomer.vendordetails;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.andressantibanez.ranger.Ranger;
import com.groomer.R;
import com.groomer.activity.BaseActivity;
import com.groomer.model.VendorServicesDTO;
import com.groomer.vendordetails.adapter.ServiceInfoAdapter;

import org.joda.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

public class ConfirmAppointmentActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private Ranger date_picker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme();
        setContentView(R.layout.activity_confirm_appointment);

        init();
        setUpRecyclerView();

        //setting click event on ranger's arrows
        setClick(R.id.ranger_back_arrow);
        setClick(R.id.ranger_next_arrow);
    }

    private void init(){
        setClick(R.id.confirm_appoint_cross_button);
    }
    private void setUpRecyclerView() {
        date_picker = (Ranger) findViewById(R.id.date_picker);
        mRecyclerView = (RecyclerView) findViewById(R.id.confirm_appoint_service_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new ServiceInfoAdapter(this, getServiceInfoList()));
    }

    private List<VendorServicesDTO> getServiceInfoList() {
        List<VendorServicesDTO> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            VendorServicesDTO servicesDTO = new VendorServicesDTO();
            servicesDTO.setServiceName("Service " + (i+1));
            servicesDTO.setServicePrice("SAR 65");
            servicesDTO.setServiceTime("30 MIN");
            list.add(servicesDTO);
        }
        return list;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.confirm_appoint_cross_button:
                finish();
                break;
            case R.id.ranger_back_arrow:
                decreaseDate();
                break;
            case R.id.ranger_next_arrow:
                increaseDate();
                break;
        }
    }

    /**
     * this method checks that the selected date must be 1 or greater than 1 then
     * it will decrease the selected day by 1.
     */
    private void decreaseDate() {
        if (date_picker.getSelectedDay() > 1) {
            date_picker.setSelectedDay(date_picker.getSelectedDay() - 1, true, 0);
        }
    }

    /**
     * this method first checks for the month and do the increament in date
     * according to the days in the current month. if month is february then it will check for
     * that the current year is leap year or not.
     */
    private void increaseDate() {
        LocalDateTime dateTime = new LocalDateTime();
        int i = dateTime.getMonthOfYear();
        if (i == 1 || i == 3 || i == 5 || i == 7 || i == 8 || i == 10 || i == 12) {
            if (date_picker.getSelectedDay() < 31) {
                date_picker.setSelectedDay(date_picker.getSelectedDay() + 1, true, 0);
            }
        } else if (i == 2) {
            int year = dateTime.getYear();
            boolean b = year % 4 == 0 ? true : false;
            if (b) {
                if (date_picker.getSelectedDay() < 29) {
                    date_picker.setSelectedDay(date_picker.getSelectedDay() + 1, true, 0);
                }
            } else {
                if (date_picker.getSelectedDay() < 28) {
                    date_picker.setSelectedDay(date_picker.getSelectedDay() + 1, true, 0);
                }
            }
        } else {
            if (date_picker.getSelectedDay() < 30) {
                date_picker.setSelectedDay(date_picker.getSelectedDay() + 1, true, 0);
            }
        }
    }
}
