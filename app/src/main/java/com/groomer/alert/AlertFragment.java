package com.groomer.alert;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import com.groomer.alert.adapter.AlertAdapter;
import com.groomer.alert.adapter.SwipeAlertListAdapter;
import com.groomer.fragments.BaseFragment;
import com.groomer.model.NotificationDTO;
import com.groomer.utillity.Constants;
import com.groomer.utillity.Theme;
import com.groomer.utillity.Utils;
import com.groomer.volley.CustomJsonRequest;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AlertFragment extends BaseFragment {


    private View view;
    private Context mActivity;
    private SwipeMenuListView mSwipeListView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<NotificationDTO> notificationDTOList;
    private final static String TAG = "AlertFragment";

    public AlertFragment() {
        // Required empty public constructor
    }


    public static AlertFragment newInstance() {
        AlertFragment fragment = new AlertFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        view = inflater.inflate(R.layout.fragment_alert, container, false);

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mSwipeListView = (SwipeMenuListView) view.findViewById(R.id.recycle_alert);

        getNotificationList();

        // Add pull to refresh functionality
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.active_swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                getNotificationList();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

    }


    private void getNotificationList() {
        if (Utils.isOnline(getActivity())) {
            Map<String, String> params = new HashMap<>();
            params.put("action", Constants.NOTIFICATIONS);
            params.put("user_id", Utils.getUserId(mActivity));
            params.put("lang", Utils.getSelectedLanguage(mActivity));
            final ProgressDialog pdialog = Utils.createProgressDialog(getActivity(), null, false);
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST,
                    Constants.SERVICE_URL, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response.getBoolean("status")) {
                                    TextView txt_blank = (TextView) view.
                                            findViewById(R.id.txt_blank);
                                    txt_blank.setVisibility(View.GONE);
                                    Utils.ShowLog(TAG, "got some response = "
                                            + response.toString());
                                    Type type = new TypeToken<ArrayList<NotificationDTO>>() {
                                    }.getType();
                                    notificationDTOList = new Gson().
                                            fromJson(response.getJSONArray("notifications").
                                                    toString(), type);
                                    setNotificationList(notificationDTOList);
                                } else {
                                    mSwipeListView.setVisibility(View.GONE);
                                    String msg = response.getString("message");
                                    TextView txt_blank = (TextView) view.
                                            findViewById(R.id.txt_blank);
                                    txt_blank.setVisibility(View.VISIBLE);
                                    txt_blank.setText(msg);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            mSwipeRefreshLayout.setRefreshing(false);
                            pdialog.dismiss();
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    pdialog.dismiss();
                    mSwipeRefreshLayout.setRefreshing(false);
                    Utils.showExceptionDialog(getActivity());
                }
            });
            GroomerApplication.getInstance().getRequestQueue().add(postReq);
            postReq.setRetryPolicy(new DefaultRetryPolicy(
                    30000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            pdialog.show();
        } else {
            mSwipeRefreshLayout.setRefreshing(false);
            Utils.showNoNetworkDialog(getActivity());
        }


    }


    private void setNotificationList(List<NotificationDTO> notificationDTOList) {
        SwipeAlertListAdapter adapter = new SwipeAlertListAdapter(mActivity, notificationDTOList);
        createDeleteMenu(adapter);
        mSwipeListView.setAdapter(adapter);

    }

    private void createDeleteMenu(SwipeAlertListAdapter adapter) {
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem item = new SwipeMenuItem(mActivity);
                item.setWidth(convert_dp_to_px(100));
                changeDeleteBtnColorAsTheme(item);
                item.setTitle(mActivity.getResources().getString(R.string.delete));
                item.setTitleSize(15);
                item.setTitleColor(Color.WHITE);
                menu.addMenuItem(item);
            }
        };

        mSwipeListView.setMenuCreator(creator);
        mSwipeListView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {
            @Override
            public void onSwipeStart(int position) {
                mSwipeListView.smoothOpenMenu(position);
            }

            @Override
            public void onSwipeEnd(int position) {

            }
        });

        setClickOnDeleteMenu(adapter); //performs the click event on Delete menu item of swipe list view.
    }

    private void setClickOnDeleteMenu(final SwipeAlertListAdapter adapter) {
        mSwipeListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                final int itemPos = position;
                Map<String, String> params = new HashMap<>();
                params.put("action", Constants.DELETE_NOTIFICATION);
                params.put("notification_id", notificationDTOList.get(position).getNotification_id());
                final ProgressDialog pdialog = Utils.createProgressDialog(getActivity(), null, false);
                CustomJsonRequest jsonRequest = new CustomJsonRequest(Request.Method.POST,
                        Constants.SERVICE_URL,
                        params,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i("Groomer info", response.toString());
                                pdialog.dismiss();
                                try {
                                    if (Utils.getWebServiceStatus(response)) {
                                        if (notificationDTOList != null
                                                && !notificationDTOList.isEmpty()) {
                                            notificationDTOList
                                                    .remove(notificationDTOList.get(itemPos));
                                            adapter.notifyDataSetChanged();
                                        }
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

                GroomerApplication.getInstance().addToRequestQueue(jsonRequest);
                jsonRequest.setRetryPolicy(new DefaultRetryPolicy(30000, 0,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                pdialog.show();
                return true;
            }
        });
    }

    private void changeDeleteBtnColorAsTheme(SwipeMenuItem item) {
        Theme theme = Utils.getObjectFromPref(mActivity, Constants.CURRENT_THEME);

        if (theme.equals(Theme.Blue)) {
            item.setBackground(R.color.theme_blue);
        } else if (theme.equals(Theme.Red)) {
            item.setBackground(R.color.theme_red);
        } else {
            item.setBackground(R.color.theme_green);
        }
    }

    private int convert_dp_to_px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

}
