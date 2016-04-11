package com.groomer.vendordetails.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.groomer.R;
import com.groomer.fragments.BaseFragment;
import com.groomer.model.ServiceDTO;
import com.groomer.vendordetails.VendorDetailsActivity;
import com.groomer.vendordetails.adapter.ServicesAdapter;

import java.util.List;

public class ServicesFragment extends BaseFragment {
    private RecyclerView mServicesList;
    private View view;
    private Activity mActivity;
    private List<ServiceDTO> serviceDTOList;

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

        serviceDTOList = ((VendorDetailsActivity) mActivity).getServiceList();
        if (serviceDTOList != null && serviceDTOList.size() != 0) {
            mServicesList.setAdapter(new ServicesAdapter(mActivity, serviceDTOList));
        } else {
            Toast.makeText(mActivity, "List is empty.", Toast.LENGTH_SHORT).show();
        }
    }

}
