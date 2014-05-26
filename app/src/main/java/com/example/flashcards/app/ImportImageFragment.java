package com.example.flashcards.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class ImportImageFragment extends DialogFragment {

    private OnSelectListener listener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            listener = (OnSelectListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnSelectListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.title_activity_import_image);

        builder.setItems(new String[]{getString(R.string.chooser_camera), getString(R.string.chooser_gallery)},
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onSelect(which);
                    }
                }
        );
        return builder.create();
    }

    static interface OnSelectListener {
        void onSelect(int which);
    }
}
