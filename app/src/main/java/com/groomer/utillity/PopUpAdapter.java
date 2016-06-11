package com.groomer.utillity;


import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.groomer.model.CategoryDTO;
import com.groomer.R;

import java.util.List;

public class PopUpAdapter<E> extends BaseAdapter {

    private Context context;
    private List<E> popUpList;
    private String title;

    public PopUpAdapter(Context context, List<E> popUpList, String title) {
        this.context = context;
        this.popUpList = popUpList;
        this.title = title;

    }

    @Override
    public int getCount() {
        return popUpList.size();
    }

    @Override
    public Object getItem(int position) {
        return popUpList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;

        try {

            if (view == null) {
                LayoutInflater layoutInflater = (LayoutInflater) ((Activity) context).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = (View) layoutInflater.inflate(R.layout.layout_popup_row, parent, false);
                holder = new ViewHolder();
                holder.txt_title = (TextView) view.findViewById(R.id.txt_title);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }


            CategoryDTO feeDurationDTO = (CategoryDTO) popUpList.get(position);

            if (HelpMe.isArabic(context)) {
                holder.txt_title.setText(feeDurationDTO.getName_ara());
            } else {
                holder.txt_title.setText(feeDurationDTO.getName_eng());
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;

    }


    public static class ViewHolder {
        TextView txt_title;
    }
}
