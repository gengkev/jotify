package com.example.flashcards.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.EditText;

public class NewGroupFragment extends DialogFragment {
    public final String EXTRA_NAME = "com.example.flashcards.app.NAME";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setTitle(R.string.new_group_title)
                .setView(inflater.inflate(R.layout.dialog_new_group, null))
                .setPositiveButton(R.string.create, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText text = (EditText) getActivity().findViewById(R.id.new_group_name);
                        String value = text.getText().toString();

                        Intent intent = new Intent(getActivity(), GroupActivity.class);
                        intent.putExtra(EXTRA_NAME, value);
                        startActivity(intent);
                    }
                });

        // Create the AlertDialog object and return it
        return builder.create();
    }
}
