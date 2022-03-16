package com.example.bluetooth.programme.erstellen;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.bluetooth.R;
import com.example.bluetooth.programme.database.Connector;
import com.example.bluetooth.programme.robot.BTConnector;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;


public class BearbeitenActivity extends AppCompatActivity implements OnTouchListener, SeekBar.OnSeekBarChangeListener, View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener{

    Connector connector;

    int idProgramm;//ID von zu bearbeitendem Programm
    String nameProgramm;//Name des Programms

    int axisOneProgress;
    int axisTwoProgress;
    int axisThreeProgress;
    int axisFourProgress;
    int axisFiveProgress;
    int axisSixProgress;

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

    RelativeLayout joystickRechts;
    RelativeLayout joystickLinks;
    ImageView image_joystick, image_border;

    JoyStickClass jsRechts;
    JoyStickClass jsLinks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bearbeiten);
        //TopBar verstecken
        getSupportActionBar().hide();
        init();


    }
    private void init(){
        connector=new Connector(this);
        //Seekbars
        axisGeschwindigkeit = findViewById(R.id.axisGeschwindigkeit_ProgrammeActivity);
        axisGeschwindigkeit.setOnSeekBarChangeListener(this);
        //Achsen auf die Werte setzen, die derzeit im BTConnector sind
        syncAxes();

        //Buttons
        btnAddPoint = findViewById(R.id.btnAddPunktActivity);
        btnAddPointState=1;
        btnSaveProgramm = findViewById(R.id.btnSaveProgrammActivity);
        btnBack = findViewById(R.id.btnBearbeitenBackActivity);
        btnHome = findViewById(R.id.btnBearbeitenHomeActivity);
        btnSleep = findViewById(R.id.btnBearbeitenSleepActivity);
        btnSwitch = findViewById(R.id.btnBearbeitenSwitchActivity);

        btnAddPoint.setOnClickListener(this);
        btnSaveProgramm.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnHome.setOnClickListener(this);
        btnSleep.setOnClickListener(this);
        btnSwitch.setOnClickListener(this);
        //TextView
        textViewDelay = findViewById(R.id.textViewDelayActivity);
        textViewName = findViewById(R.id.textViewBearbeitenNameActivity);
        textViewName.setText(nameProgramm);

        disableInput();

        //Liste
        listView=(ListView)findViewById(R.id.listViewPunkteActivity);
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
        arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(arrayAdapter);
        updateList();

        applyCurrentState();


        initJoystick();
    }
    private void initJoystick(){
        joystickRechts = (RelativeLayout)findViewById(R.id.joystickRechts);
        joystickLinks = (RelativeLayout)findViewById(R.id.joystickLinks);

        jsRechts = new JoyStickClass(this, joystickRechts, R.drawable.ic_add);
        //jsRechts.setStickSize(150, 150);
        //jsRechts.setLayoutSize(500, 500);
        jsRechts.setLayoutAlpha(150);
        jsRechts.setStickAlpha(100);
        jsRechts.setOffset(90);
        jsRechts.setMinimumDistance(50);

        jsLinks = new JoyStickClass(this, joystickLinks, R.drawable.ic_add);
        //jsLinks.setStickSize(150, 150);
        //jsLinks.setLayoutSize(500, 500);
        jsLinks.setLayoutAlpha(150);
        jsLinks.setStickAlpha(100);
        jsLinks.setOffset(90);
        jsLinks.setMinimumDistance(50);


        joystickRechts.setOnTouchListener(this);
        joystickLinks.setOnTouchListener(this);
    }

    public void setId(int id){
        this.idProgramm=id;
    }
    public void setProgrammName(String programmName){
        this.nameProgramm=programmName;
    }

    //Axes
    private void syncAxes(){
        Point p=BTConnector.getCurPosition();
        axisOneProgress=p.getAxisOne();
        axisTwoProgress=p.getAxisTwo();
        axisThreeProgress=p.getAxisThree();
        axisFourProgress=p.getAxisFour();
        axisFiveProgress=p.getAxisFive();
        axisSixProgress=p.getAxisSix();

        int geschw=BTConnector.getCurSpeed();
        axisGeschwindigkeit.setProgress(geschw);
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
        arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,arrayList);
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




    @Override
    public boolean onTouch(View v, MotionEvent motionEvent) {
        if(v.equals(joystickLinks)){
            jsLinks.drawStick(motionEvent);
            if(motionEvent.getAction() == MotionEvent.ACTION_DOWN || motionEvent.getAction() == MotionEvent.ACTION_MOVE) {

                int direction = jsLinks.get8Direction();
                if(direction == JoyStickClass.STICK_UP) {

                } else if(direction == JoyStickClass.STICK_UPRIGHT) {

                } else if(direction == JoyStickClass.STICK_RIGHT) {

                } else if(direction == JoyStickClass.STICK_DOWNRIGHT) {

                } else if(direction == JoyStickClass.STICK_DOWN) {

                } else if(direction == JoyStickClass.STICK_DOWNLEFT) {

                } else if(direction == JoyStickClass.STICK_LEFT) {

                } else if(direction == JoyStickClass.STICK_UPLEFT) {

                } else if(direction == JoyStickClass.STICK_NONE) {

                }
            } else if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                //Joystick wird nicht mehr berührt
            }
            return true;
        }else if(v.equals(joystickRechts)){
            jsRechts.drawStick(motionEvent);
            if(motionEvent.getAction() == MotionEvent.ACTION_DOWN || motionEvent.getAction() == MotionEvent.ACTION_MOVE) {

                int direction = jsRechts.get8Direction();
                if(direction == JoyStickClass.STICK_UP) {

                } else if(direction == JoyStickClass.STICK_UPRIGHT) {

                } else if(direction == JoyStickClass.STICK_RIGHT) {

                } else if(direction == JoyStickClass.STICK_DOWNRIGHT) {

                } else if(direction == JoyStickClass.STICK_DOWN) {

                } else if(direction == JoyStickClass.STICK_DOWNLEFT) {

                } else if(direction == JoyStickClass.STICK_LEFT) {

                } else if(direction == JoyStickClass.STICK_UPLEFT) {

                } else if(direction == JoyStickClass.STICK_NONE) {

                }
            } else if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                //Joystick wird nicht mehr berührt
            }
            return true;
        }
        return false;
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
                    Point pNew=new Point(axisOneProgress,axisTwoProgress,axisThreeProgress,axisFourProgress,axisFiveProgress,axisSixProgress);
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
                    Point pEdit=new Point(axisOneProgress,axisTwoProgress,axisThreeProgress,axisFourProgress,axisFiveProgress,axisSixProgress);
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
            switchToErstellen();
            //getFragmentManager().beginTransaction().replace(R.id.fragmentLayout_programm,fragmentErstellen).commit();
        }else if(view.equals(btnHome)){
            BTConnector.homePosition();
            applyCurrentState();
        }else if(view.equals(btnSleep)){
            BTConnector.sleepPosition();
            applyCurrentState();
        }else if(view.equals(btnSwitch)){
            Intent intent = new Intent(BearbeitenActivity.this, BearbeitenFragment.class);
            startActivity(intent);
        }
    }

    //Liste Listeners
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //Bei einfachem Klick können Infos angesehen werden und die Seekbars werden auf die Position gesetzt, auf die sie bei diesem Punkt sind
        PointG point=pointList.get(i);
        axisOneProgress=point.getAxisOne();
        axisTwoProgress=point.getAxisTwo();
        axisThreeProgress=point.getAxisThree();
        axisFourProgress=point.getAxisFour();
        axisFiveProgress=point.getAxisFive();
        axisSixProgress=point.getAxisSix();

        axisGeschwindigkeit.setProgress(point.getGeschwindigkeit()-10);
        textViewDelay.setText(String.valueOf(point.getDelay()));
        Point p=new Point(axisOneProgress,axisTwoProgress,axisThreeProgress,axisFourProgress,axisFiveProgress,axisSixProgress);
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

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if(seekBar.equals(axisGeschwindigkeit)){
            BTConnector.sendMessage("d", String.valueOf(getGeschwindigkeit()));
        }
    }

    private void applyCurrentState(){
        //Seekbars auf den Wert setzen, auf den die Servos tatsächlich sind
        Point curPosition=BTConnector.getCurPosition();
        axisOneProgress=curPosition.getAxisOne();
        axisTwoProgress=curPosition.getAxisTwo();
        axisThreeProgress=curPosition.getAxisThree();
        axisFourProgress=curPosition.getAxisFour();
        axisFiveProgress=curPosition.getAxisFive();
        axisSixProgress=curPosition.getAxisSix();
    }
    private void enableInput(){
        //TODO: Joysticks enablen
        axisGeschwindigkeit.setEnabled(true);
        textViewDelay.setEnabled(true);
    }
    private void disableInput(){
        //TODO: Joysticks disabeln
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
        dialogBuilder = new AlertDialog.Builder(this);
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
        dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setMessage("Punkt hinzufügen?"+"\n\n"+"Nach welchem Punkt soll die neue Position hinzugefügt werden?");
        dialogBuilder.setTitle("Punkt hinzufügen");
        dialogBuilder.setCancelable(true);

        Spinner spinner =new Spinner(this);
        ArrayList<String> spinnerList=new ArrayList<String>();
        spinnerList.add("an erster Stelle");
        for(int i=0;i<arrayList.size();i++){
            spinnerList.add("P"+(i+1));
        }
        ArrayAdapter arrayAdapterSpinner=new ArrayAdapter(this,android.R.layout.simple_list_item_1,spinnerList);
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
        dialogBuilder = new AlertDialog.Builder(this);
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
        dialogBuilder = new AlertDialog.Builder(this);
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
        dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setMessage("Programm speichern?");
        dialogBuilder.setTitle("Programm speichern");
        dialogBuilder.setCancelable(true);

        dialogBuilder.setPositiveButton("Ja",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //Programm speichern
                connector.addPoints(pointList,idProgramm);
                switchToErstellen();
                //getFragmentManager().beginTransaction().replace(R.id.fragmentLayout_programm,fragmentErstellen).commit();
                dialog.cancel();
            }
        });
        dialogBuilder.setNegativeButton("Nein",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //Programm nicht speichern
                switchToErstellen();
                //getFragmentManager().beginTransaction().replace(R.id.fragmentLayout_programm,fragmentErstellen).commit();
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
    private void switchToErstellen(){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayout_programm, new ErstellenFragment()).commit();


        //Intent i = new Intent(this,ErstellenFragment.class);
        //startActivity(i);
    }
}