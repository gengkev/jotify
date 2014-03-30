package com.example.flashcards.app;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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
    public static final String EXTRA_GROUP_ID = "com.example.flashcards.app.GROUP_ID";

    private ListView listView;
    private Category c;
    private List<Notecard> notecards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        Intent intent = getIntent();
        int data = intent.getIntExtra(GroupListActivity.EXTRA_ID, -1);

        MySQLiteHelper db = new MySQLiteHelper(this);
        c = db.getCategory(data);
        notecards = db.getNotecards(this, c._id);
        getActionBar().setTitle(c.title);


        listView = (ListView) findViewById(R.id.listView);

        List<String> titles = new ArrayList<String>(notecards.size());
        for (Notecard n : notecards) {
            titles.add(n.caption);
        }

        ListAdapter myListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, titles);
        listView.setAdapter(myListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                Notecard n = notecards.get(pos);

                // TODO: implement TestingActivity
                //Intent intent = new Intent(GroupActivity.this, TestingActivity.class);
                //intent.putExtra(GroupListActivity.EXTRA_ID, n._id);
                //startActivity(intent);
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.action_add_notecard:

                Intent intent = new Intent(this, AddNotecardActivity.class);
                intent.putExtra(GroupActivity.EXTRA_GROUP_ID, c._id);
                startActivity(intent);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
