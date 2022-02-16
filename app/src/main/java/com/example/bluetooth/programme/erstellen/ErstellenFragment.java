package com.example.bluetooth.programme.erstellen;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.bluetooth.R;
import com.example.bluetooth.programme.database.Connector;

import java.util.ArrayList;

public class ErstellenFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener{

    View view;

    BearbeitenFragment fragmentBearbeiten;

    Connector connector;

    Button btnAddProgramm;

    EditText textViewProgrammName;
    EditText textViewBeschreibung;

    ListView listView;
    ArrayList<String> arrayList;
    ArrayList<Integer> idList;
    ArrayAdapter<String> arrayAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_erstellen, container, false);
        init();
        return view;
    }
    private void init(){
        connector=new Connector(view.getContext());
        fragmentBearbeiten=new BearbeitenFragment();

        //Buttons
        btnAddProgramm = view.findViewById(R.id.btnAddProgramm);

        btnAddProgramm.setOnClickListener(this);
        //TextView
        textViewProgrammName = view.findViewById(R.id.textViewProgrammname);
        textViewBeschreibung = view.findViewById(R.id.textViewProgrammBeschreibung);
        //Liste
        listView=(ListView)view.findViewById(R.id.listViewProgramme);
        listView.setOnItemClickListener(this);

        arrayList=new ArrayList<String>();
        idList=new ArrayList<Integer>();
        arrayAdapter=new ArrayAdapter(view.getContext(),android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(arrayAdapter);
        updateList();
    }

    //Listen
    private void updateList(){
        arrayList = connector.getProgrammListe();
        idList = connector.getIDListe();

        arrayAdapter=new ArrayAdapter(view.getContext(),android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(arrayAdapter);
    }

    //Button Listeners
    @Override
    public void onClick(View view) {
        if(view.equals(btnAddProgramm)){
            String name=textViewProgrammName.getText().toString();
            String beschreibung=textViewBeschreibung.getText().toString();
            connector.newProgramm(name,beschreibung);
            idList=connector.getIDListe();
            int id=idList.get(idList.size()-1);
            switchFrag(id);
        }
    }
    //Liste Listeners
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //Bei einfachem Klick wird das ausgewählte Programm bearbeitet

        int id=idList.get(i);
        switchFrag(id);
    }
    private void switchFrag(int id){
        getFragmentManager().beginTransaction().replace(R.id.fragmentLayout_programm,fragmentBearbeiten).commit();
        fragmentBearbeiten.setId(id);
    }
}