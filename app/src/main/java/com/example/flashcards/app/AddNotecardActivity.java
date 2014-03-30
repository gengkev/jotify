package com.example.flashcards.app;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class AddNotecardActivity extends ActionBarActivity {
    public static final String NOTECARD_ID = "com.example.flashcards.app.NOTECARD_ID";

    private MySQLiteHelper db;
    private Category cParent;
    private Notecard notecard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notecard);

        Intent intent = getIntent();
        int id = intent.getIntExtra(GroupActivity.EXTRA_GROUP_ID, -1);

        db = new MySQLiteHelper(this);
        cParent = db.getCategory(id);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_notecard, menu);
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
            case R.id.add_notecard_done:
                // TODO save notecard

                db.addNotecard(notecard);

                // TODO: implement TestingActivity
                /*
                Intent intent = new Intent(this, TestingActivity.class);
                intent.putExtra(NOTECARD_ID, notecard._id);
                startActivity(intent);
                */

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
