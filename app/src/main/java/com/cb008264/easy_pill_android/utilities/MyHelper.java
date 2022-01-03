package com.cb008264.easy_pill_android;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyHelper  extends SQLiteOpenHelper {
    public MyHelper(Context context)
    {
        super(context,"NotesDB",null,1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE NOTES (_id INTEGER PRIMARY KEY AUTOINCREMENT, TITLE TEXT, DESCRIPTION TEXT)");
        db.execSQL("INSERT INTO NOTES (TITLE,DESCRIPTION) VALUES ('New Suppier Contact','+9478585622')");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
