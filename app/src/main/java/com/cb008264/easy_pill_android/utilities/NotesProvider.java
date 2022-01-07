package com.cb008264.easy_pill_android.utilities;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class NotesProvider extends ContentProvider {

    public static final String PROVIDER_NAME = "com.cb008264.easy_pill_android.utilities/NotesProvider";
    public static final String URL = "content://" + PROVIDER_NAME + "/NOTES";
    public static final Uri CONTENT_URI = Uri.parse(URL);
    public static final String _ID = "_id";
    public static final String NAME = "TITLE";
    public static final String DESCRIPTION = "DESCRIPTION";


    SQLiteDatabase db;

    @Override
    public boolean onCreate() {
        MyHelper helper = new MyHelper(getContext());
        db = helper.getWritableDatabase();
        if (db == null) {
            return false;
        } else {
            return true;
        }
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return db.query("NOTES", projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return "vnd.android.cursor.dir/vnd.example.notes";
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        db.insert("NOTES", null, values);
        getContext().getContentResolver().notifyChange(uri, null);
        return uri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count = db.delete("NOTES", selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;

    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count = db.update("NOTES", values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    void readAllData()
    {

    }

}
