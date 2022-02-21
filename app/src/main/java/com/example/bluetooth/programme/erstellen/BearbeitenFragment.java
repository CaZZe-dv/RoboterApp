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
import android.widget.TextView;

import com.example.bluetooth.R;
import com.example.bluetooth.programme.database.Connector;
import com.example.bluetooth.programme.robot.BTConnector;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class BearbeitenFragment extends Fragment implements SeekBar.OnSeekBarChangeListener, View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener{

    View view;
    Connector connector;
    BTConnector btConnector;

    ErstellenFragment fragmentErstellen;
    int id;//ID von zu bearbeitendem Programm

    SeekBar axisSix;
    SeekBar axisFive;
    SeekBar axisFour;
    SeekBar axisThree;
    SeekBar axisTwo;
    SeekBar axisOne;

    Button btnAddPoint;
    int btnAddPointState;
    int editPoint;
    FloatingActionButton btnSaveProgramm;

    EditText textViewGeschwindigkeit;
    TextView textViewEdit;

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
        btConnector=new BTConnector();
        fragmentErstellen=new ErstellenFragment();
        //Seekbars

        axisSix = view.findViewById(R.id.AxisSix_Programme);
        axisFive = view.findViewById(R.id.AxisFive_Programme);
        axisFour = view.findViewById(R.id.AxisFour_Programme);
        axisThree = view.findViewById(R.id.AxisThree_Programme);
        axisTwo = view.findViewById(R.id.AxisTwo_Programme);
        axisOne = view.findViewById(R.id.AxisOne_Programme);

        btConnector.homePosition();

        axisSix.setOnSeekBarChangeListener(this);
        axisFive.setOnSeekBarChangeListener(this);
        axisFour.setOnSeekBarChangeListener(this);
        axisThree.setOnSeekBarChangeListener(this);
        axisTwo.setOnSeekBarChangeListener(this);
        axisOne.setOnSeekBarChangeListener(this);
        disableSeekbars();

        //Buttons
        btnAddPoint = view.findViewById(R.id.btnAddPunkt);
        btnAddPointState=1;
        btnSaveProgramm = view.findViewById(R.id.btnSaveProgramm);

        btnAddPoint.setOnClickListener(this);
        btnSaveProgramm.setOnClickListener(this);
        //TextView
        textViewGeschwindigkeit = view.findViewById(R.id.textViewGeschwindigkeit);
        textViewEdit = view.findViewById(R.id.textViewEdit);
        textViewEdit.setText("Kein Punkt ausgewählt...");

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
    private void updateListItem(PointG pg){
        int index=editPoint;
        pointList.set(index,pg);
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
            switch (btnAddPointState){
                case 1:
                    applyCurrentState();

                    enableSeekbars();
                    btnAddPointState=2;
                    btnAddPoint.setText("Punkt hinzufügen");
                    textViewEdit.setText("Neuen Punkt hinzufügen");
                    break;
                case 2:
                    Point pNew=new Point(axisOne.getProgress(),axisTwo.getProgress(),axisThree.getProgress(),axisFour.getProgress(),axisFive.getProgress(),axisSix.getProgress());
                    PointG pgNew;
                    if(textViewGeschwindigkeit.getText().toString().isEmpty()){
                        pgNew=new PointG(pNew,defaultGeschwindigkeit, defaultDelay);
                    }else{
                        pgNew=new PointG(pNew,Integer.parseInt(textViewGeschwindigkeit.getText().toString()),defaultDelay);
                    }
                    addListItem(pgNew);

                    disableSeekbars();
                    btnAddPointState=1;
                    btnAddPoint.setText("Neuer Punkt");
                    textViewEdit.setText("Keinen Punkt ausgewählt");
                    break;
                case 3:
                    Point pEdit=new Point(axisOne.getProgress(),axisTwo.getProgress(),axisThree.getProgress(),axisFour.getProgress(),axisFive.getProgress(),axisSix.getProgress());
                    PointG pgEdit;
                    if(textViewGeschwindigkeit.getText().toString().isEmpty()){
                        pgEdit=new PointG(pEdit,defaultGeschwindigkeit, defaultDelay);
                    }else{
                        pgEdit=new PointG(pEdit,Integer.parseInt(textViewGeschwindigkeit.getText().toString()),defaultDelay);
                    }
                    updateListItem(pgEdit);

                    disableSeekbars();
                    btnAddPointState=1;
                    btnAddPoint.setText("Neuer Punkt");
                    textViewEdit.setText("Keinen Punkt ausgewählt");
                    break;
            }
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
        //Bei einfachem Klick können Infos angesehen werden und die Seekbars werden auf die Position gesetzt, auf die sie bei diesem Punkt sind
        PointG point=pointList.get(i);
        axisOne.setProgress(point.getAxisOne());
        axisTwo.setProgress(point.getAxisTwo());
        axisThree.setProgress(point.getAxisThree());
        axisFour.setProgress(point.getAxisFour());
        axisFive.setProgress(point.getAxisFive());
        axisSix.setProgress(point.getAxisSix());
        Point p=new Point(axisOne.getProgress(),axisTwo.getProgress(),axisThree.getProgress(),axisFour.getProgress(),axisFive.getProgress(),axisSix.getProgress());
        btConnector.goTo(p);

        btnAddPointState=3;
        editPoint=i;
        textViewEdit.setText("Punkt "+(i+1)+" bearbeiten");
        btnAddPoint.setText("Änderungen übernehmen");
        enableSeekbars();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        return false;
        //Bei halten kann das Item gelöscht werden
    }
    private void applyCurrentState(){
        //Seekbars auf den Wert setzen, auf den die Servos tatsächlich sind
        Point curPosition=btConnector.getCurPosition();
        axisOne.setProgress(curPosition.getAxisOne());
        axisTwo.setProgress(curPosition.getAxisTwo());
        axisThree.setProgress(curPosition.getAxisThree());
        axisFour.setProgress(curPosition.getAxisFour());
        axisFive.setProgress(curPosition.getAxisFive());
        axisSix.setProgress(curPosition.getAxisSix());
    }
    private void enableSeekbars(){
        axisOne.setEnabled(true);
        axisTwo.setEnabled(true);
        axisThree.setEnabled(true);
        axisFour.setEnabled(true);
        axisFive.setEnabled(true);
        axisSix.setEnabled(true);

    }
    private void disableSeekbars(){
        axisOne.setEnabled(false);
        axisTwo.setEnabled(false);
        axisThree.setEnabled(false);
        axisFour.setEnabled(false);
        axisFive.setEnabled(false);
        axisSix.setEnabled(false);
    }
}