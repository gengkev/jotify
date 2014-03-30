package com.example.flashcards.app;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
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


public class GroupListActivity extends ActionBarActivity {

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
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
