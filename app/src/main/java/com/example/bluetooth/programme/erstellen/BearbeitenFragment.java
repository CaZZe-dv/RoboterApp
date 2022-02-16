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
import android.widget.SeekBar;

import com.example.bluetooth.R;
import com.example.bluetooth.programme.database.Connector;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class BearbeitenFragment extends Fragment implements SeekBar.OnSeekBarChangeListener, View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener{

    View view;
    Connector connector;

    ErstellenFragment fragmentErstellen;
    int id;//ID von zu bearbeitendem Programm

    SeekBar axisSix;
    SeekBar axisFive;
    SeekBar axisFour;
    SeekBar axisThree;
    SeekBar axisTwo;
    SeekBar axisOne;

    Button btnAddPoint;
    FloatingActionButton btnSaveProgramm;

    EditText textViewGeschwindigkeit;

    ListView listView;
    ArrayList<String> arrayList;
    ArrayList<PointG> pointList;
    ArrayAdapter<String> arrayAdapter;

    int defaultGeschwindigkeit = 10;
    int defaultDelay = 1000;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_bearbeiten, container, false);
        init();
        return view;
    }
    private void init(){
        connector=new Connector(view.getContext());
        fragmentErstellen=new ErstellenFragment();
        //Seekbars

        axisSix = view.findViewById(R.id.AxisSix_Programme);
        axisFive = view.findViewById(R.id.AxisFive_Programme);
        axisFour = view.findViewById(R.id.AxisFour_Programme);
        axisThree = view.findViewById(R.id.AxisThree_Programme);
        axisTwo = view.findViewById(R.id.AxisTwo_Programme);
        axisOne = view.findViewById(R.id.AxisOne_Programme);

        axisSix.setOnSeekBarChangeListener(this);
        axisFive.setOnSeekBarChangeListener(this);
        axisFour.setOnSeekBarChangeListener(this);
        axisThree.setOnSeekBarChangeListener(this);
        axisTwo.setOnSeekBarChangeListener(this);
        axisOne.setOnSeekBarChangeListener(this);

        //Buttons
        btnAddPoint = view.findViewById(R.id.btnAddPunkt);
        btnSaveProgramm = view.findViewById(R.id.btnSaveProgramm);

        btnAddPoint.setOnClickListener(this);
        btnSaveProgramm.setOnClickListener(this);
        //TextView
        textViewGeschwindigkeit = view.findViewById(R.id.textViewGeschwindigkeit);

        //Liste
        listView=(ListView)view.findViewById(R.id.listViewPunkte);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);

        pointList=connector.getPoints(id);
        arrayList=new ArrayList<>();
        for(int i=0;i<pointList.size();i++){
            arrayList.add("Punkt "+(i+1));
        }
        arrayAdapter=new ArrayAdapter(view.getContext(),android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(arrayAdapter);
        updateList();
    }
    public void setId(int id){
        this.id=id;
    }

    //Listen
    private void updateList(){
        arrayAdapter=new ArrayAdapter(view.getContext(),android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(arrayAdapter);
    }
    private void addListItem(PointG pg){
        int index=arrayList.size()+1;
        arrayList.add("Punkt "+index);
        pointList.add(pg);
        updateList();
    }
//SeekBar Listener
    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
    }
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if(seekBar.equals(axisSix)){
            //btConnector.write("6"+axisSix.getProgress());
            //writeConsole("6 Achse auf "+axisSix.getProgress()+" Grad");
        }
        if(seekBar.equals(axisFive)){
            //btConnector.write("5"+axisFive.getProgress());
            //writeConsole("5 Achse auf "+axisFive.getProgress()+" Grad");
        }
        if(seekBar.equals(axisFour)){
            //btConnector.write("4"+axisFour.getProgress());
            //writeConsole("4 Achse auf "+axisFour.getProgress()+" Grad");
        }
        if(seekBar.equals(axisThree)){
            //btConnector.write("3"+axisThree.getProgress());
            //writeConsole("3 Achse auf "+axisThree.getProgress()+" Grad");
        }
        if(seekBar.equals(axisTwo)){
            //btConnector.write("2"+axisTwo.getProgress());
            //writeConsole("2 Achse auf "+axisTwo.getProgress()+" Grad");
        }
        if(seekBar.equals(axisOne)){
            //btConnector.write("1"+axisOne.getProgress());
            //writeConsole("1 Achse auf "+axisOne.getProgress()+" Grad");
        }

    }
    //Button Listeners
    @Override
    public void onClick(View view) {
        if(view.equals(btnAddPoint)){
            System.out.println("HDHADJDA");
            //ausgewählte Achsenpositionen als Punkt hinzufügen
            Point p=new Point(axisOne.getProgress(),axisTwo.getProgress(),axisThree.getProgress(),axisFour.getProgress(),axisFive.getProgress(),axisSix.getProgress());
            PointG pg;
            if(textViewGeschwindigkeit.getText().toString().isEmpty()){
                pg=new PointG(p,defaultGeschwindigkeit, defaultDelay);
            }else{
                pg=new PointG(p,Integer.parseInt(textViewGeschwindigkeit.getText().toString()),defaultDelay);
            }
            addListItem(pg);
            //TODO: kleines Fenster vor finalem Hinzufügen aufrufen
        }
        if(view.equals(btnSaveProgramm)){
            //TODO: Save Programm
            connector.addPoints(pointList,id);
            getFragmentManager().beginTransaction().replace(R.id.fragmentLayout_programm,fragmentErstellen).commit();
        }
    }
    //Liste Listeners
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //Bei einfachem Klick können Infos angesehen werden
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        return false;
        //Bei halten kann das Item gelöscht werden
    }
}