package com.groomer.reschedule;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.andressantibanez.ranger.Ranger;
import com.groomer.R;
import com.groomer.utillity.Utils;

public class RescheduleDialogFragment extends DialogFragment {
    private Ranger date_picker;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.reschedule_dialog);
        date_picker = (Ranger) dialog.findViewById(R.id.dialog_date_picker);
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

        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        return dialog;
    }

    public static RescheduleDialogFragment getInstance() {
        return new RescheduleDialogFragment();
    }
}
