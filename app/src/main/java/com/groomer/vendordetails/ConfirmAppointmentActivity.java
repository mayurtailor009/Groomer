package com.groomer.vendordetails;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.groomer.R;
import com.groomer.activity.BaseActivity;
import com.groomer.model.VendorServicesDTO;
import com.groomer.vendordetails.adapter.ServiceInfoAdapter;

import java.util.ArrayList;
import java.util.List;

public class ConfirmAppointmentActivity extends BaseActivity {

    private RecyclerView mRecyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme();
        setContentView(R.layout.activity_confirm_appointment);

        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
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
}
