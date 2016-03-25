package com.groomer.alert;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.groomer.R;
import com.groomer.alert.adapter.AlertAdapter;
import com.groomer.fragments.BaseFragment;


public class AlertFragment extends BaseFragment {


    private View view;
    private Context mActivity;

    public AlertFragment() {
        // Required empty public constructor
    }


    public static AlertFragment newInstance() {
        AlertFragment fragment = new AlertFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        view = inflater.inflate(R.layout.fragment_alert, container, false);

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        RecyclerView alertRecyclerView = (RecyclerView) view.findViewById(R.id.recycle_alert);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        alertRecyclerView.setLayoutManager(llm);

        AlertAdapter alertAdapter = new AlertAdapter(mActivity);
        alertRecyclerView.setAdapter(alertAdapter);

    }
}
