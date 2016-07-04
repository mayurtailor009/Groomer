package com.groomer.reschedule;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.groomer.GroomerApplication;
import com.groomer.R;
import com.groomer.utillity.Constants;
import com.groomer.utillity.Utils;
import com.groomer.volley.CustomJsonRequest;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;

public class RescheduleDialogNewFragment extends DialogFragment {
    private String order_id;
    private Activity mActivity;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private Button btnDate, btnTime, btnSubmit;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.reschedule_dialog_new);
        mActivity = getActivity();

        init(dialog);



        dialog.findViewById(R.id.cross_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnSubmit
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rescheduleAppointment();
                    }
                });

       btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                mMonth = monthOfYear; mDay = dayOfMonth; mYear = year;
                                btnDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                if(mHour!=0)
                                    btnSubmit.setEnabled(true);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
                datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
            }
        });

        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                if(mDay!=0)
                                    btnSubmit.setEnabled(true);
                                String AM_PM ;
                                if(hourOfDay < 12) {
                                    AM_PM = "AM";
                                } else {
                                    AM_PM = "PM";
                                }
                                if(hourOfDay>11)
                                    hourOfDay = hourOfDay-12;
                                btnTime.setText(hourOfDay + ":" + (minute<10?"0"+minute:minute) +" "+AM_PM);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        return dialog;
    }

    private void init(Dialog dialog) {

        order_id = getArguments().getString("order_id");
        btnDate = (Button) dialog.findViewById(R.id.btn_date);
        btnTime = (Button) dialog.findViewById(R.id.btn_time);
        btnSubmit = (Button) dialog.findViewById(R.id.btn_dialog_date_confirm);
    }

    private void rescheduleAppointment() {
        String date = mDay + "-" + (mMonth+1) + "-" + mYear;

        HashMap<String, String> params = new HashMap<>();
        params.put("action", Constants.RESCHEDULE_APPOINTMENT);
        params.put("user_id", Utils.getUserId(mActivity));
        params.put("lang", Utils.getSelectedLanguage(mActivity));
        params.put("date", date);
        params.put("time", btnTime.getText().toString());
        params.put("order_id", order_id);

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
                                Toast.makeText(mActivity, Utils.getWebServiceMessage(response),
                                        Toast.LENGTH_SHORT).show();
                                dismiss();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("Groomer info", error.toString());
                        pdialog.dismiss();
                        Utils.showExceptionDialog(mActivity);
                    }
                }
        );

        pdialog.show();
        GroomerApplication.getInstance().addToRequestQueue(jsonRequest);
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(30000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public static RescheduleDialogNewFragment getInstance() {
        return new RescheduleDialogNewFragment();
    }
}
