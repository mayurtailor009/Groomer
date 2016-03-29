package com.groomer.category;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.groomer.GroomerApplication;
import com.groomer.R;
import com.groomer.fragments.BaseFragment;
import com.groomer.model.CategoryDTO;
import com.groomer.utillity.Constants;
import com.groomer.utillity.Utils;
import com.groomer.volley.CustomJsonRequest;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mayur on 09-03-2016.
 */
public class SaloonListFragment extends BaseFragment {

    View view;
    ListView listView;
    GridView gridView;


    public static SaloonListFragment newInstance() {
        SaloonListFragment fragment = new SaloonListFragment();

        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_saloon_list, container, false);
        init();
        return view;
    }

    private void init() {
        listView = (ListView) view.findViewById(R.id.listview);
        gridView = (GridView) view.findViewById(R.id.gridview);
        getCategoryList();
    }

    public void swapeView() {
        if (gridView.getVisibility() == View.VISIBLE) {
            gridView.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        } else {
            gridView.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }
    }

    private void getCategoryList() {

        if (Utils.isOnline(getActivity())) {
            Map<String, String> params = new HashMap<>();
            params.put("action", Constants.SALOON_LIST_METHOD);
            params.put("lang", "eng");
            final ProgressDialog pdialog = Utils.createProgressDialog(getActivity(), null, false);
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, Constants.SERVICE_URL, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                pdialog.dismiss();
                                Utils.ShowLog(Constants.TAG, "Response=>" + response.toString());
                                Type type = new TypeToken<ArrayList<CategoryDTO>>() {
                                }.getType();
                                List<CategoryDTO> categoryList = new Gson().fromJson(response.getJSONArray("category").toString(), type);
                                setAdapter(categoryList);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Utils.showExceptionDialog(getActivity());
                    pdialog.dismiss();
                }
            });
            pdialog.show();
            GroomerApplication.getInstance().getRequestQueue().add(postReq);
        } else {
            Utils.showNoNetworkDialog(getActivity());
        }
    }


    private void setAdapter(List<CategoryDTO> categoryList) {
        GridAdapter gridAdapter = new GridAdapter(getActivity(), categoryList);
        gridView.setAdapter(gridAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getActivity(), VendorListActivity.class));
            }
        });


        ListAdapter listAdapter = new ListAdapter(getActivity(), categoryList);
        listView.setAdapter(listAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getActivity(), VendorListActivity.class));
            }
        });

    }

    class GridAdapter extends BaseAdapter {
        private Context mContext;
        LayoutInflater inflater;
        private List<CategoryDTO> categoryList;
        private DisplayImageOptions options;

        public GridAdapter(Context c, List<CategoryDTO> list) {
            mContext = c;
            inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            categoryList = list;
            options = new DisplayImageOptions.Builder()
                    .showImageForEmptyUri(R.drawable.icon_9)
                    .showImageOnFail(R.drawable.icon_9)
                    .showImageOnLoading(R.drawable.icon_9)
                    .resetViewBeforeLoading(true)
                    .cacheOnDisk(true)
                    .imageScaleType(ImageScaleType.EXACTLY)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .considerExifParams(true)
                    .displayer(new SimpleBitmapDisplayer())
                    .build();
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return categoryList.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return categoryList.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            View grid = convertView;
            ViewHolder holder;
            if (convertView == null) {
                grid = inflater.inflate(R.layout.grid_item, null);
                holder = new ViewHolder();

                holder.txtCategoryName = (TextView) grid.findViewById(R.id.txt_category_name);
                holder.ivThumb = (ImageView) grid.findViewById(R.id.ivThumb);
                grid.setTag(holder);
            } else {
                holder = (ViewHolder) grid.getTag();
            }

            holder.txtCategoryName.setText(categoryList.get(position).getName_eng());
            ImageLoader.getInstance().displayImage(categoryList.get(position).getImage(), holder.ivThumb,
                    options);
            if (position % 2 != 0)
                grid.setBackgroundColor(getResources().getColor(R.color.grid_color_dark));
            return grid;
        }


        private class ViewHolder {
            private TextView txtCategoryName;
            private ImageView ivThumb;
        }
    }

    class ListAdapter extends BaseAdapter {
        private Context mContext;
        LayoutInflater inflater;
        private List<CategoryDTO> categoryList;
        private DisplayImageOptions options;

        public ListAdapter(Context c, List<CategoryDTO> list) {
            mContext = c;
            inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            categoryList = list;
            options = new DisplayImageOptions.Builder()
                    .showImageForEmptyUri(R.drawable.icon_9)
                    .showImageOnFail(R.drawable.icon_9)
                    .showImageOnLoading(R.drawable.icon_9)
                    .resetViewBeforeLoading(true)
                    .cacheOnDisk(true)
                    .imageScaleType(ImageScaleType.EXACTLY)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .considerExifParams(true)
                    .displayer(new SimpleBitmapDisplayer())
                    .build();
        }

        @Override
        public int getCount() {

            return categoryList.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return categoryList.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            View list = convertView;
            ViewHolder holder;
            if (convertView == null) {
                list = inflater.inflate(R.layout.list_item, null);
                holder = new ViewHolder();

                holder.txtCategoryName = (TextView) list.findViewById(R.id.txt_category_name);
                holder.tvCount = (TextView) list.findViewById(R.id.tv_count);
                holder.ivThumb = (ImageView) list.findViewById(R.id.ivThumb);
                list.setTag(holder);
            } else {
                holder = (ViewHolder) list.getTag();
            }

            holder.txtCategoryName.setText(categoryList.get(position).getName_eng());
            holder.tvCount.setText(categoryList.get(position).getId());
            ImageLoader.getInstance().displayImage(categoryList.get(position).getImage(), holder.ivThumb,
                    options);
            return list;
        }


        private class ViewHolder {
            private TextView txtCategoryName;
            private ImageView ivThumb;
            private TextView tvCount;
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.list_view:
                swapeView();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
