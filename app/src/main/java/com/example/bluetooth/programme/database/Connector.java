package com.example.bluetooth.programme.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.bluetooth.programme.erstellen.PointG;

import java.util.ArrayList;

public class Connector {

    private final DBHelper dbHelper;
    //beschreibbare Datenbank
    private final SQLiteDatabase dbWrite;
    //lesbare Datenbank
    private final SQLiteDatabase dbRead;
    private final ContentValues values;
    private Cursor cursor;
    private String selection;
    private String[] selectionArgs;
    private String[] columns;

    public Connector(Context context){
        dbHelper=new DBHelper(context);
        dbWrite=dbHelper.getWritableDatabase();
        dbRead=dbHelper.getReadableDatabase();
        values=new ContentValues();
    }

    //Gibt den Namen eines einzelnen Programms zurück
    public String getProgrammName(int id){
        selection="id = ?";
        selectionArgs=new String[]{String.valueOf(id)};

        cursor = dbRead.query("programmData ", null, selection, selectionArgs, null, null,null);
        cursor.moveToFirst();
        String name=cursor.getString(cursor.getColumnIndexOrThrow("programmName"));
        return name;
    }

    //Gibt die Beschreibung eines einzelnen Programms zurück
    public String getProgrammBeschreibung(int id){
        selection="id = ?";
        selectionArgs=new String[]{String.valueOf(id)};

        cursor = dbRead.query("programmData ", null, selection, selectionArgs, null, null,null);
        cursor.moveToFirst();
        String beschreibung=cursor.getString(cursor.getColumnIndexOrThrow("beschreibung"));
        return beschreibung;
    }

    //Ändert den Namen eines Programmes
    public void alterProgrammName(int id, String name){
        values.clear();
        values.put("programmName", name);

        selection="id = ?";
        selectionArgs=new String[]{String.valueOf(id)};

        dbWrite.update("programmData",values,selection,selectionArgs);
    }

    //Ändert die Beschreibung eines Programmes
    public void alterProgrammBeschreibung(int id, String beschreibung){
        values.clear();
        values.put("beschreibung", beschreibung);

        String selection="id = ?";
        String[] selectionArgs={String.valueOf(id)};

        dbWrite.update("programmData",values,selection,selectionArgs);
    }

    //Gibt eine ArrayList mit allen Programmnamen zurück
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

    //Gibt eine ArrayList mit allen Programmbeschreibungen zurück
    public ArrayList<String> getBeschreibungListe(){
        columns=new String[]{"beschreibung"};

        cursor = dbRead.query("programmData ", columns, null, null, null, null,null);
        ArrayList<String> arrayList=new ArrayList<>();

        if(cursor!=null && cursor.getCount()>0) {
            while (cursor.moveToNext()) {
                arrayList.add(cursor.getString(0));
            }
        }
        return arrayList;
    }

    //Gibt eine ArrayList mit allen Programmids zurück
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

    //neues Programm einfügen
    public void newProgramm(String name,String beschreibung){
        values.clear();
        values.put("programmName", name);
        values.put("beschreibung",beschreibung);
        dbWrite.insert("programmData", null,values);
    }

    //Fügt neue (und löscht dabei die alten) Punkte zur id eines Programmes hinzu
    public void addPoints(ArrayList<PointG> arrayList, int id){
        //Zuerst alle Punkte mit dieser id löschen
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

    //Gibte eine Arraylist mit allen Punkten eines Programms zurück
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

    //Löscht ein Programm (und alle Punkte dieses Programmes)
    public void deleteProgramm(int id){
        //Aus programmDate löschen
        selection="id = ?";
        selectionArgs= new String[]{String.valueOf(id)};
        dbWrite.delete("programmData",selection,selectionArgs);

        //Aus pointData löschen
        selection="pid = ?";
        dbWrite.delete("pointData",selection,selectionArgs);
    }
}
