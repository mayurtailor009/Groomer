package com.groomer.category;

import android.app.Activity;
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

import com.android.volley.Cache;
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
import com.groomer.utillity.HelpMe;
import com.groomer.utillity.Utils;
import com.groomer.volley.CustomJsonRequest;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mayur on 09-03-2016.
 */
public class SaloonListFragment extends BaseFragment {

    private View view;
    private ListView listView;
    private GridView gridView;
    private Activity mActivity;


    public static SaloonListFragment newInstance() {
        SaloonListFragment fragment = new SaloonListFragment();

        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mActivity = getActivity();
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
        try {
            if (Utils.isOnline(mActivity)) {
                Map<String, String> params = new HashMap<>();
                params.put("action", Constants.SALOON_LIST_METHOD);
                params.put("lang", Utils.getSelectedLanguage(mActivity));
                final ProgressDialog pdialog = Utils.createProgressDialog(mActivity, null, false);
                CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, Constants.SERVICE_URL, params,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    pdialog.dismiss();
                                    Utils.ShowLog(Constants.TAG, "Response=>" + response.toString());
                                    setCategoryResponse(response);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Utils.showExceptionDialog(mActivity);
                        pdialog.dismiss();
                    }
                });
                pdialog.show();
                GroomerApplication.getInstance().getRequestQueue().add(postReq);
            } else {
                Cache.Entry entry = GroomerApplication.getInstance().getRequestQueue().
                        getCache().get(Constants.SALOON_LIST_METHOD);
                if (entry != null) {
                    try {
                        String data = new String(entry.data, "UTF-8");
                        JSONObject response = new JSONObject(data);
                        setCategoryResponse(response);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Utils.showNoNetworkDialog(mActivity);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setCategoryResponse(JSONObject response) {
        try {
            Type type = new TypeToken<ArrayList<CategoryDTO>>() {
            }.getType();
            final ArrayList<CategoryDTO> categoryList = new Gson().fromJson(response.getJSONArray("category").toString(), type);


            GridAdapter gridAdapter = new GridAdapter(mActivity, categoryList);
            gridView.setAdapter(gridAdapter);

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("dto", categoryList.get(position));
                    bundle.putSerializable("dtoList", categoryList);
                    Intent intent = new Intent(mActivity, VendorListActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });


            ListAdapter listAdapter = new ListAdapter(mActivity, categoryList);
            listView.setAdapter(listAdapter);


            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("dto", categoryList.get(position));
                    bundle.putSerializable("dtoList", categoryList);
                    Intent intent = new Intent(mActivity, VendorListActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    class GridAdapter extends BaseAdapter {
        private Context mContext;
        LayoutInflater inflater;
        private ArrayList<CategoryDTO> categoryList;
        private DisplayImageOptions options;

        public GridAdapter(Context c, ArrayList<CategoryDTO> list) {
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
            return categoryList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
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

            if (HelpMe.isArabic(mContext)) {
                holder.txtCategoryName.setText(categoryList.get(position).getName_ara());
            } else {
                holder.txtCategoryName.setText(categoryList.get(position).getName_eng());
            }

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
        private ArrayList<CategoryDTO> categoryList;
        private DisplayImageOptions options;

        public ListAdapter(Context c, ArrayList<CategoryDTO> list) {
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

            return categoryList.get(position);
        }

        @Override
        public long getItemId(int position) {

            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
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

            if (HelpMe.isArabic(mContext)) {
                holder.txtCategoryName.setText(categoryList.get(position).getName_ara());
            } else {
                holder.txtCategoryName.setText(categoryList.get(position).getName_eng());
            }
            String count = categoryList.get(position).getService_count();
            holder.tvCount.setText(count.equalsIgnoreCase("0") ? "" : count);
            ImageLoader.getInstance().displayImage(categoryList.get(position).getImage(),
                    holder.ivThumb,
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
