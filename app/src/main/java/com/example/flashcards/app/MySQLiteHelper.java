package com.example.flashcards.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MySQLiteHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "database";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_NOTECARDS_TABLE = "CREATE TABLE notecard("
                + "_id INTEGER PRIMARY KEY, "
                + "category_id INTEGER, "
                + "caption TEXT, "
                + "path1 TEXT, "
                + "path2 TEXT)";
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

        // create fresh table
        this.onCreate(db);
    }

    public List<Notecard> getNotecards(int category) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor =
                db.query("notecard", // a. table
                        null, // b. column names
                        "category_id=?", // c. selections
                        new String[] { String.valueOf(category) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        List<Notecard> notecards = new ArrayList<Notecard>();

        if (cursor.moveToFirst()) {
            do {
                Notecard n = new Notecard();
                n._id = cursor.getInt(0);
                n.category_id = cursor.getInt(1);
                n.caption = cursor.getString(2);
                n.path1 = cursor.getString(3);
                n.path2 = cursor.getString(4);

                notecards.add(n);

            } while (cursor.moveToNext());
        }

        return notecards;
    }

    public List<Category> getCategories() {
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

            } while (cursor.moveToNext());
        }

        return categories;
    }

    public Notecard getNotecard(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("notecard", null, "_id=?",
                new String[] { String.valueOf(id) }, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            Notecard n = new Notecard();
            n._id = cursor.getInt(0);
            n.category_id = cursor.getInt(1);
            n.caption = cursor.getString(2);
            n.path1 = cursor.getString(3);
            n.path2 = cursor.getString(4);

            return n;
        } else {
            Log.e(MySQLiteHelper.class.getName(), "Notecard with id " + id + " not found");
            return null;
        }
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
        values.put("category_id", s(notecard.category_id + ""));
        values.put("caption", s(notecard.caption));
        values.put("path1", s(notecard.path1));
        values.put("path2", s(notecard.path2));

        // Inserting Row
        long id = db.insert("notecard", null, values);
        db.close(); // Closing database connection

        notecard._id = (int) id;
        return id;
    }

    public void editNotecard(Notecard notecard) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("category_id", s(notecard.category_id + ""));
        values.put("caption", s(notecard.caption));
        values.put("path1", s(notecard.path1));
        values.put("path2", s(notecard.path2));

        db.update("notecard", values, "_id = ?",
                new String[] { String.valueOf(notecard._id) });

    }

    public long addCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("title", s(category.title));

        // Inserting Row
        long id = db.insertOrThrow("category", null, values);
        db.close(); // Closing database connection

        category._id = (int) id;
        return id;
    }

    private String s(String s) {
        return (s == null || s.isEmpty() ? " " : s);
    }
}
