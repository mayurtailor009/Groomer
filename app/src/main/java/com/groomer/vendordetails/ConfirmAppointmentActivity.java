package com.groomer.vendordetails;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.andressantibanez.ranger.Ranger;
import com.groomer.R;
import com.groomer.activity.BaseActivity;
import com.groomer.model.VendorServicesDTO;
import com.groomer.utillity.Utils;
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
                Utils.decreaseDate(date_picker);
                break;
            case R.id.ranger_next_arrow:
                Utils.increaseDate(date_picker);
                break;
        }
    }
}
