package com.groomer.settings.editprofile;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
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
import com.groomer.activity.BaseActivity;
import com.groomer.camera.CameraChooseDialogFragment;
import com.groomer.camera.CameraSelectInterface;
import com.groomer.camera.GallerySelectInterface;
import com.groomer.home.HomeActivity;
import com.groomer.model.UserDTO;
import com.groomer.settings.adapter.CountryCodeAdapter;
import com.groomer.utillity.Constants;
import com.groomer.utillity.GroomerPreference;
import com.groomer.utillity.Utils;
import com.groomer.volley.CustomJsonImageRequest;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditProfile extends BaseActivity {

    private Activity mActivity;
    private CameraChooseDialogFragment dFragment;
    private DisplayImageOptions options;
    private ImageView ivProfile;
    private byte[] bitmapdata;
    private File file = null;
    private int CAMERA_REQUEST = 1001;
    private int GALLERY_REQUEST = 1002;
    private String TAG = EditProfile.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme();
        setContentView(R.layout.activity_edit_profile);
        mActivity = EditProfile.this;

        setHeader(getString(R.string.txt_edit_profile));
        setLeftClick(R.drawable.back_btn, true);

        options = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(true)
                .cacheOnDisk(false)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new SimpleBitmapDisplayer())
                .showImageOnLoading(R.drawable.avater)
                .showImageOnFail(R.drawable.avater)
                .showImageForEmptyUri(R.drawable.avater)
                .build();

        ivProfile = (ImageView) findViewById(R.id.img_user_image);
        ivProfile.setOnClickListener(addImageClick);

        settingAllProfileValues();

        //setting click event.
        setClick(R.id.edt_dob);
        setClick(R.id.edt_gender);
        setClick(R.id.btn_save);
        setClick(R.id.txt_country_code);
    }


    private void settingAllProfileValues() {
        UserDTO userDTO = GroomerPreference.getObjectFromPref(mActivity, Constants.USER_INFO);
        if (userDTO != null) {
            setViewText(R.id.et_name, userDTO.getName_eng());
            if (userDTO.getEmail() != null) {
                setViewText(R.id.et_emailid, userDTO.getEmail());
            }

            if (userDTO.getMobile() != null) {
                setViewText(R.id.et_mobile_no, userDTO.getMobile());
            }
            if (userDTO.getDob() != null) {
                setViewText(R.id.edt_dob, userDTO.getDob());

            }

            if (userDTO.getGender() != null) {
                setViewText(R.id.edt_gender, userDTO.getGender().equalsIgnoreCase("M") ?
                        getString(R.string.txt_male) : getString(R.string.txt_female));
            }

            if (userDTO.getIs_location_service() != null) {
                Switch swh_location = (Switch) findViewById(R.id.swh_location);
                swh_location.setChecked(userDTO.getIs_location_service().equals("1"));
            }

            if (userDTO.getCountry_code() != null) {
                setViewText(R.id.txt_country_code, "+" + userDTO.getCountry_code());
            }

            ImageLoader.getInstance().displayImage(userDTO.getImage(), ivProfile, options);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edt_dob:
                showCalendarDialog();
                break;
            case R.id.edt_gender:
                showSexDialog();
                break;
            case R.id.txt_country_code:
                openCountryCodeDialog();
                break;
            case R.id.btn_save:
                if (validateForm()) {
                    updateProfile();
                }
                break;
            case R.id.hamburgur_img_icon:
                this.onBackPressed();
                break;
        }
    }

    private void updateProfile() {
        boolean locationChecked = ((Switch) findViewById(R.id.swh_location)).isChecked();
        if (Utils.isOnline(mActivity)) {

            Map<String, String> params = new HashMap<>();
            params.put("action", Constants.EDIT_PROFILE_METHOD);
            params.put("user_id", Utils.getUserId(mActivity));
            params.put("name", getViewText(R.id.et_name));
            params.put("dob", getViewText(R.id.edt_dob));
            params.put("gender", getViewText(R.id.edt_gender).equals("Male") ? "M" : "F");
            params.put("country_code", getViewText(R.id.txt_country_code).contains("+")
                    ? getViewText(R.id.txt_country_code).replace("+", "") : getViewText(R.id.txt_country_code));
            params.put("is_location_service", locationChecked ? "1" : "0");
            params.put("mobile", getViewText(R.id.et_mobile_no));
            params.put("location", getViewText(R.id.et_mobile_no));
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

                                    Toast.makeText(mActivity, "Profile Successfully Updated", Toast.LENGTH_SHORT).show();

                                    UserDTO userDTO = new Gson().fromJson(response.getJSONObject("user").
                                            toString(), UserDTO.class);
                                    GroomerPreference.putObjectIntoPref(mActivity,
                                            userDTO, Constants.USER_INFO);

                                    // delete cache image
                                    File imageFile = ImageLoader.getInstance().getDiscCache().
                                            get(userDTO.getImage());
                                    if (imageFile != null && imageFile.exists()) {
                                        imageFile.delete();
                                    }
                                    MemoryCacheUtils.removeFromCache(userDTO.getImage(),
                                            ImageLoader.getInstance().getMemoryCache());

                                    ImageLoader.getInstance().displayImage(userDTO.getImage(), ivProfile,
                                            options);
                                    Activity activity = mActivity;
                                    if (activity instanceof HomeActivity) {
                                        ((HomeActivity) activity).setProfilePic(userDTO);
                                    }

                                    Intent intent = new Intent(mActivity, HomeActivity.class);
                                    intent.putExtra("fragmentNumber", 4);
                                    startActivity(intent);
                                } else {
                                    Utils.customDialog(Utils.getWebServiceMessage(response),
                                            mActivity);
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

                    Utils.showExceptionDialog(mActivity);
                }
            });
            pdialog.show();
            GroomerApplication.getInstance().getRequestQueue().add(postReq);
            postReq.setRetryPolicy(new DefaultRetryPolicy(
                    30000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        } else {
            Utils.showNoNetworkDialog(mActivity);
        }
    }

    public void showCalendarDialog() {

        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        // Launch Date Picker Dialog
        DatePickerDialog dpd = new DatePickerDialog(mActivity,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view1, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // Display Selected date in textbox
                        setViewText(R.id.edt_dob, dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, mYear, mMonth, mDay);
        dpd.show();
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
            FragmentManager fm = getSupportFragmentManager();
            dFragment.show(fm, "Dialog Fragment");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void showSexDialog() {
        final CharSequence[] items = {getString(R.string.txt_male), getString(R.string.txt_female)};
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle(getString(R.string.txt_choose_gender));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection
                setViewText(R.id.edt_gender, items[item].toString());
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
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
            cameraIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION,
                    ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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

    private void openCountryCodeDialog() {
        final List<Map<String, String>> countryCodeList = Utils.getCountryCode(mActivity);
        final Dialog dialogCountryCode = new Dialog(mActivity);
        dialogCountryCode.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogCountryCode.setContentView(R.layout.layout_country_code);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        ListView listView = (ListView) dialogCountryCode.findViewById(R.id.list);
        CountryCodeAdapter adapter = new CountryCodeAdapter(mActivity, countryCodeList);
        listView.setAdapter(adapter);
        dialogCountryCode.show();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setViewText(R.id.txt_country_code,
                        countryCodeList.get(position).get("dial_code"));
                dialogCountryCode.dismiss();
            }
        });

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

                    file = new File(mActivity.getCacheDir(), "profile.png");
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
                    e.printStackTrace();
                } catch (IOException e) {
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

                file = new File(mActivity.getCacheDir(), "profile.png");
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

    public boolean validateForm() {

        if (getViewText(R.id.et_name).equals("")) {
            Utils.showDialog(mActivity, mActivity.getString(R.string.message_title),
                    mActivity.getString(R.string.enter_name));
            return false;
        } else if (getViewText(R.id.et_emailid).equals("")) {
            Utils.showDialog(mActivity, mActivity.getString(R.string.message_title),
                    mActivity.getString(R.string.alert_please_enter_emailid));
            return false;
        } else if (getViewText(R.id.et_mobile_no).equals("")) {
            Utils.showDialog(mActivity, mActivity.getString(R.string.message_title),
                    mActivity.getString(R.string.enter_mobile));
            return false;
        } else if (getViewText(R.id.edt_dob).equals("")) {
            Utils.showDialog(mActivity, mActivity.getString(R.string.message_title),
                    mActivity.getString(R.string.enter_dob));
            return false;
        } else if (getViewText(R.id.edt_gender).equals("")) {
            Utils.showDialog(mActivity, mActivity.getString(R.string.message_title),
                    mActivity.getString(R.string.enter_gender));
            return false;
        } else if (getViewText(R.id.txt_country_code).equals("")) {
            Utils.showDialog(mActivity, mActivity.getString(R.string.message_title),
                    mActivity.getString(R.string.enter_country_code));
            return false;
        }
        return true;
    }
}
