package com.groomer.appointment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.groomer.R;
import com.groomer.appointment.adapter.AppointmentListAdapter;
import com.groomer.fragments.BaseFragment;
import com.groomer.model.AppointmentDTO;
import com.groomer.model.VendorServicesDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class AppointmentFragment extends BaseFragment {

    private View view;
    private ExpandableListView mExpandableListView;

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
            appointmentDTO.setmUserAddress("7th Ave, Apt 12, NYC");
            appointmentDTO.setmUserTime("11:00 PM");
            list.add(appointmentDTO);
        }
        return list;
    }


    private HashMap<Integer, List<VendorServicesDTO>> getChilderList() {
        HashMap<Integer, List<VendorServicesDTO>> hashMap = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            List<VendorServicesDTO> list = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                VendorServicesDTO servicesDTO = new VendorServicesDTO();
                servicesDTO.setServiceName("service " + (j + 1));
                servicesDTO.setServicePrice("SAR 65");
                servicesDTO.setServiceTime("30 MIN");
                list.add(servicesDTO);
            }
            hashMap.put(i, list);
        }
        return hashMap;
    }
}
