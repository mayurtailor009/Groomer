package com.groomer.vendordetails.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.groomer.R;
import com.groomer.fragments.BaseFragment;
import com.groomer.model.VendorServicesDTO;
import com.groomer.vendordetails.adapter.ServicesAdapter;

import java.util.ArrayList;
import java.util.List;

public class ServicesFragment extends BaseFragment {
    private RecyclerView mServicesList;
    private View view;
    private Activity mActivity;

    public static ServicesFragment newInstance() {
        ServicesFragment fragment = new ServicesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this.getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_services, container, false);
        setUpRecycler();
        return view;
    }

    private void setUpRecycler() {
        mServicesList = (RecyclerView) view.findViewById(R.id.vendor_services_list);
        mServicesList.setLayoutManager(new LinearLayoutManager(mActivity));
        mServicesList.setAdapter(new ServicesAdapter(mActivity, getServicesList()));
    }

    private List<VendorServicesDTO> getServicesList() {
        List<VendorServicesDTO> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            VendorServicesDTO servicesDTO = new VendorServicesDTO();
            servicesDTO.setServiceName("Hair Cut");
            servicesDTO.setServicePrice("SAR 65");
            servicesDTO.setServiceTime("30 MIN");
            list.add(servicesDTO);
        }
        return list;
    }
}
