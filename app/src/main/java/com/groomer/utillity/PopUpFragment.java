package com.groomer.utillity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.groomer.R;
import java.util.ArrayList;

@SuppressLint("NewApi")
public class PopUpFragment<E> extends DialogFragment {

    private ArrayList<E> list;
    private String title;
    private FetchPopUpSelectValue fetchPopUpSelectValue;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_pop_up);

        list = (ArrayList<E>) getArguments()
                .getSerializable("popUpList");

        title = getArguments().getString("title");


        init(dialog);
        dialog.show();

        return dialog;
    }


    private void init(final Dialog dialog) {

        ListView listView = (ListView) dialog.findViewById(R.id.listview_popup);
        TextView titleText =(TextView)dialog.findViewById(R.id.txt_title);
        titleText.setText(title);

        PopUpAdapter countryListAdapter = new PopUpAdapter(getActivity(), list,title);
        listView.setAdapter(countryListAdapter);
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        dialog.dismiss();

                       // SchoolDTO schoolDTO = (SchoolDTO) list.get(position);
                        fetchPopUpSelectValue.selectedValue(position, title);

                    }
                }
        );


    }


    public void setFetchSelectedInterface(Object activity) {
        fetchPopUpSelectValue = (FetchPopUpSelectValue) activity;
    }

}
