package com.groomer.settings;


import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;

import com.groomer.R;
import com.groomer.camera.CameraChooseDialogFragment;
import com.groomer.camera.CameraSelectInterface;
import com.groomer.camera.GallerySelectInterface;
import com.groomer.changepassword.ChangePasswordActivity;
import com.groomer.customviews.alert.CustomAlert;
import com.groomer.fragments.BaseFragment;
import com.groomer.home.HomeActivity;
import com.groomer.model.UserDTO;
import com.groomer.settings.editprofile.EditProfile;
import com.groomer.utillity.Constants;
import com.groomer.utillity.GroomerPreference;
import com.groomer.utillity.HelpMe;
import com.groomer.utillity.SessionManager;
import com.groomer.utillity.Theme;
import com.groomer.utillity.Utils;

import java.io.File;

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

        selectedButton(GroomerPreference.getAPP_LANG(getActivity().getApplicationContext()));

        init();
        //selectedButton(GroomerPreference.getAPP_LANG(getActivity().getApplicationContext()));
    }

    private void init() {
        setViewsColor();
        //settingAllProfileValues();

        setClick(R.id.view_blue, view);
        setClick(R.id.view_red, view);
        setClick(R.id.view_green, view);
        setClick(R.id.btn_select_english, view);
        setClick(R.id.btn_select_arabic, view);
        //setClick(R.id.btn_save, view);
        setClick(R.id.btn_change_password, view);
        setClick(R.id.btn_edit_profile, view);
        //setClick(R.id.btn_signout, view);

    }

    private void setViewsColor() {
        View greenView = view.findViewById(R.id.view_green);
        View blueView = view.findViewById(R.id.view_blue);
        View redView = view.findViewById(R.id.view_red);
        GradientDrawable greenDrawable = (GradientDrawable) greenView.getBackground();
        GradientDrawable blueDrawable = (GradientDrawable) blueView.getBackground();
        GradientDrawable redDrawale = (GradientDrawable) redView.getBackground();

        greenDrawable.setColor(Color.parseColor("#558A2E"));
        blueDrawable.setColor(Color.parseColor("#1156D5"));
        redDrawale.setColor(Color.parseColor("#A52A2A"));
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
                startHomeActivity();
                break;
            case R.id.view_green:
                theme = Theme.Green;
                Utils.putObjectIntoPref(getContext(), theme, Constants.CURRENT_THEME);
                getActivity().finish();
                startHomeActivity();
                break;
            case R.id.view_red:
                theme = Theme.Red;
                Utils.putObjectIntoPref(getContext(), theme, Constants.CURRENT_THEME);
                getActivity().finish();
                startHomeActivity();
                break;
            case R.id.btn_select_english:
                changeLanguageToEnglish();
                break;
            case R.id.btn_select_arabic:
                changeLanguageToArabic();
                break;
            case R.id.btn_change_password:
                Intent intent = new Intent(mActivity, ChangePasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_edit_profile:
                Intent edit_profile = new Intent(mActivity, EditProfile.class);
                startActivity(edit_profile);
                break;
//            case R.id.btn_signout:
//                logoutFromApp();
//                break;
        }
    }

    /**
     * this method takes out of the application.
     */
    private void logoutFromApp() {
        new CustomAlert(mActivity)
                .doubleButtonAlertDialog(
                        getString(R.string.you_logout),
                        getString(R.string.ok_button),
                        getString(R.string.canceled), "dblBtnCallbackResponse", 1000);
    }

    /**
     * callback method of double button alert box.
     *
     * @param flag true if Ok button pressed otherwise false.
     * @param code is requestCode.
     */
    public void dblBtnCallbackResponse(Boolean flag, int code) {
        if (flag) {
            SessionManager.logoutUser(mActivity);
        }

    }

    private void changeLanguageToArabic() {

        if (GroomerPreference.getAPP_LANG(getActivity().getApplicationContext()).contains(Constants.LANG_ENGLISH_CODE)) {
            HelpMe.setLocale(Constants.LANG_ARABIC_CODE, getActivity().getApplicationContext());
            selectedButton(Constants.LANG_ARABIC_CODE);
            GroomerPreference.setAPP_LANG(getActivity().getApplicationContext(), Constants.LANG_ARABIC_CODE);
            Intent intent = new Intent(mActivity, HomeActivity.class);
            intent.putExtra("fragmentNumber", 4);
            startActivity(intent);
        }

    }

    private void changeLanguageToEnglish() {

        if (GroomerPreference.getAPP_LANG(getActivity().getApplicationContext()).contains(Constants.LANG_ARABIC_CODE)) {
            HelpMe.setLocale(Constants.LANG_ENGLISH_CODE, getActivity().getApplicationContext());
            selectedButton(Constants.LANG_ENGLISH_CODE);
            GroomerPreference.setAPP_LANG(getActivity().getApplicationContext(), Constants.LANG_ENGLISH_CODE);
            Intent intent = new Intent(mActivity, HomeActivity.class);
            intent.putExtra("fragmentNumber", 4);
            startActivity(intent);
        }
    }


    private void selectedButton(String STATUS_CODE) {

        /*Button btn_select_english = (Button) view.findViewById(R.id.btn_select_english);
        Button btn_select_arabic = (Button) view.findViewById(R.id.btn_select_arabic);
        Theme theme = Utils.getObjectFromPref(mActivity, Constants.CURRENT_THEME);

        if (STATUS_CODE.contains(Constants.LANG_ENGLISH_CODE)) {

            if (theme.equals(Theme.Blue)) {
                btn_select_english.setBackgroundColor(getResources().getColor(R.color.blue_light));
            } else if (theme.equals(Theme.Red)) {
                btn_select_english.setBackgroundColor(getResources().getColor(R.color.red));
            } else {
                btn_select_english.setBackgroundColor(getResources().getColor(R.color.green));
            }
            btn_select_arabic.setBackgroundColor(getResources().getColor(R.color.colorWhite));

            btn_select_english.setTextColor(getResources().getColor(R.color.colorWhite));
            btn_select_arabic.setTextColor(getResources().getColor(R.color.black));


        } else {

            if (theme.equals(Theme.Blue)) {
                btn_select_arabic.setBackgroundColor(getResources().getColor(R.color.blue_light));
            } else if (theme.equals(Theme.Red)) {
                btn_select_arabic.setBackgroundColor(getResources().getColor(R.color.red));
            } else {
                btn_select_arabic.setBackgroundColor(getResources().getColor(R.color.green));
            }


            btn_select_english.setBackgroundColor(getResources().getColor(R.color.colorWhite));

            btn_select_arabic.setTextColor(getResources().getColor(R.color.colorWhite));
            btn_select_english.setTextColor(getResources().getColor(R.color.black));
        }*/

        Button btn_select_english = (Button) view.findViewById(R.id.btn_select_english);
        Button btn_select_arabic = (Button) view.findViewById(R.id.btn_select_arabic);


        Theme theme = Utils.getObjectFromPref(mActivity, Constants.CURRENT_THEME);


        if (STATUS_CODE.contains(Constants.LANG_ENGLISH_CODE)) {

            btn_select_english.setBackgroundColor(getResources().getColor(R.color.theme_green));
            btn_select_arabic.setBackgroundColor(getResources().getColor(R.color.colorWhite));

            btn_select_english.setTextColor(getResources().getColor(R.color.colorWhite));
            btn_select_arabic.setTextColor(getResources().getColor(R.color.black));

            if (theme.equals(Theme.Blue)) {
                btn_select_english.setBackgroundColor(getResources().getColor(R.color.theme_blue));
            } else if (theme.equals(Theme.Red)) {
                btn_select_english.setBackgroundColor(getResources().getColor(R.color.theme_red));
            } else {
                btn_select_english.setBackgroundColor(getResources().getColor(R.color.theme_green));
            }

        } else {

            btn_select_arabic.setBackgroundColor(getResources().getColor(R.color.theme_green));
            btn_select_english.setBackgroundColor(getResources().getColor(R.color.colorWhite));

            btn_select_arabic.setTextColor(getResources().getColor(R.color.colorWhite));
            btn_select_english.setTextColor(getResources().getColor(R.color.black));

            if (theme.equals(Theme.Blue)) {
                btn_select_arabic.setBackgroundColor(getResources().getColor(R.color.theme_blue));
            } else if (theme.equals(Theme.Red)) {
                btn_select_arabic.setBackgroundColor(getResources().getColor(R.color.theme_red));
            } else {
                btn_select_arabic.setBackgroundColor(getResources().getColor(R.color.theme_green));
            }
        }
    }

    public void startHomeActivity() {
        Intent intent = new Intent(mActivity, HomeActivity.class);
        intent.putExtra("fragmentNumber", 4);
        startActivity(intent);
    }
}
