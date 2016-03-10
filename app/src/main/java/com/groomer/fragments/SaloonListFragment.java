package com.groomer.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.groomer.GroomerApplication;
import com.groomer.R;
import com.groomer.utillity.Constants;
import com.groomer.utillity.Utils;
import com.groomer.volley.CustomJsonRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mayur on 09-03-2016.
 */
public class SaloonListFragment extends BaseFragment{

    View view;
    ListView listView;
    GridView gridView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_saloon_list, container, false);

        init();

        GridAdapter gridAdapter = new GridAdapter(getActivity());
        gridView.setAdapter(gridAdapter);

        ListAdapter listAdapter = new ListAdapter(getActivity());
        listView.setAdapter(listAdapter);

        return view;
    }

    private void init(){
        listView = (ListView) view.findViewById(R.id.listview);
        gridView = (GridView) view.findViewById(R.id.gridview);
    }

    public void swapeView(){
        if(gridView.getVisibility() == View.VISIBLE){
            gridView.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        }else{
            gridView.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }
    }

    private void getSaloonList() {

        if (Utils.isOnline(getActivity())) {
            Map<String, String> params = new HashMap<>();
            params.put("action", Constants.SALOON_LIST_METHOD);

            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, Constants.SERVICE_URL, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Utils.ShowLog(Constants.TAG, "Response=>" + response.toString());
                                /*Type type = new TypeToken<ArrayList<TripDTO>>() {
                                }.getType();
                                List<TripDTO> tripList = new Gson().fromJson(response.getJSONArray("trip_list").toString(), type);*/
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Utils.showExceptionDialog(getActivity());
                }
            });
            GroomerApplication.getInstance().getRequestQueue().add(postReq);
        } else {
            Utils.showNoNetworkDialog(getActivity());
        }
    }

    class GridAdapter extends BaseAdapter {
        private Context mContext;
        LayoutInflater inflater;
        public GridAdapter(Context c) {
            mContext = c;
            inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return 12;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            View grid;

            grid = inflater.inflate(R.layout.grid_item, null);
            if(position%2!=0)
                grid.setBackgroundColor(getResources().getColor(R.color.grid_color_dark));
            return grid;
        }
    }

    class ListAdapter extends BaseAdapter {
        private Context mContext;
        LayoutInflater inflater;
        public ListAdapter(Context c) {
            mContext = c;
            inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return 12;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            View list;

            list = inflater.inflate(R.layout.list_item, null);

            return list;
        }
    }
}
