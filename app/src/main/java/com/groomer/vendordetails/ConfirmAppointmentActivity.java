package com.groomer.vendordetails;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.andressantibanez.ranger.Ranger;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.groomer.GroomerApplication;
import com.groomer.R;
import com.groomer.activity.BaseActivity;
import com.groomer.appointment.adapter.SwipeMenuListViewAdapter;
import com.groomer.home.HomeActivity;
import com.groomer.model.ServiceDTO;
import com.groomer.utillity.Constants;
import com.groomer.utillity.Utils;
import com.groomer.volley.CustomJsonRequest;

import org.joda.time.LocalDateTime;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class ConfirmAppointmentActivity extends BaseActivity implements
        SeekBar.OnSeekBarChangeListener, SwipeMenuListView.OnMenuItemClickListener {

    private SwipeMenuListView mRecyclerView;
    private Ranger date_picker;
    private Activity mActivity;
    private SeekBar timeSeekbar;
    private List<ServiceDTO> serviceDTOList;
    private SwipeMenuListViewAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme();
        setContentView(R.layout.activity_confirm_appointment);
        mActivity = ConfirmAppointmentActivity.this;

        init();
        setUpRecyclerView();
    }

    /**
     * initializes parameters of activity and sets the text as wanted.
     */
    private void init() {
        timeSeekbar = (SeekBar) findViewById(R.id.time_seekbar);
        timeSeekbar.setProgress(9);
        timeSeekbar.setOnSeekBarChangeListener(this);
        serviceDTOList = (List<ServiceDTO>) getIntent()
                .getSerializableExtra("serviceDTO");

        setClick(R.id.confirm_appoint_cross_button);
        setClick(R.id.ranger_back_arrow);
        setClick(R.id.ranger_next_arrow);
        setClick(R.id.confirm_appointment_btn);

        setViewText(R.id.confirm_appoint_txt_service_price,
                getIntent().getStringExtra("totalPrice"));
        setViewText(R.id.confirm_appoint_txt_service_name,
                getIntent().getStringExtra("saloonName"));
        setViewText(R.id.confirm_appoint_txt_service_address,
                getIntent().getStringExtra("saloonAddress"));
    }

    private void setUpRecyclerView() {
        date_picker = (Ranger) findViewById(R.id.date_picker);
        mRecyclerView = (SwipeMenuListView) findViewById(R.id.confirm_appoint_service_list);

        adapter = new SwipeMenuListViewAdapter(mActivity, serviceDTOList);
        createSwipeMenu();
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setOnMenuItemClickListener(this);
        setListViewHeightBasedOnItems(mRecyclerView);

    }

    private void createSwipeMenu() {
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem remove = new SwipeMenuItem(mActivity);
                remove.setWidth(convert_dp_to_px(100));
                remove.setBackground(R.color.red);
                remove.setTitle("Remove");
                remove.setTitleSize(15);
                remove.setTitleColor(Color.WHITE);
                menu.addMenuItem(remove);
            }
        };

        mRecyclerView.setMenuCreator(creator);
        mRecyclerView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

            @Override
            public void onSwipeStart(int position) {
                // swipe start
                mRecyclerView.smoothOpenMenu(position);
            }

            @Override
            public void onSwipeEnd(int position) {
                // swipe end
            }
        });

    }

    private int convert_dp_to_px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.confirm_appoint_cross_button:
                finish();
                break;
            case R.id.ranger_back_arrow:
                Utils.decreaseDate(date_picker);
                break;
            case R.id.ranger_next_arrow:
                Utils.increaseDate(date_picker);
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
            params.height = totalItemsHeight + totalDividersHeight;
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
        LocalDateTime dateTime = new LocalDateTime();
        int month = dateTime.getMonthOfYear();
        int year = dateTime.getYear();
        String date = date_picker.getSelectedDay() + "-" + month + "-" + year;
        String amount = getAmount();

        HashMap<String, String> params = new HashMap<>();
        params.put("action", "confirm_appointment");
        params.put("user_id", Utils.getUserId(mActivity));
        params.put("lang", Utils.getSelectedLanguage(mActivity));
        params.put("store_id", getIntent().getStringExtra("store_id"));
        params.put("services", getServices());
        params.put("date", date);
        params.put("time", getViewText(R.id.txt_selected_time));
        params.put("amount", amount);

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

    /**
     * this method return the amount seperated from the amount unit.
     *
     * @return the string of amount.
     */
    private String getAmount() {
        String amount = getIntent().getStringExtra("totalPrice");
        return amount.split(" ")[1];
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

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (progress > 12) {
            setViewText(R.id.txt_selected_time,
                    (progress - 12) < 10 ? "0" + (progress - 12) + ":00 PM" : (progress - 12) + ":00 PM");
        } else {
            setViewText(R.id.txt_selected_time,
                    progress < 10 ? "0" + progress + ":00 AM" : progress + ":00 AM");
        }

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
        Toast.makeText(mActivity, "Remove Clicked", Toast.LENGTH_SHORT).show();
        removeServiceFromTheList(position);
        return false;
    }

    /**
     * this method removes the service from the list and substract the amount from total amount.
     *
     * @param position is the position of selected service.
     */
    private void removeServiceFromTheList(int position) {
        serviceDTOList.remove(position);
        adapter.setServiceList(serviceDTOList);
        adapter.notifyDataSetChanged();
        setListViewHeightBasedOnItems(mRecyclerView);
        double amount = 0.0;
        for (int i = 0; i < serviceDTOList.size(); i++) {
            amount = Double.valueOf(serviceDTOList.get(i).getPrice());
        }
        setViewText(R.id.confirm_appoint_txt_service_price, "SAR " + amount + "");
    }
}
