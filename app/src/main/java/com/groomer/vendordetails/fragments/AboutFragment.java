package com.groomer.vendordetails.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.groomer.R;
import com.groomer.fragments.BaseFragment;


public class AboutFragment extends BaseFragment {

    private View view;
    private Context mActivity;

    public AboutFragment() {
        // Required empty public constructor
    }


    public static AboutFragment newInstance(String description) {
        AboutFragment fragment = new AboutFragment();
        Bundle b = new Bundle();
        b.putString("description", description);
        fragment.setArguments(b);
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


        view = inflater.inflate(R.layout.fragment_about, container, false);


        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String storeDesc = getArguments().getString("description");
        ((TextView) view.findViewById(R.id.txt_about_text)).setText(storeDesc);

    }
}
