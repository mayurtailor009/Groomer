package com.groomer.appointment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.groomer.GroomerApplication;
import com.groomer.R;
import com.groomer.activity.BaseActivity;
import com.groomer.appointment.adapter.ModifySwipeMenuListViewAdapter;
import com.groomer.appointment.adapter.SwipeMenuListViewAdapter;
import com.groomer.home.HomeActivity;
import com.groomer.model.AppointServicesDTO;
import com.groomer.model.ServiceDTO;
import com.groomer.model.SloteDTO;
import com.groomer.recyclerviewitemclick.MyOnClickListener;
import com.groomer.recyclerviewitemclick.RecyclerTouchListener;
import com.groomer.utillity.Constants;
import com.groomer.utillity.Utils;
import com.groomer.vendordetails.adapter.TimeSlotesAdater;
import com.groomer.volley.CustomJsonRequest;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class ModifyAppointmentActivity extends BaseActivity {

    private ListView listView;
    private Activity mActivity;
    private List<AppointServicesDTO> serviceDTOList;
    private ModifySwipeMenuListViewAdapter adapter;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private Button btnSubmit;
    private TextView btnDate;
    private RecyclerView recyclerViewSlots;
    private List<SloteDTO> sloteList;
    private String selectedSlot;
    private double totalPrice = 0.0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme();
        setContentView(R.layout.activity_modify_appointment);
        mActivity = ModifyAppointmentActivity.this;

        init();
        setUpRecyclerView();
    }

    /**
     * initializes parameters of activity and sets the text as wanted.
     */
    private void init() {

        btnSubmit = (Button) findViewById(R.id.confirm_appointment_btn);
        btnDate = (TextView) findViewById(R.id.btn_date);
        btnDate.setText(Utils.getCurrentDate());
        // btnTime = (Button) findViewById(R.id.btn_time);

        serviceDTOList = (List<AppointServicesDTO>) getIntent()
                .getSerializableExtra("serviceDTO");


        boolean isModifyAppointment = getIntent().getBooleanExtra("isModifyAppointment", false);
        if (isModifyAppointment) {
            setButtonText(R.id.confirm_appointment_btn,
                    getString(R.string.btn_reschedule_appointment));
        }

        setClick(R.id.confirm_appoint_cross_button);
        setClick(R.id.btn_date);
        setClick(R.id.confirm_appointment_btn);

        setViewText(R.id.confirm_appoint_txt_service_price,
                getIntent().getStringExtra("totalPrice"));
        setViewText(R.id.confirm_appoint_txt_service_name,
                getIntent().getStringExtra("saloonName"));
        setViewText(R.id.confirm_appoint_txt_service_address,
                getIntent().getStringExtra("saloonAddress"));
        recyclerViewSlots = (RecyclerView) findViewById(R.id.recycle_time_slot);
        final LinearLayoutManager manager = new LinearLayoutManager(mActivity,
                LinearLayoutManager.HORIZONTAL, false);
        recyclerViewSlots.setLayoutManager(manager);

        getTimeSlotes();
    }


    private void getTimeSlotes() {
        if (Utils.isOnline(mActivity)) {
            HashMap<String, String> params = new HashMap<>();
            params.put("action", "available_schedule");
            params.put("lang", Utils.getSelectedLanguage(mActivity));
            params.put("store_id", getIntent().getStringExtra("store_id"));
            params.put("date", btnDate.getText().toString());

            final ProgressDialog pdialog = Utils.createProgressDialog(mActivity, null, false);
            CustomJsonRequest jsonRequest = new CustomJsonRequest(Request.Method.POST,
                    Constants.SERVICE_URL, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.i("Groomer info", response.toString());
                            pdialog.dismiss();
                            try {
                                if (Utils.getWebServiceStatus(response)) {
                                    Type type = new TypeToken<ArrayList<SloteDTO>>() {
                                    }.getType();
                                    sloteList = new Gson()
                                            .fromJson(response
                                                    .getJSONArray("schedule").toString(), type);

                                    setSlotedList();
                                } else {
                                    Toast.makeText(ModifyAppointmentActivity.this,
                                            Utils.getWebServiceMessage(response), Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
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


    }

    private void setSlotedList() {

        final TimeSlotesAdater timeSlotesAdater = new TimeSlotesAdater(mActivity, sloteList);
        recyclerViewSlots.setAdapter(timeSlotesAdater);


        recyclerViewSlots.addOnItemTouchListener(new RecyclerTouchListener(mActivity, recyclerViewSlots, new MyOnClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                selectedSlot = "";
                selectedSlot = sloteList.get(position).getId();

                for (int i = 0; i < sloteList.size(); i++) {
                    if (i == position) {
                        sloteList.get(i).setSelected(selectedSlot);
                    } else {
                        sloteList.get(i).setSelected("");
                    }
                }

                timeSlotesAdater.setServiceDTOList(sloteList);
                timeSlotesAdater.notifyDataSetChanged();


            }
        }));
    }


    private void setUpRecyclerView() {
        listView = (ListView) findViewById(R.id.confirm_appoint_service_list);
        adapter = new ModifySwipeMenuListViewAdapter(mActivity, serviceDTOList);
        listView.setAdapter(adapter);
        setListViewHeightBasedOnItems(listView);

    }


    private int convert_dp_to_px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    @Override
    public void onClick(View view) {
        Calendar c = Calendar.getInstance();
        switch (view.getId()) {
            case R.id.confirm_appoint_cross_button:
                finish();
                break;

            case R.id.btn_date:
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(ModifyAppointmentActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                mMonth = monthOfYear;
                                mDay = dayOfMonth;
                                mYear = year;
                                //   btnDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                btnDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                getTimeSlotes();

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
                datePickerDialog.show();
                break;
            case R.id.confirm_appointment_btn:
                confirmAppointment();
                break;
        }
    }


    public static boolean setListViewHeightBasedOnItems(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = (totalItemsHeight + totalDividersHeight);
            listView.setLayoutParams(params);
            listView.requestLayout();

            return true;

        } else {
            return false;
        }

    }

    /**
     * this method calls confirm appoint as you selected date and time.
     */
    private void confirmAppointment() {
        String amount = getAmount();
        if (validateForm()) {

            HashMap<String, String> params = new HashMap<>();
            params.put("action", "confirm_appointment");
            params.put("user_id", Utils.getUserId(mActivity));
            params.put("lang", Utils.getSelectedLanguage(mActivity));
            params.put("store_id", getIntent().getStringExtra("store_id"));
            params.put("services", getServices());
            params.put("date", btnDate.getText().toString());
            params.put("amount", amount);
            params.put("slot_id", selectedSlot);

            final ProgressDialog pdialog = Utils.createProgressDialog(mActivity, null, false);
            CustomJsonRequest jsonRequest = new CustomJsonRequest(Request.Method.POST,
                    Constants.SERVICE_URL, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.i("Groomer info", response.toString());
                            pdialog.dismiss();
                            try {
                                if (Utils.getWebServiceStatus(response)) {

                                    finish();
                                    Intent intent = new Intent(mActivity, HomeActivity.class);
                                    intent.putExtra("fragmentNumber", 2);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(ModifyAppointmentActivity.this,
                                            Utils.getWebServiceMessage(response), Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
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
    }

    /**
     * this method return the amount seperated from the amount unit.
     *
     * @return the string of amount.
     */
    private String getAmount() {
        String amount = getIntent().getStringExtra("totalPrice");
        return amount;
    }

    /**
     * this method gets the servcies id from the services list.
     *
     * @return a string of services_ids with comma seperated.
     */
    private String getServices() {
        String services = "";
        for (int i = 0; i < serviceDTOList.size(); i++) {
            if (i < serviceDTOList.size() - 1) {
                services += serviceDTOList.get(i).getService_id() + ",";
            } else {
                services += serviceDTOList.get(i).getService_id();
            }
        }
        return services;
    }


    private boolean validateForm() {

        if (selectedSlot.equalsIgnoreCase("")) {
            return false;
        }

        return true;
    }
}
