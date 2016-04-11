package com.groomer.reschedule;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.andressantibanez.ranger.Ranger;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.groomer.GroomerApplication;
import com.groomer.R;
import com.groomer.utillity.Constants;
import com.groomer.utillity.GroomerPreference;
import com.groomer.utillity.Utils;
import com.groomer.volley.CustomJsonRequest;

import org.joda.time.LocalDateTime;
import org.json.JSONObject;

import java.util.HashMap;

public class RescheduleDialogFragment extends DialogFragment {
    private Ranger date_picker;
    private String order_id;
    private Activity mActivity;
    private SeekBar timeSeekbar;
    private TextView timeTextView;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.reschedule_dialog);
        mActivity = getActivity();

        init(dialog);


        dialog.findViewById(R.id.dialog_ranger_back_arrow)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Utils.decreaseDate(date_picker);
                    }
                });
        dialog.findViewById(R.id.dialog_ranger_next_arrow)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Utils.increaseDate(date_picker);
                    }
                });
        dialog.findViewById(R.id.cross_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.btn_dialog_date_confirm)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rescheduleAppointment();
                    }
                });

        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        return dialog;
    }

    private void init(Dialog dialog) {
        timeSeekbar = (SeekBar) dialog.findViewById(R.id.time_seekbar);
        timeTextView = (TextView) dialog.findViewById(R.id.txt_selected_time);
        timeSeekbar.setProgress(9);
        timeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress > 12) {
                    timeTextView.setText((progress - 12) < 10 ? "0"
                            + (progress - 12) + ":00 PM" : (progress - 12) + ":00 PM");
                } else {
                    timeTextView.setText(progress < 10 ? "0"
                            + progress + ":00 AM" : progress + ":00 AM");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        date_picker = (Ranger) dialog.findViewById(R.id.dialog_date_picker);
        order_id = getArguments().getString("order_id");
    }

    private void rescheduleAppointment() {
        LocalDateTime dateTime = new LocalDateTime();
        int month = dateTime.getMonthOfYear();
        int year = dateTime.getYear();
        String date = date_picker.getSelectedDay() + "-" + month + "-" + year;

        HashMap<String, String> params = new HashMap<>();
        params.put("action", Constants.RESCHEDULE_APPOINTMENT);
        params.put("user_id", Utils.getUserId(mActivity));
        params.put("lang", Utils.getSelectedLanguage(mActivity));
        params.put("date", date);
        params.put("time", timeTextView.getText().toString());
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

    public static RescheduleDialogFragment getInstance() {
        return new RescheduleDialogFragment();
    }
}
