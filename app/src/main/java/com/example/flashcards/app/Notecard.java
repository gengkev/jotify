package com.example.flashcards.app;

public class Notecard {
    public int _id;
    public int category_id;
    public String caption;
    public String path1;
    public String path2;

    public String toString() {
        return "_id (" + _id + "), category_id (" + category_id + "), caption (" + caption + "), "
                + "path1 (" + path1 + "), path2 (" + path2 + ")";
    }
}
