package com.example.bluetooth.programme.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Connector {
    DBHelper dbHelper;
    SQLiteDatabase db;
    ContentValues values;
    Cursor cursor;
    String selection;
    String[] selectionArgs;
    String[] columns;

    public Connector(Context context){
        dbHelper=new DBHelper(context);
        db=dbHelper.getWritableDatabase();
        values=new ContentValues();
    }

}
