package com.example.flashcards.app;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.Button;
import android.view.*;

public class LaunchActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.launch, menu);
        return true;
    }

<<<<<<< HEAD
    public void LaunchApp(View view){
        Intent intent = new Intent(LaunchActivity.this, TestingActivity.class);
        LaunchActivity.this.startActivity(intent);
=======
    public void launchApp(View view){
        Intent intent = new Intent(this, GroupListActivity.class);
        startActivity(intent);
>>>>>>> b6d69355b4425f1bd308b442fa31decb09d0d5db
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
