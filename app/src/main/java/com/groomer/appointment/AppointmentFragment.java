package com.groomer.appointment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.groomer.GroomerApplication;
import com.groomer.R;
import com.groomer.appointment.adapter.AppointmentListAdapter;
import com.groomer.fragments.BaseFragment;
import com.groomer.model.AppointmentDTO;
import com.groomer.utillity.Constants;
import com.groomer.utillity.Utils;
import com.groomer.volley.CustomJsonRequest;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class AppointmentFragment extends BaseFragment {

    private View view;
    private ExpandableListView mExpandableListView;
    private Activity mActivity;
    private int lastExpandedPosition = -1;
    private ExpandableListView mExpandableCompleteListView;
    private Button btn_appointment_complete;
    private Button btn_appointment;

    public static AppointmentFragment newInstance() {
        AppointmentFragment fragment = new AppointmentFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_appointment, container, false);

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = getActivity();
        mExpandableListView = (ExpandableListView) view.findViewById(R.id.appointment_list);
        mExpandableCompleteListView = (ExpandableListView) view.findViewById(R.id.listview_complete_appointment);
        btn_appointment = (Button) view.findViewById(R.id.btn_appointment);
        btn_appointment_complete = (Button) view.findViewById(R.id.btn_appointment_complete);

        init();
        btn_appointment.setSelected(true);
        getAppointmentList();


    }

    private void init() {
        setTouchNClick(R.id.btn_appointment, view);
        setTouchNClick(R.id.btn_appointment_complete, view);

    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.btn_appointment:
                btn_appointment.setSelected(true);
                btn_appointment_complete.setSelected(false);
                mExpandableListView.setVisibility(View.VISIBLE);
                mExpandableCompleteListView.setVisibility(View.GONE);
                getAppointmentList();

                break;
            case R.id.btn_appointment_complete:
                btn_appointment.setSelected(false);
                btn_appointment_complete.setSelected(true);
                mExpandableListView.setVisibility(View.GONE);
                mExpandableCompleteListView.setVisibility(View.VISIBLE);
                getAppointmentCompleteList();

                break;
        }
    }


    private void setUpExpandableListVIew(final List<AppointmentDTO> appointmentList) {
        setViewVisibility(R.id.no_appointment, view, View.GONE);
        setViewVisibility(R.id.appointment_list, view, View.VISIBLE);

        mExpandableListView.setChoiceMode(ExpandableListView.CHOICE_MODE_SINGLE);
        mExpandableListView.setAdapter(new AppointmentListAdapter(
                this.getActivity(), appointmentList, mExpandableListView)
        );

        mExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition) {
                    mExpandableListView.collapseGroup(lastExpandedPosition);
                }
                if (appointmentList.get(groupPosition).isPassedDateFlag()) {
                    mExpandableListView.collapseGroup(groupPosition);
                }
                lastExpandedPosition = groupPosition;
            }
        });
    }

    private void setUpCompleteExpandableListVIew(final List<AppointmentDTO> appointmentList) {
        setViewVisibility(R.id.no_appointment, view, View.GONE);
        setViewVisibility(R.id.listview_complete_appointment, view, View.VISIBLE);

        mExpandableCompleteListView.setChoiceMode(ExpandableListView.CHOICE_MODE_SINGLE);
        mExpandableCompleteListView.setAdapter(new AppointmentListAdapter(
                this.getActivity(), appointmentList, mExpandableCompleteListView)
        );

        mExpandableCompleteListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition) {
                    mExpandableCompleteListView.collapseGroup(lastExpandedPosition);
                }
                if (appointmentList.get(groupPosition).isPassedDateFlag()) {
                    mExpandableCompleteListView.collapseGroup(groupPosition);
                }
                lastExpandedPosition = groupPosition;
            }
        });
    }


    private void getAppointmentCompleteList() {
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("action", Constants.APPOINTMENTS);
            params.put("user_id", Utils.getUserId(mActivity));
            params.put("lang", Utils.getSelectedLanguage(mActivity));

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
                                    Type type = new TypeToken<ArrayList<AppointmentDTO>>() {
                                    }.getType();
                                    List<AppointmentDTO> appointmentList = new Gson()
                                            .fromJson(response
                                                    .getJSONArray("completed").toString(), type);
                                    setUpCompleteExpandableListVIew(appointmentList);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                setViewVisibility(R.id.no_appointment, view, View.VISIBLE);
                                setViewVisibility(R.id.appointment_list, view, View.GONE);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getAppointmentList() {
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("action", Constants.APPOINTMENTS);
            params.put("user_id", Utils.getUserId(mActivity));
            params.put("lang", Utils.getSelectedLanguage(mActivity));

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
                                    Type type = new TypeToken<ArrayList<AppointmentDTO>>() {
                                    }.getType();
                                    List<AppointmentDTO> appointmentList = new Gson()
                                            .fromJson(response
                                                    .getJSONArray("appointment").toString(), type);
                                    setUpExpandableListVIew(appointmentList);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                setViewVisibility(R.id.no_appointment, view, View.VISIBLE);
                                setViewVisibility(R.id.appointment_list, view, View.GONE);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
