package com.groomer.appointment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.groomer.R;
import com.groomer.appointment.adapter.AppointmentListAdapter;
import com.groomer.fragments.BaseFragment;
import com.groomer.model.AppointmentDTO;
import com.groomer.model.ServiceDTO;
import com.groomer.model.VendorServicesDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class AppointmentFragment extends BaseFragment {

    private View view;
    private ExpandableListView mExpandableListView;
    private Activity mActivity;

    public static AppointmentFragment newInstance() {
        AppointmentFragment fragment = new AppointmentFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_appointment, container, false);
        mActivity = AppointmentFragment.this.getActivity();

        setUpExpandableListVIew();
        return view;
    }

    private void setUpExpandableListVIew() {
        mExpandableListView = (ExpandableListView) view.findViewById(R.id.appointment_list);
        mExpandableListView.setChoiceMode(ExpandableListView.CHOICE_MODE_SINGLE);
        mExpandableListView.setAdapter(new AppointmentListAdapter(
                        this.getActivity(), getGroupList(), getChilderList())
        );
    }

    private List<AppointmentDTO> getGroupList() {
        List<AppointmentDTO> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            AppointmentDTO appointmentDTO = new AppointmentDTO();
            appointmentDTO.setmUserName("Dave Dud");
            appointmentDTO.setmUserAddress(mActivity.getString(R.string.txt_vendor_address));
            appointmentDTO.setmUserTime("11:00 PM");
            list.add(appointmentDTO);
        }
        return list;
    }


    private HashMap<Integer, List<ServiceDTO>> getChilderList() {
        HashMap<Integer, List<ServiceDTO>> hashMap = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            List<ServiceDTO> list = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                ServiceDTO servicesDTO = new ServiceDTO();
                servicesDTO.setName_eng(mActivity.getResources().getString(R.string.menu_services)
                        + " " + (j + 1));
                servicesDTO.setPrice("65");
                list.add(servicesDTO);
            }
            hashMap.put(i, list);
        }
        return hashMap;
    }
}
