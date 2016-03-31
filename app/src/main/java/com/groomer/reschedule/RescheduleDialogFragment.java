package com.groomer.reschedule;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

import com.groomer.R;

/**
 * Created by Deepak Singh on 01-Apr-16.
 */
public class RescheduleDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.reschedule_dialog);
        dialog.show();
        return dialog;
    }

    public static RescheduleDialogFragment getInstance() {
        return new RescheduleDialogFragment();
    }
}
