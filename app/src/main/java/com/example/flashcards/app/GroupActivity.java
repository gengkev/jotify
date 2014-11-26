package com.example.flashcards.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class GroupActivity extends ActionBarActivity {
    private ListView listView;

    private Category category;
    private List<Notecard> notecards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        MySQLiteHelper db = new MySQLiteHelper(this);
        listView = (ListView) findViewById(R.id.listView);

        // Get category ID
        Intent intent = getIntent();
        int data = intent.getIntExtra(GroupListActivity.EXTRA_GROUP_ID, -1);

        // Try the Bundle
        if (data == -1 && savedInstanceState != null) {
            data = savedInstanceState.getInt("category-id", -1);
        }

        // Failed
        if (data == -1) {
            // Go back up to GroupListActivity
            // http://stackoverflow.com/a/20306670/1435804
            Intent up = new Intent(this, GroupListActivity.class);
            up.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            NavUtils.navigateUpTo(this, up);
            return;
        }

        // Load category and notecards from database
        category = db.getCategory(data);
        notecards = db.getNotecards(category._id);

        // Set title
        getActionBar().setTitle(category.title);


        // Display notecards in listView via an ArrayAdapter
        List<String> titles = new ArrayList<String>(notecards.size());
        for (Notecard n : notecards) {
            titles.add(n.caption);
        }

        ListAdapter myListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, titles);
        listView.setAdapter(myListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                // Create a NotecardActivity to display the notecard
                Notecard notecard = notecards.get(pos);
                Intent intent = new Intent(GroupActivity.this, NotecardActivity.class);
                intent.putExtra(GroupListActivity.EXTRA_NOTECARD_ID, notecard._id);
                intent.putExtra(GroupListActivity.EXTRA_GROUP_ID, category._id);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.group, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

            case R.id.action_settings:
                return true;

            case R.id.action_add_notecard:
                // Create an AddNotecardActivity to add a notecard
                Intent intent = new Intent(this, AddNotecardActivity.class);
                intent.putExtra(GroupListActivity.EXTRA_GROUP_ID, category._id);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("category-id", category._id);
    }
}
