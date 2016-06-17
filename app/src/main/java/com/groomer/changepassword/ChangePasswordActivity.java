package com.groomer.changepassword;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.groomer.GroomerApplication;
import com.groomer.R;
import com.groomer.activity.BaseActivity;
import com.groomer.utillity.Constants;
import com.groomer.utillity.Theme;
import com.groomer.utillity.Utils;
import com.groomer.volley.CustomJsonRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChangePasswordActivity extends BaseActivity {

    private Activity mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);
        mActivity = this;
        init();

        Button btn_changepassword = (Button) findViewById(R.id.btn_changepassword);
        Theme theme = Utils.getObjectFromPref(mActivity, Constants.CURRENT_THEME);
        if (theme.equals(Theme.Blue)) {
            btn_changepassword.setBackgroundColor(getResources().getColor(R.color.blue_light));
        } else if (theme.equals(Theme.Red)) {
            btn_changepassword.setBackgroundColor(getResources().getColor(R.color.red));
        } else {
            btn_changepassword.setBackgroundColor(getResources().getColor(R.color.green));
        }
    }

    private void init() {
        setLeftClick(R.drawable.back_btn, true);
        setHeader(getString(R.string.change_password_header));
        setTouchNClick(R.id.btn_changepassword);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_changepassword:
                performChangePassword();
                break;

            case R.id.hamburgur_img_icon:
                this.finish();
                break;
        }
    }


    public void performChangePassword() {

        Utils.hideKeyboard(ChangePasswordActivity.this);
        if (Utils.isOnline(ChangePasswordActivity.this)) {
            if (validateForm()) {
                Map<String, String> params = new HashMap<>();
                params.put("action", Constants.CHANGE_PASSWORD_METHOD);
                params.put("user_id", Utils.getUserId(mActivity));
                params.put("password", getEditTextText(R.id.edt_new_password));
                params.put("confirm_password", getEditTextText(R.id.edt_new_password));
                params.put("current_pass", getEditTextText(R.id.edt_password));
                params.put("lang", Utils.getSelectedLanguage(mActivity));

                final ProgressDialog pdialog = Utils.createProgressDialog(this, null, false);
                CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, Constants.SERVICE_URL, params,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Utils.ShowLog(Constants.TAG, "Response -> " + response.toString());
                                pdialog.dismiss();
                                try {
                                    if (Utils.getWebServiceStatus(response)) {
                                        //startActivity(new Intent(ChangePasswordActivity.this, LoginActivity.class));
                                        Toast.makeText(ChangePasswordActivity.this, Utils.getWebServiceMessage(response), Toast.LENGTH_LONG).show();
                                        mActivity.finish();
                                    } else {
                                        Utils.showDialog(ChangePasswordActivity.this, "Error", Utils.getWebServiceMessage(response));
                                    }


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pdialog.dismiss();
                        Utils.showExceptionDialog(ChangePasswordActivity.this);
                    }
                });
                pdialog.show();
                GroomerApplication.getInstance().getRequestQueue().add(postReq);
            }
        } else {
            Utils.showNoNetworkDialog(ChangePasswordActivity.this);
        }


    }


    public boolean validateForm() {

        if (getEditTextText(R.id.edt_password).equals("")) {
            Utils.showDialog(this, getString(R.string.message_title), getString(R.string.alert_please_enter_previous_password));
            return false;
        } else if (getEditTextText(R.id.edt_new_password).equals("")) {
            Utils.showDialog(this, getString(R.string.message_title), getString(R.string.alert_please_enter_new_password));
            return false;
        } else if (getEditTextText(R.id.edt_confirm_password).equals("")) {
            Utils.showDialog(this, getString(R.string.message_title), getString(R.string.alert_please_enter_confirm_password));
            return false;
        } else if (!getEditTextText(R.id.edt_confirm_password).equals
                (getEditTextText(R.id.edt_new_password))) {
            Utils.showDialog(this, getString(R.string.message_title), getString(R.string.alert_new_and_confirm_password_are_not_same));
            return false;
        }
        return true;
    }

}
