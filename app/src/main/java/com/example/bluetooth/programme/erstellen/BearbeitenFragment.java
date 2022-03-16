package com.example.bluetooth.programme.erstellen;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.ParcelUuid;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.bluetooth.R;
import com.example.bluetooth.programme.database.Connector;
import com.example.bluetooth.programme.einstellungen.Console;
import com.example.bluetooth.programme.robot.BTConnector;
import com.example.bluetooth.steuerung.ControllerChoose;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class BearbeitenFragment extends Fragment implements SeekBar.OnSeekBarChangeListener, View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener{

    View view;
    Connector connector;

    ErstellenFragment fragmentErstellen;
    BearbeitenFragmentJoystick fragmentBearbeitenJoystick;
    int idProgramm;//ID von zu bearbeitendem Programm
    String nameProgramm;//Name des Programms
    ArrayList<PointG> pointListOriginal;

    SeekBar axisSix;
    SeekBar axisFive;
    SeekBar axisFour;
    SeekBar axisThree;
    SeekBar axisTwo;
    SeekBar axisOne;
    SeekBar axisGeschwindigkeit;

    AlertDialog dialog;
    AlertDialog.Builder dialogBuilder;

    Button btnAddPoint;
    int btnAddPointState;
    int editPoint;
    FloatingActionButton btnSaveProgramm;
    ImageButton btnBack;
    ImageButton btnHome;
    ImageButton btnSleep;
    ImageButton btnSwitch;

    TextView textViewName;
    EditText textViewDelay;

    ListView listView;
    ArrayList<String> arrayList;
    ArrayList<PointG> pointList;
    ArrayAdapter<String> arrayAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_bearbeiten, container, false);
        init();
        return view;
    }
    private void init(){
        connector=new Connector(view.getContext());
        fragmentErstellen=new ErstellenFragment();
        fragmentBearbeitenJoystick=new BearbeitenFragmentJoystick();
        //Seekbars
        //Seekbars

        axisSix = view.findViewById(R.id.AxisSix_Programme);
        axisFive = view.findViewById(R.id.AxisFive_Programme);
        axisFour = view.findViewById(R.id.AxisFour_Programme);
        axisThree = view.findViewById(R.id.AxisThree_Programme);
        axisTwo = view.findViewById(R.id.AxisTwo_Programme);
        axisOne = view.findViewById(R.id.AxisOne_Programme);
        axisGeschwindigkeit = view.findViewById(R.id.AxisGeschwindigkeit_Programme);

        axisSix.setOnSeekBarChangeListener(this);
        axisFive.setOnSeekBarChangeListener(this);
        axisFour.setOnSeekBarChangeListener(this);
        axisThree.setOnSeekBarChangeListener(this);
        axisTwo.setOnSeekBarChangeListener(this);
        axisOne.setOnSeekBarChangeListener(this);
        axisGeschwindigkeit.setOnSeekBarChangeListener(this);

        //Buttons
        btnAddPoint = view.findViewById(R.id.btnAddPunkt);
        btnAddPointState=1;
        btnSaveProgramm = view.findViewById(R.id.btnSaveProgramm);
        btnBack = view.findViewById(R.id.btnBearbeitenBack);
        btnHome = view.findViewById(R.id.btnBearbeitenHome);
        btnSleep = view.findViewById(R.id.btnBearbeitenSleep);
        btnSwitch = view.findViewById(R.id.btnBearbeitenSwitch);

        btnAddPoint.setOnClickListener(this);
        btnSaveProgramm.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnHome.setOnClickListener(this);
        btnSleep.setOnClickListener(this);
        btnSwitch.setOnClickListener(this);
        //TextView
        textViewDelay = view.findViewById(R.id.textViewDelay);
        textViewName = view.findViewById(R.id.textViewBearbeitenName);
        textViewName.setText(nameProgramm);

        disableInput();

        //Liste
        listView=(ListView)view.findViewById(R.id.listViewPunkte);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);

        pointList=connector.getPoints(idProgramm);
        arrayList=new ArrayList<>();
        for(int i=0;i<pointList.size();i++){
            PointG pg=pointList.get(i);
            int index=i+1;
            String pName=generatePointName(pg,index);
            arrayList.add(pName);
        }
        arrayAdapter=new ArrayAdapter(view.getContext(),android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(arrayAdapter);
        updateList();

        applyCurrentState();
    }
    public void setId(int id){
        this.idProgramm=id;
    }
    public void setProgrammName(String programmName){
        this.nameProgramm=programmName;
    }
    public void setPointListOriginal(ArrayList<PointG> pointList){
        this.pointListOriginal=pointList;
    }

    //Listen
    private void updateList(){
        arrayList=new ArrayList<>();
        for(int i=0;i<pointList.size();i++){
            PointG pg=pointList.get(i);
            int index=i+1;
            String pName=generatePointName(pg,index);
            arrayList.add(pName);
        }
        arrayAdapter=new ArrayAdapter(view.getContext(),android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(arrayAdapter);
    }
    private void addListItem(PointG pg, int index){
        if(index==pointList.size()){
            pointList.add(pg);
        }else{
            pointList.add(index,pg);
        }
        updateList();
    }
    private void updateListItem(PointG pg){
        int index=editPoint;
        pointList.set(index,pg);
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
            BTConnector.sendMessage("6", String.valueOf(axisSix.getProgress()));
        }
        if(seekBar.equals(axisFive)){
            BTConnector.sendMessage("5", String.valueOf(axisFive.getProgress()));
        }
        if(seekBar.equals(axisFour)){
            BTConnector.sendMessage("4", String.valueOf(axisFour.getProgress()));
        }
        if(seekBar.equals(axisThree)){
            BTConnector.sendMessage("3", String.valueOf(axisThree.getProgress()));
        }
        if(seekBar.equals(axisTwo)){
            BTConnector.sendMessage("2", String.valueOf(axisTwo.getProgress()));
        }
        if(seekBar.equals(axisOne)){
            BTConnector.sendMessage("1", String.valueOf(axisOne.getProgress()));
        }if(seekBar.equals(axisGeschwindigkeit)){
            BTConnector.sendMessage("d", String.valueOf(getGeschwindigkeit()));
        }

    }
    //Button Listeners
    @Override
    public void onClick(View view) {
        if(view.equals(btnAddPoint)){
            switch (btnAddPointState){
                case 1: //geklickt auf: "Neuer Punkt"
                    applyCurrentState();

                    enableInput();
                    btnAddPointState=2;
                    btnAddPoint.setText("Punkt hinzufügen");
                    break;
                case 2: //geklickt auf "Punkt hinzufügen"
                    Point pNew=new Point(axisOne.getProgress(),axisTwo.getProgress(),axisThree.getProgress(),axisFour.getProgress(),axisFive.getProgress(),axisSix.getProgress());
                    PointG pgNew;
                    //Delay überprüfen
                    int delayNew=0;
                    if(textViewDelay.getText().toString().isEmpty()){
                        dialogError("Bitte Delay angeben");
                    }else if((delayNew=Integer.parseInt(textViewDelay.getText().toString()))<0){
                        dialogError("Delay muss >= 0 sein");
                    }else{
                        pgNew=new PointG(pNew,getGeschwindigkeit(), delayNew);
                        dialogAddPunkt(pgNew);
                    }
                    break;
                case 3: //geklickt auf "Änderungen übernehmen"
                    Point pEdit=new Point(axisOne.getProgress(),axisTwo.getProgress(),axisThree.getProgress(),axisFour.getProgress(),axisFive.getProgress(),axisSix.getProgress());
                    PointG pgEdit;
                    //Delay überprüfen
                    int delayEdit=0;
                    if(textViewDelay.getText().toString().isEmpty()){
                        dialogError("Bitte Delay angeben");
                    }else if((delayEdit=Integer.parseInt(textViewDelay.getText().toString()))<0){
                        dialogError("Delay muss >= 0 sein");
                    }else {
                        pgEdit = new PointG(pEdit, getGeschwindigkeit(), delayEdit);
                        dialogEditPunkt(pgEdit);
                    }
                    break;
            }
        }else if(view.equals(btnSaveProgramm)){
            dialogSaveProgramm();
        }else if(view.equals(btnBack)){
            connector.addPoints(pointListOriginal,idProgramm);
            getFragmentManager().beginTransaction().replace(R.id.fragmentLayout_programm,fragmentErstellen).commit();
        }else if(view.equals(btnHome)){
            BTConnector.homePosition();
            applyCurrentState();
        }else if(view.equals(btnSleep)){
            getFragmentManager().beginTransaction().replace(R.id.fragmentLayout_programm,new BearbeitenFragmentJoystick()).commit();

            BTConnector.sleepPosition();
            applyCurrentState();
        }else if(view.equals(btnSwitch)){
            //Zum Joystick wechseln
            //Alte Activity
            //Intent intent = new Intent(getActivity(), BearbeitenActivity.class);
            //startActivity(intent);
            fragmentBearbeitenJoystick.setId(idProgramm);
            fragmentBearbeitenJoystick.setProgrammName(nameProgramm);
            fragmentBearbeitenJoystick.setPointListOriginal(pointListOriginal);
            //Programm speicher damit die neu hinzugefügten/gelöschten Punkte im Joystick Modus auch angezeigt werden
            connector.addPoints(pointList,idProgramm);
            getFragmentManager().beginTransaction().replace(R.id.fragmentLayout_programm,fragmentBearbeitenJoystick).commit();
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
        axisGeschwindigkeit.setProgress(point.getGeschwindigkeit()-10);
        textViewDelay.setText(String.valueOf(point.getDelay()));
        Point p=new Point(axisOne.getProgress(),axisTwo.getProgress(),axisThree.getProgress(),axisFour.getProgress(),axisFive.getProgress(),axisSix.getProgress());
        int geschw=getGeschwindigkeit();
        BTConnector.goTo(p,geschw);

        btnAddPointState=3;
        editPoint=i;
        btnAddPoint.setText("Änderungen übernehmen");
        enableInput();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        dialogDeletePunkt(i);
        return true;
        //Bei halten kann das Item gelöscht werden
    }
    private void applyCurrentState(){
        //Seekbars auf den Wert setzen, auf den die Servos tatsächlich sind
        Point curPosition=BTConnector.getCurPosition();
        axisOne.setProgress(curPosition.getAxisOne());
        axisTwo.setProgress(curPosition.getAxisTwo());
        axisThree.setProgress(curPosition.getAxisThree());
        axisFour.setProgress(curPosition.getAxisFour());
        axisFive.setProgress(curPosition.getAxisFive());
        axisSix.setProgress(curPosition.getAxisSix());
    }
    private void enableInput(){
        axisOne.setEnabled(true);
        axisTwo.setEnabled(true);
        axisThree.setEnabled(true);
        axisFour.setEnabled(true);
        axisFive.setEnabled(true);
        axisSix.setEnabled(true);
        axisGeschwindigkeit.setEnabled(true);
        textViewDelay.setEnabled(true);
    }
    private void disableInput(){
        axisOne.setEnabled(false);
        axisTwo.setEnabled(false);
        axisThree.setEnabled(false);
        axisFour.setEnabled(false);
        axisFive.setEnabled(false);
        axisSix.setEnabled(false);
        axisGeschwindigkeit.setEnabled(false);
        textViewDelay.setEnabled(false);
    }

    private String generatePointName(PointG pg, int index){
        String pName="P"+index+ " ["+"A1:"+pg.getAxisOne()+", A2:"+pg.getAxisTwo()+", A3:"+pg.getAxisThree()+", A4:"+pg.getAxisFour()+", A5:"+pg.getAxisFive()+", AGreifer:"+pg.getAxisSix()+"] Speed:"+pg.getGeschwindigkeit()+", Delay:"+pg.getDelay();
        return pName;
    }
    private int getGeschwindigkeit(){
        //Speed muss ausgewählt werden -> zwischen 10 und 40 -> umso niedriger umso schneller
        int speed=axisGeschwindigkeit.getProgress()+10;
        return speed;
    }

    //AlertDialogs
    private void dialogDeletePunkt(final int index){
        dialogBuilder = new AlertDialog.Builder(view.getContext());
        dialogBuilder.setMessage("P"+(index+1)+" löschen?");
        dialogBuilder.setTitle("Punkt löschen");
        dialogBuilder.setCancelable(true);

        dialogBuilder.setPositiveButton("OK",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //Punkt löschen
                arrayList.remove(index);
                pointList.remove(index);
                updateList();
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
    private void dialogAddPunkt(final PointG pg){
        dialogBuilder = new AlertDialog.Builder(view.getContext());
        dialogBuilder.setMessage("Punkt hinzufügen?"+"\n\n"+"Nach welchem Punkt soll die neue Position hinzugefügt werden?");
        dialogBuilder.setTitle("Punkt hinzufügen");
        dialogBuilder.setCancelable(true);

        Spinner spinner =new Spinner(view.getContext());
        ArrayList<String> spinnerList=new ArrayList<String>();
        spinnerList.add("an erster Stelle");
        for(int i=0;i<arrayList.size();i++){
            spinnerList.add("P"+(i+1));
        }
        ArrayAdapter arrayAdapterSpinner=new ArrayAdapter(view.getContext(),android.R.layout.simple_list_item_1,spinnerList);
        spinner.setAdapter(arrayAdapterSpinner);
        spinner.setSelection(spinnerList.size()-1);

        dialogBuilder.setView(spinner);

        dialogBuilder.setPositiveButton("Ja",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //Punkt hinzufügen
                int index=spinner.getSelectedItemPosition(); //Position ist äuqivilent zu index, an dem es eingefügt werden soll
                addListItem(pg,index);

                textViewDelay.setText("");
                disableInput();
                btnAddPointState=1;
                btnAddPoint.setText("Neuer Punkt");

                dialog.cancel();
            }
        });
        dialogBuilder.setNegativeButton("Nein",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                textViewDelay.setText("");
                disableInput();
                btnAddPointState=1;
                btnAddPoint.setText("Neuer Punkt");

                dialog.cancel();
            }
        });
        dialogBuilder.setNeutralButton("Abbrechen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //nur schließen
                dialog.cancel();
            }
        });

        dialog = dialogBuilder.create();
        dialog.show();
    }
    private void dialogEditPunkt(final PointG pg){
        dialogBuilder = new AlertDialog.Builder(view.getContext());
        int index = editPoint+1;
        String pName=generatePointName(pg,index);
        dialogBuilder.setMessage("Änderungen für "+pName+" übernehmen?");
        dialogBuilder.setTitle("Punkt ändern");
        dialogBuilder.setCancelable(true);

        dialogBuilder.setPositiveButton("Ja",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //Änderungen übernehmen
                updateListItem(pg);
                textViewDelay.setText("");
                disableInput();
                btnAddPointState = 1;
                btnAddPoint.setText("Neuer Punkt");
                dialog.cancel();
            }
        });
        dialogBuilder.setNegativeButton("Nein",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                textViewDelay.setText("");
                disableInput();
                btnAddPointState = 1;
                btnAddPoint.setText("Neuer Punkt");
                dialog.cancel();
            }
        });
        dialogBuilder.setNeutralButton("Abbruch", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //nur schließen
                dialog.cancel();
            }
        });
        dialog = dialogBuilder.create();
        dialog.show();
    }
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
    private void dialogSaveProgramm(){
        dialogBuilder = new AlertDialog.Builder(view.getContext());
        dialogBuilder.setMessage("Programm speichern?");
        dialogBuilder.setTitle("Programm speichern");
        dialogBuilder.setCancelable(true);

        dialogBuilder.setPositiveButton("Ja",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //Programm speichern
                connector.addPoints(pointList,idProgramm);
                getFragmentManager().beginTransaction().replace(R.id.fragmentLayout_programm,fragmentErstellen).commit();
                dialog.cancel();
            }
        });
        dialogBuilder.setNegativeButton("Nein",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //Programm nicht speichern
                //ursprünglichen Status wiederherstellen
                connector.addPoints(pointListOriginal,idProgramm);
                getFragmentManager().beginTransaction().replace(R.id.fragmentLayout_programm,fragmentErstellen).commit();
                dialog.cancel();
            }
        });
        dialogBuilder.setNeutralButton("Abbruch",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //nur schließen
                dialog.cancel();
            }
        });

        dialog = dialogBuilder.create();
        dialog.show();
    }
}