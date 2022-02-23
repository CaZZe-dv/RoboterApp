package com.example.bluetooth.programme.liste;

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
import com.example.bluetooth.programme.database.Connector;
import com.example.bluetooth.programme.erstellen.PointG;
import com.example.bluetooth.programme.robot.BTConnector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;


public class ProgrammeFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener{

    View view;

    Connector connector;
    BTConnector btConnector;

    //Liste
    ListView listView;
    ArrayList<SpannableString> arrayList;
    ArrayList<Integer> idList;
    ArrayAdapter arrayAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_programme, container, false);
        init();
        return view;
    }
    private void init(){
        //Connector
        connector=new Connector(view.getContext());
        btConnector=new BTConnector();
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
        btConnector.playbackProgramm(points);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        return false;
    }
}