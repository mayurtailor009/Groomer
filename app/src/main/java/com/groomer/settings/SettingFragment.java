package com.groomer.settings;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.groomer.R;
import com.groomer.fragments.BaseFragment;
import com.groomer.home.HomeActivity;
import com.groomer.utillity.Constants;
import com.groomer.utillity.Theme;
import com.groomer.utillity.Utils;

public class SettingFragment extends BaseFragment {

    private View view;
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
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_setting, container, false);

        init();
        return view;
    }


    private void init(){
        setClick(R.id.view_blue, view);
        setClick(R.id.view_red, view);
        setClick(R.id.view_green, view);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_settings, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_lock:

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View arg0) {
        super.onClick(arg0);
        Theme theme;
        switch (arg0.getId()){
            case R.id.view_blue:
                theme = Theme.Blue;
                Utils.putObjectIntoPref(getContext(), theme, Constants.CURRENT_THEME);
                getActivity().finish();
                startActivity(new Intent(getContext(), HomeActivity.class));
                break;
            case R.id.view_green:
                theme = Theme.Green;
                Utils.putObjectIntoPref(getContext(), theme, Constants.CURRENT_THEME);
                getActivity().finish();
                startActivity(new Intent(getContext(), HomeActivity.class));
                break;
            case R.id.view_red:
                theme = Theme.Red;
                Utils.putObjectIntoPref(getContext(), theme, Constants.CURRENT_THEME);
                getActivity().finish();
                startActivity(new Intent(getContext(), HomeActivity.class));
                break;
        }
    }
}
