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
    public ArrayList<Integer> getIDListe(){
        columns=new String[]{"id"};

        cursor = dbRead.query("programmData ", columns, null, null, null, null,null);
        ArrayList<Integer> arrayList=new ArrayList<>();

        if(cursor!=null && cursor.getCount()>0) {
            while (cursor.moveToNext()) {
                arrayList.add(cursor.getInt(0));
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
    public void addPoints(ArrayList<PointG> arrayList, int id){
        //Zuerst alle Punkte mit dieser ID löschen
        selection = "pid = ?";
        selectionArgs=new String[]{String.valueOf(id)};
        dbWrite.delete("pointData", selection, selectionArgs);
        //Dann alle Punkte hinzufügen mit id als pid
        for(int i=0;i<arrayList.size();i++){
            values.clear();
            values.put("pid", id);
            values.put("geschwindigkeit",arrayList.get(i).getGeschwindigkeit());
            values.put("achse1",arrayList.get(i).getAxisOne());
            values.put("achse2",arrayList.get(i).getAxisTwo());
            values.put("achse3",arrayList.get(i).getAxisThree());
            values.put("achse4",arrayList.get(i).getAxisFour());
            values.put("achse5",arrayList.get(i).getAxisFive());
            values.put("achse6",arrayList.get(i).getAxisSix());
            values.put("delay",arrayList.get(i).getDelay());
            dbWrite.insert("pointData", null,values);
        }
    }
    public ArrayList<PointG> getPoints(int id){
        columns=new String[]{"geschwindigkeit","achse1","achse2","achse3","achse4","achse5","achse6","delay"};
        selection="pid = ?";
        selectionArgs=new String[]{String.valueOf(id)};

        cursor = dbRead.query("pointData ", columns, selection, selectionArgs, null, null,null);
        ArrayList<PointG> arrayList=new ArrayList<>();

        if(cursor!=null && cursor.getCount()>0) {
            while (cursor.moveToNext()) {
                int geschwindigkeit = cursor.getInt(0);
                int achse1 = cursor.getInt(1);
                int achse2 = cursor.getInt(2);
                int achse3 = cursor.getInt(3);
                int achse4 = cursor.getInt(4);
                int achse5 = cursor.getInt(5);
                int achse6 = cursor.getInt(6);
                int delay = cursor.getInt(7);

                PointG pg=new PointG(achse1, achse2, achse3, achse4, achse5, achse6, geschwindigkeit, delay);
                arrayList.add(pg);
            }
        }
        return arrayList;
    }
}
