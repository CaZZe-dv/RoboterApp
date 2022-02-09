package com.example.bluetooth.programme.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.bluetooth.programme.erstellen.PointG;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
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


    /*
    public String[] getTableNames(){
        selection="type = ?";
        selectionArgs=new String[]{"table"};
        columns=new String[]{"name"};

        cursor = dbRead.query("sqlite_master ", columns, selection, selectionArgs, null, null,null);
        String[] names=new String[cursor.getCount()-1];//

        if(cursor!=null && cursor.getCount()>0) {
            cursor.moveToFirst();
            int i=0;
            while (cursor.moveToNext()) {
                String name=cursor.getString(0);
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
    private boolean newProgrammTable(String name){
        //Neue Tabelle einfügen
        if(!checkTableName(name)){
            return false;
        }
        dbWrite.execSQL("CREATE TABLE IF NOT EXISTS "+name+" (id INTEGER PRIMARY KEY, nummer INTEGER, geschwindigkeit INTEGER, greifer INTEGER, achse1 INTEGER, achse2 INTEGER, achse3 INTEGER, achse4 INTEGER, achse5 INTEGER, delay INTEGER)");
        return true;
    }
    public void newProgramm(String name, ArrayList<PointG> arrayListPG){
        
    }*/
    //Programme in Liste anzeigen
    public ArrayList<String> getProgrammListe(){
        columns=new String[]{"programmName"};

        cursor = dbRead.query("programmData ", columns, null, null, null, null,null);
        ArrayList<String> arrayList=new ArrayList<>();

        if(cursor!=null && cursor.getCount()>0) {
            while (cursor.moveToNext()) {
                arrayList.add(cursor.getString(0));
            }
        }
        return arrayList;
    }
    public void newProgramm(String name,String beschreibung){
        values.clear();
        values.put("programmName", name);
        values.put("beschreibung",beschreibung);
        dbWrite.insert("programmData", null,values);
    }



}
