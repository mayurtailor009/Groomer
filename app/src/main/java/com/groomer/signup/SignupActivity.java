package com.groomer.signup;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.groomer.GroomerApplication;
import com.groomer.R;
import com.groomer.activity.BaseActivity;
import com.groomer.gps.GPSTracker;
import com.groomer.home.HomeActivity;
import com.groomer.login.LoginActivity;
import com.groomer.model.UserDTO;
import com.groomer.utillity.Constants;
import com.groomer.utillity.GroomerPreference;
import com.groomer.utillity.Utils;
import com.groomer.volley.CustomJsonRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends BaseActivity {

    private Activity mActivity;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mActivity=this;

        GPSTracker gpstracker= new GPSTracker(mActivity);
        init();
    }

    private void init() {
        setTouchNClick(R.id.btn_signup);
        setClick(R.id.tv_signin);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_signup:
                performSignUp();
                break;
            case R.id.tv_signin:
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
        }
    }

    public void performSignUp() {

        Utils.hideKeyboard(SignupActivity.this);
        if (Utils.isOnline(SignupActivity.this)) {
            if (validateForm()) {
                Map<String, String> params = new HashMap<>();
                params.put("action", Constants.SIGN_UP_METHOD);
                params.put("name", getEditTextText(R.id.et_name));
                params.put("email", getEditTextText(R.id.et_emailid));
                params.put("password", getEditTextText(R.id.et_passowrd));
                params.put("mobile", getEditTextText(R.id.et_phone));
                params.put("mobie_countrycode", "+91");
                params.put("gender", "M");
                params.put("dob", "08/21/1989");
                params.put("confirm_password", getEditTextText(R.id.et_passowrd));
                params.put("device_id", GroomerPreference.getPushRegistrationId(mActivity));
                params.put("lat", GroomerPreference.getLatitude(mActivity)+"");
                params.put("lng", GroomerPreference.getLongitude(mActivity)+"");
                params.put("image", "");
                params.put("device", "android");

                final ProgressDialog pdialog = Utils.createProgressDialog(this, null, false);
                CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, Constants.SERVICE_URL, params,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Utils.ShowLog(Constants.TAG, "Response -> " + response.toString());
                                pdialog.dismiss();
                                try {
                                    if (Utils.getWebServiceStatusByInt(response) == 1) {

                                        UserDTO userDTO = new Gson().fromJson(response.getJSONObject("user").toString(), UserDTO.class);
                                        GroomerPreference.putObjectIntoPref(SignupActivity.this, userDTO, Constants.USER_INFO);
                                        Intent intent = new Intent(SignupActivity.this, HomeActivity.class);
                                        intent.putExtra("fragmentNumber", 0);
                                        startActivity(intent);

                                        finish();
                                    } else {
                                        Utils.showDialog(SignupActivity.this, "Error", Utils.getWebServiceMessage(response));
                                    }


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pdialog.dismiss();
                        Utils.showExceptionDialog(SignupActivity.this);
                    }
                });
                pdialog.show();
                GroomerApplication.getInstance().getRequestQueue().add(postReq);
            }
        } else {
            Utils.showNoNetworkDialog(SignupActivity.this);
        }


    }


    public boolean validateForm() {
        if (getEditTextText(R.id.et_name).equals("")) {
            Utils.showDialog(this, "Message", "Please enter name");
            return false;
        } else if (getEditTextText(R.id.et_phone).equals("")) {
            Utils.showDialog(this, "Message", "Please enter phone no");
            return false;
        }
        else if (getEditTextText(R.id.et_emailid).equals("")) {
            Utils.showDialog(this, "Message", "Please enter emailid");
            return false;
        } else if (getEditTextText(R.id.et_passowrd).equals("")) {
            Utils.showDialog(this, "Message", "Please enter password");
            return false;
        }
        return true;
    }
}
