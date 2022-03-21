package com.example.bluetooth.programme.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    //aktuelle Version der Datenbank
    public static final int version=3;
    //Name der Datenbank
    public static final String name="Programme.db";

    public DBHelper(Context context){
        //Datenbank erstellen
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Erstellen der einzelnen Tabellen
        db.execSQL("CREATE TABLE IF NOT EXISTS programmData (id INTEGER PRIMARY KEY, programmName TEXT, beschreibung TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS pointData (id INTEGER PRIMARY KEY, pid INTEGER, geschwindigkeit INTEGER, achse1 INTEGER, achse2 INTEGER, achse3 INTEGER, achse4 INTEGER, achse5 INTEGER, achse6 INTEGER, delay INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Bei Versionsupgrade werden die Tabellen gelöscht
        db.execSQL("DROP TABLE IF EXISTS programmData");
        db.execSQL("DROP TABLE IF EXISTS pointData");
        //und die onCreate Methode aufgerufen, um die Tabellen neu zu erstellen
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Bei Versionsdowngrade passiert das gleich wie beim Upgrade
        onUpgrade(db, oldVersion, newVersion);
    }
}
