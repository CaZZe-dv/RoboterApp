package com.example.bluetooth.programme.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION=3;
    public static final String DATABASE_NAME="Programme.db";
    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS programmData (id INTEGER PRIMARY KEY, programmName TEXT, beschreibung TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS pointData (id INTEGER PRIMARY KEY, pid INTEGER, geschwindigkeit INTEGER, achse1 INTEGER, achse2 INTEGER, achse3 INTEGER, achse4 INTEGER, achse5 INTEGER, achse6 INTEGER, delay INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS programmData");
        db.execSQL("DROP TABLE IF EXISTS pointData");
        onCreate(db);
    }
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
