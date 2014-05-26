package com.example.flashcards.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class NewGroupFragment extends DialogFragment {
    private EditText text;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View v = inflater.inflate(R.layout.dialog_new_group, null);
        builder.setView(v);
        builder.setTitle(R.string.new_group_title);

        text = (EditText) v.findViewById(R.id.new_group_name);

        builder.setPositiveButton(R.string.create, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String value = text.getText().toString();

                Category c = new Category();
                c.title = value;

                MySQLiteHelper db = new MySQLiteHelper(getActivity());
                db.addCategory(c);
                Log.i(NewGroupFragment.class.getName(), "new category: " + c);

                Intent intent = new Intent(getActivity(), GroupActivity.class);
                intent.putExtra(GroupListActivity.EXTRA_GROUP_ID, c._id);
                startActivity(intent);
            }
        });

        // Create the AlertDialog object and return it
        return builder.create();
    }
}
