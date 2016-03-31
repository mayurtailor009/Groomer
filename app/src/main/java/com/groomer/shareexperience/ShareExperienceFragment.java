package com.groomer.shareexperience;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.groomer.R;
import com.groomer.fragments.BaseFragment;

public class ShareExperienceFragment extends BaseFragment {

    private View view;

    public static ShareExperienceFragment newInstance() {
        ShareExperienceFragment fragment = new ShareExperienceFragment();
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
        view = inflater.inflate(R.layout.fragment_share_experience, container, false);
        return view;
    }
}
