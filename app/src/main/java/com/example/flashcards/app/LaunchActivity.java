package com.example.flashcards.app;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class LaunchActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
    }

    public void LaunchApp(View view){
        Intent intent = new Intent(LaunchActivity.this, GroupListActivity.class);
        LaunchActivity.this.startActivity(intent);
    }

}
