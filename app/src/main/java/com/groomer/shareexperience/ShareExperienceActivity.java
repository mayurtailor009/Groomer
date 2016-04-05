package com.groomer.shareexperience;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.groomer.GroomerApplication;
import com.groomer.R;
import com.groomer.activity.BaseActivity;
import com.groomer.utillity.Constants;
import com.groomer.utillity.Utils;
import com.groomer.volley.CustomJsonRequest;

import org.json.JSONObject;

import java.util.HashMap;

public class ShareExperienceActivity extends BaseActivity {

    private Activity mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme();
        setContentView(R.layout.activity_share_experience);

        setHeader(getString(R.string.share_title));
        setLeftClick(R.drawable.back_btn);
        setClick(R.id.share_exp_submitbtn);

        mActivity = this;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.hamburgur_img_icon:
                onBackPressed();
                break;

            case R.id.share_exp_submitbtn:
                RatingBar ratingBar = (RatingBar) findViewById(R.id.share_exp_ratingbar);
                submitReview(ratingBar.getRating() + "");
                break;
        }
    }


    @Override
    public void onBackPressed() {
        this.finish();
    }


    private void submitReview(String rating) {
        if (Utils.isOnline(mActivity)) {
            HashMap<String, String> params = new HashMap<>();
            params.put("action", Constants.ADD_REVIEW);
            params.put("user_id", Utils.getUserId(mActivity));
            // params.put("store_id", getIntent().getStringExtra("storeId"));
            params.put("store_id", "17");
            params.put("lang", "eng");
            params.put("rating", rating);
            params.put("review", getEditTextText(R.id.edt_write_something));

            final ProgressDialog pdialog = Utils.createProgressDialog(mActivity, null, false);
            CustomJsonRequest jsonRequest = new CustomJsonRequest(Request.Method.POST,
                    Constants.SERVICE_URL, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.i("Groomer info", response.toString());
                            pdialog.dismiss();
                            if (Utils.getWebServiceStatus(response)) {
                                try {

                                    Toast.makeText(mActivity, Utils.getWebServiceMessage(response), Toast.LENGTH_SHORT).show();
                                    finish();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {

                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("Groomer info", error.toString());
                        }
                    }
            );

            pdialog.show();
            GroomerApplication.getInstance().addToRequestQueue(jsonRequest);
            jsonRequest.setRetryPolicy(new DefaultRetryPolicy(30000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        }


    }
}
