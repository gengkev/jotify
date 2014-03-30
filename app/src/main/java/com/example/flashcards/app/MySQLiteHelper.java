package com.example.flashcards.app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.widget.Toast;
import android.content.Context;
import android.app.*;
import java.util.List;
import java.util.ArrayList;

public class MySQLiteHelper extends SQLiteOpenHelper{
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "database";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // create fresh books table
        this.onCreate(db);
    }

    private static final String[] COLUMNS = {"_id","path","category_id","caption"};

    String[] tableColumns = new String[] {
            "caption" };
    public String getNotecards(int id){

        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query("notecard", // a. table
                        null, // b. column names
                        null, // c. selections
                        null, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

      /*  COLUMNS,
                "_id = 1",
                new String[] { String.valueOf(id) }, */
        if (cursor != null)
            cursor.moveToFirst();

       return cursor.getString(1);

       // return "Hello";
    }

    public List<String> getCategories(int id, Context context){

        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query("notecard", // a. table
                        null, // b. column names
                        null, // c. selections
                        null, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

      /*  COLUMNS,
                "_id = 1",
                new String[] { String.valueOf(id) }, */
        if (cursor != null)
           cursor.moveToFirst();

       // Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, cursor.getString(4), duration);
        toast.show();

        List<String> categories = new ArrayList<String>();

        do{
        categories.add(cursor.getString(4));
        }while(cursor.moveToNext());




        return categories;

        // return "Hello";
    }


}
