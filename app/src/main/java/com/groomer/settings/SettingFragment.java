package com.groomer.settings;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.groomer.R;
import com.groomer.fragments.BaseFragment;

public class SettingFragment extends BaseFragment {

    public SettingFragment() {
        // Required empty public constructor
    }


    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();

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
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }




}
