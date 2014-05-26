package com.example.flashcards.app;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
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

public class GroupListActivity extends Activity {
    public static final String EXTRA_GROUP_ID = "com.example.flashcards.app.GROUP_ID";
    public static final String EXTRA_NOTECARD_ID = "com.example.flashcards.app.NOTECARD_ID";

    private ListView listView;
    private List<Category> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);

        MySQLiteHelper db = new MySQLiteHelper(this);
        listView = (ListView) findViewById(R.id.GroupList);

        // Load categories from db
        categories = db.getCategories();

        // Display categories in listView via an ArrayAdapter
        List<String> titles = new ArrayList<String>(categories.size());
        for (Category c : categories) {
            titles.add(c.title);
        }

        ListAdapter myListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, titles);
        listView.setAdapter(myListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                // Create a GroupActivity to display the notecard
                Category c = categories.get(pos);
                Intent intent = new Intent(GroupListActivity.this, GroupActivity.class);
                intent.putExtra(EXTRA_GROUP_ID, c._id);
                startActivity(intent);
            }
        });
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
                // Show Dialog to create a new group
                DialogFragment dialog = new NewGroupFragment();
                dialog.show(getFragmentManager(), GroupListActivity.class.toString());
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
