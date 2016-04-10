package com.groomer.settings;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.groomer.GroomerApplication;
import com.groomer.R;
import com.groomer.camera.CameraChooseDialogFragment;
import com.groomer.camera.CameraSelectInterface;
import com.groomer.camera.GallerySelectInterface;
import com.groomer.changepassword.ChangePasswordActivity;
import com.groomer.fragments.BaseFragment;
import com.groomer.home.HomeActivity;
import com.groomer.model.UserDTO;
import com.groomer.settings.adapter.CountryCodeAdapter;
import com.groomer.utillity.Constants;
import com.groomer.utillity.GroomerPreference;
import com.groomer.utillity.HelpMe;
import com.groomer.utillity.Theme;
import com.groomer.utillity.Utils;
import com.groomer.volley.CustomJsonImageRequest;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettingFragment extends BaseFragment {

    private static final String TAG = "SettingFragment";
    private View view;
    private Context mActivity;
    private int CAMERA_REQUEST = 1001;
    private int GALLERY_REQUEST = 1002;
    private CameraChooseDialogFragment dFragment;
    private DisplayImageOptions options;
    private ImageView ivProfile;
    private byte[] bitmapdata;
    private File file = null;
    private List<Map<String, String>> countryCodeList;

    private Dialog dialogCountryCode;
    private Switch swh_location;


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

        options = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new SimpleBitmapDisplayer())
                .showImageOnLoading(R.drawable.avater)
                .showImageOnFail(R.drawable.avater)
                .showImageForEmptyUri(R.drawable.avater)
                .build();


        ivProfile = (ImageView) view.findViewById(R.id.img_user_image);
        ivProfile.setOnClickListener(addImageClick);

        swh_location = (Switch) view.findViewById(R.id.swh_location);

        countryCodeList = getCountryCode();
        init();
        //selectedButton(GroomerPreference.getAPP_LANG(getActivity().getApplicationContext()));
    }

    private void init() {
        settingAllProfileValues();

        setClick(R.id.view_blue, view);
        setClick(R.id.view_red, view);
        setClick(R.id.view_green, view);
        setClick(R.id.btn_select_english, view);
        setClick(R.id.btn_select_arabic, view);
        setClick(R.id.edt_dob, view);
        setClick(R.id.edt_gender, view);
        setClick(R.id.btn_save, view);
        setClick(R.id.txt_country_code, view);

    }

    private void settingAllProfileValues() {
        UserDTO userDTO = GroomerPreference.getObjectFromPref(mActivity, Constants.USER_INFO);

        setViewText(R.id.et_name, userDTO.getName_eng(), view);
        setViewText(R.id.et_emailid, userDTO.getEmail(), view);
        setViewText(R.id.et_mobile_no, userDTO.getMobile(), view);
        setViewText(R.id.edt_dob, userDTO.getDob(), view);
        setViewText(R.id.edt_gender, userDTO.getGender(), view);
        if (userDTO.getIs_location_service() != null) {
            swh_location.setChecked(userDTO.getIs_location_service().equals("1") ? true : false);
        }
        if (userDTO.getCountry_code() != null) {
            setViewText(R.id.txt_country_code, "+" + userDTO.getCountry_code(), view);
        }
        ImageLoader.getInstance().displayImage(userDTO.getImage(), ivProfile, options);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_settings, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_lock:
                Intent intent = new Intent(mActivity, ChangePasswordActivity.class);
                startActivity(intent);
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
            case R.id.edt_dob:
                showCalendarDialog();
                break;
            case R.id.edt_gender:
                showSexDialog();
                break;

            case R.id.txt_country_code:
                openDialogForCountry();
                break;

            case R.id.btn_save:
                if (validateForm()) {
                    updateProfile();
                }
                break;
        }
    }

    private List<Map<String, String>> getCountryCode() {
        List<Map<String, String>> list = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(Utils.loadJSONFromAsset(getActivity()));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                Map<String, String> map = new HashMap<>();
                map.put("name", jsonObject.getString("name"));
                map.put("dial_code", jsonObject.getString("dial_code"));
                list.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }


    private void openDialogForCountry() {
        dialogCountryCode = new Dialog(getActivity());
        dialogCountryCode.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogCountryCode.setContentView(R.layout.layout_country_code);
        getActivity().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        ListView listView = (ListView) dialogCountryCode.findViewById(R.id.list);
        CountryCodeAdapter adapter = new CountryCodeAdapter(getActivity(), countryCodeList);
        listView.setAdapter(adapter);
        dialogCountryCode.show();

        listView.setOnItemClickListener(mobCountryCodeClickListener);

    }


    AdapterView.OnItemClickListener mobCountryCodeClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view1, int i, long l) {
            setViewText(R.id.txt_country_code, countryCodeList.get(i).get("dial_code"), view);
            dialogCountryCode.dismiss();
        }
    };


    public void showCalendarDialog() {

        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        // Launch Date Picker Dialog
        DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view1, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // Display Selected date in textbox
                        setViewText(R.id.edt_dob, dayOfMonth + "-" + (monthOfYear + 1) + "-" + year, view);

                    }
                }, mYear, mMonth, mDay);
        dpd.show();
    }


    public void showSexDialog() {
        final CharSequence[] items = {"Male", "Female"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose Gender");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection
                setViewText(R.id.edt_gender, items[item].toString(), view);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }


    public View.OnClickListener getAddImageClick() {
        return addImageClick;
    }


    View.OnClickListener addImageClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showAlertCamera();
        }
    };

    private void showAlertCamera() {
        try {
            if (dFragment == null) {
                dFragment = new CameraChooseDialogFragment();
            }
            dFragment.setCallBack(cameraSelectInterface, gallerySelectInterface);
            // Show DialogFragment
            FragmentManager fm = getActivity().getSupportFragmentManager();
            dFragment.show(fm, "Dialog Fragment");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    CameraSelectInterface cameraSelectInterface = new CameraSelectInterface() {
        @Override
        public void startCamera() {
            clickPictureUsingCamera();
        }
    };

    GallerySelectInterface gallerySelectInterface = new GallerySelectInterface() {
        @Override
        public void startGallery() {
            selectImageFromGallery();
        }
    };


    /**
     * This method is used to click image using camera and set the clicked image
     * in round image view.
     */
    private void clickPictureUsingCamera() {
        try {
            Intent cameraIntent = new Intent(
                    MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void selectImageFromGallery() {
        try {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(
                    Intent.createChooser(intent, "Select Picture"), GALLERY_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            byte[] hash = null;
            if (requestCode == GALLERY_REQUEST && resultCode == Activity.RESULT_OK
                    && null != data) {
                if (dFragment != null) {
                    dFragment.dismiss();
                    dFragment = null;
                }
                Uri selectedImage = data.getData();
                Utils.ShowLog("DATA", data.toString());
                Utils.ShowLog("uri ", selectedImage.toString());

                // setting image in image in profile pic.
//                image_loader
//                        .displayImage(selectedImage.toString(), img_profile);
//
//                Bitmap bitmap = ((BitmapDrawable) img_profile.getDrawable())
//                        .getBitmap();
                Bitmap bitmap = null;

                try {
                    bitmap = MediaStore.Images.Media.getBitmap(mActivity.getContentResolver(), selectedImage);
                    ivProfile.setImageBitmap(bitmap);

                    file = new File(getActivity().getCacheDir(), "profile.png");
                    file.createNewFile();

//Convert bitmap to byte array
                    Bitmap bitmap1 = bitmap;
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap1.compress(Bitmap.CompressFormat.PNG, 70 /*ignored for PNG*/, bos);
                    bitmapdata = bos.toByteArray();

//write the bytes in file
                    FileOutputStream fos = new FileOutputStream(file);
                    fos.write(bitmapdata);
                    fos.flush();
                    fos.close();


                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


                // Converting image's bitmap to byte array.
                // ByteArrayOutputStream bos = new ByteArrayOutputStream();
                //   bitmap.compress(Bitmap.CompressFormat.JPEG, 30, bos);
                //hash = bos.toByteArray();

                // converting image's byte array to Base64encoded string
                // imageStringBase64 = Base64.encodeToString(hash, Base64.NO_WRAP);

            } else if (requestCode == CAMERA_REQUEST
                    && resultCode == Activity.RESULT_OK) {
                if (dFragment != null) {
                    dFragment.dismiss();
                    dFragment = null;
                }
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                ivProfile.setImageBitmap(photo);

                file = new File(getActivity().getCacheDir(), "profile.png");
                file.createNewFile();

//Convert bitmap to byte array
                Bitmap bitmap1 = photo;
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap1.compress(Bitmap.CompressFormat.PNG, 70 /*ignored for PNG*/, bos);
                bitmapdata = bos.toByteArray();

//write the bytes in file
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(bitmapdata);
                fos.flush();
                fos.close();


                // Converting image's bitmap to byte array.
                //ByteArrayOutputStream bos = new ByteArrayOutputStream();
                //photo.compress(Bitmap.CompressFormat.JPEG, 30, bos);
                //hash = bos.toByteArray();

                // converting image's byte array to Base64encoded string
                // imageStringBase64 = Base64.encodeToString(hash, Base64.NO_WRAP);

            }
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();

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

        Intent intent = new Intent(mActivity, HomeActivity.class);
        intent.putExtra("fragmentNumber", 4);
        startActivity(intent);
    }


    private void updateProfile() {
        if (Utils.isOnline(getActivity())) {

            Map<String, String> params = new HashMap<>();
            params.put("action", Constants.EDIT_PROFILE_METHOD);
            params.put("user_id", Utils.getUserId(getActivity()));
            params.put("name", getViewText(R.id.et_name, view));
            params.put("dob", getViewText(R.id.edt_dob, view));
            params.put("gender", getViewText(R.id.edt_gender, view).equals("Male") ? "M" : "F");
            params.put("country_code", getViewText(R.id.txt_country_code, view));
            params.put("is_location_service", swh_location.isChecked() ? "1" : "0");
            params.put("mobile", getViewText(R.id.et_mobile_no, view));
            params.put("location", getViewText(R.id.et_mobile_no, view));
            final ProgressDialog pdialog = Utils.createProgressDialog(mActivity, null, false);

            CustomJsonImageRequest postReq = new CustomJsonImageRequest(Request.Method.POST,
                    Constants.SERVICE_URL, params, file,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Utils.ShowLog(TAG, "Response -> " + response.toString());
                            pdialog.dismiss();

                            try {
                                if (Utils.getWebServiceStatus(response)) {

                                    Toast.makeText(mActivity, "Success", Toast.LENGTH_SHORT).show();

                                    UserDTO userDTO = new Gson().fromJson(response.getJSONObject("user").toString(), UserDTO.class);
                                    GroomerPreference.putObjectIntoPref(getActivity(),
                                            userDTO, Constants.USER_INFO);
                                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                                    intent.putExtra("fragmentNumber", 4);
                                    startActivity(intent);
                                } else {
                                    Utils.customDialog(Utils.getWebServiceMessage(response), getActivity());
                                }
                            } catch (Exception e) {
                                pdialog.dismiss();
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    pdialog.dismiss();

                    Utils.showExceptionDialog(getActivity());
                }
            });
            pdialog.show();
            GroomerApplication.getInstance().getRequestQueue().add(postReq);
            postReq.setRetryPolicy(new DefaultRetryPolicy(
                    30000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        } else {
            Utils.showNoNetworkDialog(getActivity());
        }
    }

    public void startHomeActivity() {
        Intent intent = new Intent(mActivity, HomeActivity.class);
        intent.putExtra("fragmentNumber", 4);
        startActivity(intent);
    }


    public boolean validateForm() {

        if (getViewText(R.id.et_name, view).equals("")) {
            Utils.showDialog(mActivity, "Message", "Please enter name.");
            return false;
        } else if (getViewText(R.id.et_emailid, view).equals("")) {
            Utils.showDialog(mActivity, "Message", "Please enter email id.");
            return false;
        } else if (getViewText(R.id.txt_country_code, view).equals("")) {
            Utils.showDialog(mActivity, "Message", "Please select country code.");
            return false;
        } else if (getViewText(R.id.et_mobile_no, view).equals("")) {
            Utils.showDialog(mActivity, "Message", "Please enter mobile number.");
            return false;
        } else if (getViewText(R.id.edt_dob, view).equals("")) {
            Utils.showDialog(mActivity, "Message", "Please select dob.");
            return false;
        } else if (getViewText(R.id.edt_gender, view).equals("")) {
            Utils.showDialog(mActivity, "Message", "Please select gender.");
            return false;
        }
        return true;
    }
}
