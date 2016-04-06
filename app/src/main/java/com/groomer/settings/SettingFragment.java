package com.groomer.settings;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.groomer.R;
import com.groomer.fragments.BaseFragment;
import com.groomer.home.HomeActivity;
import com.groomer.utillity.Constants;
import com.groomer.utillity.GroomerPreference;
import com.groomer.utillity.HelpMe;
import com.groomer.utillity.Theme;
import com.groomer.utillity.Utils;

public class SettingFragment extends BaseFragment {

    private View view;
    private Context mActivity;

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
        mActivity = getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_setting, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        //selectedButton(GroomerPreference.getAPP_LANG(getActivity().getApplicationContext()));
    }

    private void init() {
        setClick(R.id.view_blue, view);
        setClick(R.id.view_red, view);
        setClick(R.id.view_green, view);
        setClick(R.id.btn_select_english, view);
        setClick(R.id.btn_select_arabic, view);

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
        switch (arg0.getId()) {
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
            case R.id.btn_select_english:
                changeLanguageToEnglish();
                break;
            case R.id.btn_select_arabic:
                changeLanguageToArabic();
                break;
        }
    }

    private void changeLanguageToArabic() {

        if (GroomerPreference.getAPP_LANG(getActivity().getApplicationContext()).contains(Constants.LANG_ENGLISH_CODE)) {
            HelpMe.setLocale(Constants.LANG_ARABIC_CODE, getActivity().getApplicationContext());
            selectedButton(Constants.LANG_ARABIC_CODE);
            GroomerPreference.setAPP_LANG(getActivity().getApplicationContext(), Constants.LANG_ARABIC_CODE);
        }

    }

    private void changeLanguageToEnglish() {

        if (GroomerPreference.getAPP_LANG(getActivity().getApplicationContext()).contains(Constants.LANG_ARABIC_CODE)) {
            HelpMe.setLocale(Constants.LANG_ENGLISH_CODE, getActivity().getApplicationContext());
            selectedButton(Constants.LANG_ENGLISH_CODE);
            GroomerPreference.setAPP_LANG(getActivity().getApplicationContext(), Constants.LANG_ENGLISH_CODE);
        }
    }


    private void selectedButton(String STATUS_CODE) {

        Button btn_select_english = (Button) view.findViewById(R.id.btn_select_english);
        Button btn_select_arabic = (Button) view.findViewById(R.id.btn_select_arabic);

        if (STATUS_CODE.contains(Constants.LANG_ENGLISH_CODE)) {

            btn_select_english.setBackgroundColor(getResources().getColor(R.color.green));
            btn_select_arabic.setBackgroundColor(getResources().getColor(R.color.colorWhite));

            btn_select_english.setTextColor(getResources().getColor(R.color.colorWhite));
            btn_select_arabic.setTextColor(getResources().getColor(R.color.black));


        } else {

            btn_select_arabic.setBackgroundColor(getResources().getColor(R.color.green));
            btn_select_english.setBackgroundColor(getResources().getColor(R.color.colorWhite));

            btn_select_arabic.setTextColor(getResources().getColor(R.color.colorWhite));
            btn_select_english.setTextColor(getResources().getColor(R.color.black));
        }

        Intent intent = new Intent(mActivity,HomeActivity.class);
        intent.putExtra("fragmentNumber",4);
        startActivity(intent);
    }


}
