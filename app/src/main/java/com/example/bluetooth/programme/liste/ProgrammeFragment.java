package com.example.bluetooth.programme.liste;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.bluetooth.R;
import com.example.bluetooth.programme.ProgrammActivity;
import com.example.bluetooth.programme.database.Connector;
import com.example.bluetooth.programme.erstellen.PointG;
import com.example.bluetooth.programme.robot.BTConnector;
import com.example.bluetooth.steuerung.MainActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;


public class ProgrammeFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener{

    View view;

    Connector connector;

    AlertDialog dialog;
    AlertDialog.Builder dialogBuilder;

    int delProgramm;

    //Liste
    ListView listView;
    ArrayList<SpannableString> arrayList;
    ArrayList<Integer> idList;
    ArrayAdapter<SpannableString> arrayAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_programme, container, false);
        init();
        return view;
    }
    private void init(){
        //Connector
        connector=new Connector(view.getContext());
        //ListView
        initListView();
        updateListView();
    }
    private void initListView(){
        //Da kein Programmname doppelt verwendet werden kann, kann einfach der Name der Tabelle verwendet werden.
        listView=(ListView)view.findViewById(R.id.listview_programme);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);

        arrayList=new ArrayList<SpannableString>();
        idList=connector.getIDListe();

        arrayAdapter=new ArrayAdapter(view.getContext(),android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(arrayAdapter);
    }
    private void updateListView(){
        ArrayList<String> nameList=connector.getProgrammListe();
        ArrayList<String> beschreibungList=connector.getBeschreibungListe();
        arrayList=new ArrayList<SpannableString>();
        for(int i=0;i<nameList.size();i++){
            String name=nameList.get(i);
            String beschreibung=beschreibungList.get(i);
            SpannableString sString = new SpannableString(name+"\n"+beschreibung);
            sString.setSpan(new RelativeSizeSpan(1f), 0, name.length(), 0);
            sString.setSpan(new RelativeSizeSpan(0.7f), name.length(), name.length()+beschreibung.length()+1, 0);

            arrayList.add(sString);
        }
        idList=connector.getIDListe();
        arrayAdapter=new ArrayAdapter(view.getContext(),android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(arrayAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        ArrayList<PointG>points = connector.getPoints(idList.get(i));
        if(points.isEmpty()){
            dialogError("Programm enthält keinen Punkt");
        }else{
            BTConnector.playbackProgramm(points);
        }

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        delProgramm=idList.get(i);
        dialogDeleteProgramm();
        return false;
    }


    //AlertDialogs
    private void dialogError(String errorMessage){
        dialogBuilder = new AlertDialog.Builder(view.getContext());
        dialogBuilder.setMessage(errorMessage);
        dialogBuilder.setTitle("Fehler");
        dialogBuilder.setCancelable(true);

        dialogBuilder.setPositiveButton("OK",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        dialog = dialogBuilder.create();
        dialog.show();
    }
    //AlertDialogs
    private void dialogDeleteProgramm(){
        dialogBuilder = new AlertDialog.Builder(view.getContext());
        String name=connector.getProgrammName(delProgramm);
        dialogBuilder.setMessage("Programm "+name+" löschen?");
        dialogBuilder.setTitle("Programm löschen");
        dialogBuilder.setCancelable(true);

        dialogBuilder.setPositiveButton("OK",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                connector.deleteProgramm(delProgramm);
                updateListView();
                dialog.cancel();
            }
        });
        dialogBuilder.setNegativeButton("Abbruch",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //nur schließen
                dialog.cancel();
            }
        });

        dialog = dialogBuilder.create();
        dialog.show();
    }
}