package com.example.flashcards.app;

import android.view.Menu;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import java.util.List;
import com.example.flashcards.app.MySQLiteHelper;
import android.app.Activity;
import android.content.Context;
import android.widget.Toast;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.*;
import android.widget.ArrayAdapter;
import java.util.*;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.support.v7.app.ActionBar;

public class GroupListActivity extends Activity{
    ListView listView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);

        MySQLiteHelper db = new MySQLiteHelper(this);

        // get all books

       /* Context context = getApplicationContext();
        CharSequence text = db.getNotecards(1);
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();*/

        //db.getCategories(1, getApplicationContext());

        ListView myListView = (ListView) findViewById(R.id.GroupList);
        ArrayList<String> myStringArray1 =  new ArrayList<String>();
        myStringArray1.add("something");
        ListAdapter myListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, db.getCategories(1, getApplicationContext()));
        myListView.setAdapter(myListAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.group_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.action_add_group:

                DialogFragment dialog = new NewGroupFragment();
                dialog.show(getFragmentManager(), GroupListActivity.class.toString());

                return true;
            case R.id.addphoto:
                Intent intent = new Intent(this, ImportImageActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
