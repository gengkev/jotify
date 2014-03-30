package com.example.flashcards.app;

import android.content.ContentValues;
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
        String CREATE_NOTECARDS_TABLE = "CREATE TABLE notecard("
                + "_id INTEGER PRIMARY KEY, "
                + "path TEXT, "
                + "category_id INTEGER, "
                + "caption TEXT)";
        db.execSQL(CREATE_NOTECARDS_TABLE);

        String CREATE_CATEGORIES_TABLE = "CREATE TABLE category("
                + "_id INTEGER PRIMARY KEY, "
                + "title TEXT)";
        db.execSQL(CREATE_CATEGORIES_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS notecard");
        db.execSQL("DROP TABLE IF EXISTS category");
        // create fresh books table
        this.onCreate(db);
    }

    private static final String[] NOTECARD_COLUMNS = {"_id","path","category_id","caption"};
    private static final String[] CATEGORY_COLUMNS = {"_id", "title"};

    public List<Notecard> getNotecards(Context context, int category){

        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query("notecard", // a. table
                        null, // b. column names
                        "_id=?", // c. selections
                        new String[] { String.valueOf(category) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

      /*  COLUMNS,
                "_id = 1",
                new String[] { String.valueOf(id) }, */

        List<Notecard> notecards = new ArrayList<Notecard>();

         if (cursor.moveToFirst()) {
            do {
                Notecard n = new Notecard();
                n._id = cursor.getInt(0);
                n.caption = cursor.getString(1);
                n.category_id = cursor.getInt(2);
                n.path = cursor.getString(3);

                notecards.add(n);

                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, n.toString(), duration);
                toast.show();

            } while(cursor.moveToNext());
         }

        return notecards;

        // return "Hello";
    }

    public List<Category> getCategories(Context context){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =
                db.query("category", // a. table
                        null, // b. column names
                        null, // c. selections
                        null, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        List<Category> categories = new ArrayList<Category>();

        if (cursor.moveToFirst()) {
            do {
                Category c = new Category();
                c._id = cursor.getInt(0);
                c.title = cursor.getString(1);

                categories.add(c);

                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, c.toString(), duration);
                toast.show();

            } while(cursor.moveToNext());
        }

        return categories;

        // return "Hello";
    }

    public Category getCategory(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("category", null, "_id=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Category c = new Category();
        c._id = cursor.getInt(0);
        c.title = cursor.getString(1);

        return c;
    }

    public long addNotecard(Notecard notecard) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("_id", notecard._id);
        values.put("path", notecard.path);
        values.put("category_id", notecard.category_id);
        values.put("caption", notecard.caption);

        // Inserting Row
        long id = db.insert("notecard", null, values);
        db.close(); // Closing database connection

        notecard._id = (int) id;
        return id;
    }

    public long addCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("title", category.title);

        // Inserting Row
        long id = db.insert("category", null, values);
        db.close(); // Closing database connection

        category._id = (int) id;
        return id;
    }


}
