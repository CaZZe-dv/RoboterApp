package com.example.bluetooth.programme.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class Connector {
    DBHelper dbHelper;
    SQLiteDatabase dbWrite;
    SQLiteDatabase dbRead;
    ContentValues values;
    Cursor cursor;
    String selection;
    String[] selectionArgs;
    String[] columns;

    public Connector(Context context){
        dbHelper=new DBHelper(context);
        dbWrite=dbHelper.getWritableDatabase();
        dbRead=dbHelper.getReadableDatabase();
        values=new ContentValues();
    }

    public boolean newProgramm(String name){
        //Neue Tabelle einfügen
        if(!checkTableName(name)){
            return false;
        }
        dbWrite.execSQL("CREATE TABLE IF NOT EXISTS "+name+" (id INTEGER PRIMARY KEY, nummer INTEGER, geschwindigkeit INTEGER, greifer INTEGER, achse1 INTEGER, achse2 INTEGER, achse3 INTEGER, achse4 INTEGER, achse5 INTEGER, delay INTEGER)");
        return true;
    }
    public String[] getTableNames(){
        selection="type = ?";
        selectionArgs=new String[]{"table"};
        columns=new String[]{"name"};

        cursor = dbRead.query("sqlite_master ", columns, selection, selectionArgs, null, null,null);
        String[] names=new String[cursor.getCount()-1];

        if(cursor!=null && cursor.getCount()>0) {
            cursor.moveToFirst();
            int i=0;
            while (cursor.moveToNext()) {
                names[i] = cursor.getString(0);
                i++;
            }
        }
        return names;
    }
    private boolean checkTableName(String name){
        String[] names=getTableNames();
        for(String elem:names){
            if(elem.toLowerCase().equals(name.toLowerCase())){
                return false;
            }
        }
        return true;
    }
    public void dropAllTables() {
        String tables[]=getTableNames();
        cursor = dbRead.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        for (String table : tables) {
            System.out.println("DROP TABLE " + table);
            dbWrite.execSQL("DROP TABLE " + table);
        }
    }
}
