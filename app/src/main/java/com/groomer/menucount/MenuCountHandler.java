package com.groomer.menucount;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.groomer.GroomerApplication;
import com.groomer.model.MenuDTO;
import com.groomer.utillity.Constants;
import com.groomer.utillity.Utils;
import com.groomer.volley.CustomJsonRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


/**
 * Created by deepak.gupta on 07-09-2015.
 */
public class MenuCountHandler implements Runnable {

    public static final int MENU_COUNT_HANDLER = 1001;
    private static final String TAG = "MenuCountHandler";
    private Handler handler;
    private Activity mActivity;

    public MenuCountHandler(Handler handler, Activity mActivity) {
        this.handler = handler;
        this.mActivity = mActivity;
    }

    @Override
    public void run() {
        getMenuCount();
    }


    private void getMenuCount() {

        if (Utils.isOnline(mActivity)) {
            Map<String, String> params = new HashMap<>();
            params.put("action", Constants.MENU_COUNTER);
            params.put("user_id", Utils.getUserId(mActivity));
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST,
                    Constants.SERVICE_URL, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (Utils.getWebServiceStatus(response)) {
                                    //CustomProgressDialog.hideProgressDialog();
                                    Utils.ShowLog(TAG, "got Menu count response = " + response.toString());
                                    MenuDTO menuDTO;
                                    menuDTO = new Gson().fromJson(response.
                                            getJSONObject("count").toString(), MenuDTO.class);
                                    //setUpMenu();
                                    handleMenuCountResponse(menuDTO);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    // CustomProgressDialog.hideProgressDialog();
                    // Utils.showExceptionDialog(mActivity);
                    //setUpMenu();
                    //       CustomProgressDialog.hideProgressDialog();
                }
            });
            GroomerApplication.getInstance().getRequestQueue().add(postReq);
            postReq.setRetryPolicy(new DefaultRetryPolicy(
                    30000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        } else {

        }


    }

    private void handleMenuCountResponse(MenuDTO menuDTO) {
        Utils.ShowLog(TAG, "handleMenuCountResponse");
        Message msg = handler.obtainMessage(MENU_COUNT_HANDLER, menuDTO);
        handler.sendMessage(msg);

    }

//
}
