package com.example.flashcards.app;

public class Notecard {
    public int _id;
    public String path;
    public int category_id;
    public String caption;

    public String toString() {
        return "_id (" + _id + "), path (" + path + "), category_id (" + category_id + "), caption (" + caption + ")";
    }
}
