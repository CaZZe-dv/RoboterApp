package com.example.bluetooth.programme.erstellen;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.example.bluetooth.R;
import com.example.bluetooth.programme.database.Connector;
import com.example.bluetooth.programme.robot.BTConnector;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class BearbeitenFragmentJoystick extends Fragment implements View.OnTouchListener, SeekBar.OnSeekBarChangeListener, View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, Runnable, CompoundButton.OnCheckedChangeListener {

    View view;
    Connector connector;
    public Thread thread;

    ErstellenFragment fragmentErstellen;
    BearbeitenFragment fragmentBearbeiten;

    int idProgramm;//ID von zu bearbeitendem Programm
    String nameProgramm;//Name des Programms
    ArrayList<PointG> pointListOriginal;

    int axisOneProgress;
    int axisTwoProgress;
    int axisThreeProgress;
    int axisFourProgress;
    int axisFiveProgress;
    int axisSixProgress;

    boolean axisOneUp;
    boolean axisOneDown;
    boolean axisTwoUp;
    boolean axisTwoDown;
    boolean axisThreeUp;
    boolean axisThreeDown;
    boolean axisFourUp;
    boolean axisFourDown;
    boolean axisFiveUp;
    boolean axisFiveDown;
    boolean axisGreiferUp;
    boolean axisGreiferDown;
    Point curPoint;

    SeekBar axisGeschwindigkeit;
    int speed;

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

    Switch switchJoystickMode;
    boolean joystickMode;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        view = inflater.inflate(R.layout.fragment_prog_bearbeiten_joystick, container, false);
        init();
        return view;
    }
    private void init(){
        connector=new Connector(view.getContext());
        fragmentErstellen=new ErstellenFragment();
        fragmentBearbeiten=new BearbeitenFragment();
        //Seekbars
        axisGeschwindigkeit = view.findViewById(R.id.seekBar_bearbeitenJoystick_Geschwindigkeit);
        axisGeschwindigkeit.setOnSeekBarChangeListener(this);
        //Achsen auf die Werte setzen, die derzeit im BTConnector sind
        syncAxes();

        //Buttons
        btnAddPoint = view.findViewById(R.id.btn_bearbeitenJoystick_addPunkt);
        btnAddPointState=1;
        btnSaveProgramm = view.findViewById(R.id.btn_bearbeitenJoystick_saveProgramm);
        btnBack = view.findViewById(R.id.btn_bearbeitenJoystick_back);
        btnHome = view.findViewById(R.id.btn_bearbeitenJoystick_home);
        btnSleep = view.findViewById(R.id.btn_bearbeitenJoystick_sleep);
        btnSwitch = view.findViewById(R.id.btn_bearbeitenJoystick_switchMode);

        btnAddPoint.setOnClickListener(this);
        btnSaveProgramm.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnHome.setOnClickListener(this);
        btnSleep.setOnClickListener(this);
        btnSwitch.setOnClickListener(this);

        //Switch
        switchJoystickMode=view.findViewById(R.id.switch_bearbeiten);
        switchJoystickMode.setOnCheckedChangeListener(this);
        //TextView
        textViewDelay = view.findViewById(R.id.txt_bearbeitenJoystick_Delay);
        textViewName = view.findViewById(R.id.txt_bearbeitenJoystick_programmName);
        textViewName.setText(nameProgramm);

        disableInput();

        //Liste
        listView=(ListView)view.findViewById(R.id.listView_bearbeitenJoystick_punkte);
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
        initJoystick();

        curPoint=BTConnector.getCurPosition();

        startThread();
    }
    private void initJoystick(){
        joystickRechts = (RelativeLayout)view.findViewById(R.id.relLayout_bearbeitenJoystick_joystickRechts);
        joystickLinks = (RelativeLayout)view.findViewById(R.id.relLayout_bearbeitenJoystick_joystickLinks);

        jsRechts = new JoyStickClass(view.getContext(), joystickRechts);


        jsLinks = new JoyStickClass(view.getContext(), joystickLinks);



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
        Point p= BTConnector.getCurPosition();
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




    @Override
    public boolean onTouch(View v, MotionEvent motionEvent) {
        if(v.equals(joystickLinks)){
            jsLinks.drawStick(motionEvent);
            if(motionEvent.getAction() == MotionEvent.ACTION_DOWN || motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                int direction = jsLinks.get8Direction();
                if(direction == JoyStickClass.conUp) {

                } else if(direction == JoyStickClass.conUpRight) {

                } else if(direction == JoyStickClass.conRight) {

                } else if(direction == JoyStickClass.conDownRight) {

                } else if(direction == JoyStickClass.conDown) {

                } else if(direction == JoyStickClass.conDownLeft) {

                } else if(direction == JoyStickClass.conLeft) {

                } else if(direction == JoyStickClass.conUpLeft) {

                } else if(direction == JoyStickClass.conNone) {

                }
            } else if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                //Joystick wird nicht mehr berührt
            }
            return true;
        }else if(v.equals(joystickRechts)){
            jsRechts.drawStick(motionEvent);
            if(motionEvent.getAction() == MotionEvent.ACTION_DOWN || motionEvent.getAction() == MotionEvent.ACTION_MOVE) {

                int direction = jsRechts.get8Direction();
                if(direction == JoyStickClass.conUp) {

                } else if(direction == JoyStickClass.conUpRight) {

                } else if(direction == JoyStickClass.conRight) {

                } else if(direction == JoyStickClass.conDownRight) {

                } else if(direction == JoyStickClass.conDown) {

                } else if(direction == JoyStickClass.conDownLeft) {

                } else if(direction == JoyStickClass.conLeft) {

                } else if(direction == JoyStickClass.conUpLeft) {

                } else if(direction == JoyStickClass.conNone) {

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
            //ursprünglichen Status wiederherstellen
            connector.addPoints(pointListOriginal,idProgramm);
            switchToErstellen();
            //getFragmentManager().beginTransaction().replace(R.id.fragmentLayout_programm,fragmentErstellen).commit();
        }else if(view.equals(btnHome)){
            BTConnector.homePosition();
            applyCurrentState();
        }else if(view.equals(btnSleep)){
            BTConnector.sleepPosition();
            applyCurrentState();
        }else if(view.equals(btnSwitch)){
            switchBack();
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
                switchToErstellen();
                //getFragmentManager().beginTransaction().replace(R.id.fragmentLayout_programm,fragmentErstellen).commit();
                dialog.cancel();
            }
        });
        dialogBuilder.setNegativeButton("Nein",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //Programm nicht speichern
                //ursprünglichen Status wiederherstellen
                connector.addPoints(pointListOriginal,idProgramm);
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
    private void switchBack(){
        fragmentBearbeiten.setId(idProgramm);
        fragmentBearbeiten.setProgrammName(nameProgramm);
        fragmentBearbeiten.setPointListOriginal(pointListOriginal);

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //Programm speichern, damit es mit id im anderen Modus wieder aufgerufen werden kann
        connector.addPoints(pointList,idProgramm);
        getFragmentManager().beginTransaction().replace(R.id.fragmentLayout_programm,fragmentBearbeiten).commit();
        //SupportActionBar wieder anzeigen
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }
    private void switchToErstellen(){
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getFragmentManager().beginTransaction().replace(R.id.fragmentLayout_programm,fragmentErstellen).commit();
        //SupportActionBar wieder anzeigen
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }

    public void setPointListOriginal(ArrayList<PointG> pointList) {
        this.pointListOriginal = pointList;
    }

    public void startThread(){
        thread = new Thread(this);
        thread.start();
    }
    public void stopThread(){
        thread = null;
    }

    @Override
    public void run() {
        try {
            while (true) {
                speed=axisGeschwindigkeit.getProgress()+10;

                //Linker Joystick -> Bewegungs Modus
                if(!joystickMode){
                    //axisOne
                    if(axisOneUp){
                        int axisOne=curPoint.getAxisOne();
                        if(axisOne < 180 && axisOne>0){
                            curPoint.setAxisOne(axisOne++);
                        }
                    }else if(axisOneDown){
                        int axisOne=curPoint.getAxisOne();
                        if(axisOne < 180 && axisOne>0){
                            curPoint.setAxisOne(axisOne--);
                        }
                    }
                    //axisTwo
                    if(axisTwoUp){
                        int axisTwo=curPoint.getAxisTwo();
                        if(axisTwo < 180 && axisTwo>0){
                            curPoint.setAxisTwo(axisTwo++);
                        }
                    }else if(axisTwoDown){
                        int axisTwo=curPoint.getAxisTwo();
                        if(axisTwo < 180 && axisTwo>0){
                            curPoint.setAxisTwo(axisTwo--);
                        }
                    }
                }else if(joystickMode){//Linker Joystick -> Greifer Modus
                    //axisFive
                    if(axisFiveUp){
                        int axisFive=curPoint.getAxisFive();
                        if(axisFive < 180 && axisFive>0){
                            curPoint.setAxisFive(axisFive++);
                        }
                    }else if(axisFiveDown){
                        int axisFive=curPoint.getAxisFive();
                        if(axisFive < 180 && axisFive>0){
                            curPoint.setAxisFive(axisFive--);
                        }
                    }
                    //axisGreifer
                    if(axisGreiferUp){
                        int axisGreifer=curPoint.getAxisSix();
                        if(axisGreifer < 180 && axisGreifer>0){
                            curPoint.setAxisSix(axisGreifer++);
                        }
                    }else if(axisGreiferDown){
                        int axisGreifer=curPoint.getAxisSix();
                        if(axisGreifer < 180 && axisGreifer>0){
                            curPoint.setAxisSix(axisGreifer--);
                        }
                    }
                }
                //Rechter Joystick
                //axisThree
                if(axisThreeUp){
                    int axisThree=curPoint.getAxisThree();
                    if(axisThree < 180 && axisThree>0){
                        curPoint.setAxisThree(axisThree++);
                    }
                }else if(axisThreeDown){
                    int axisThree=curPoint.getAxisThree();
                    if(axisThree < 180 && axisThree>0){
                        curPoint.setAxisThree(axisThree--);
                    }
                }
                //axisFour
                if(axisFourUp){
                    int axisFour=curPoint.getAxisFour();
                    if(axisFour < 180 && axisFour>0){
                        curPoint.setAxisFour(axisFour++);
                    }
                }else if(axisFourDown){
                    int axisFour=curPoint.getAxisFour();
                    if(axisFour < 180 && axisFour>0){
                        curPoint.setAxisFour(axisFour--);
                    }
                }
                BTConnector.goTo(curPoint,speed);

                Thread.sleep(speed);
            }
        }catch (Exception e){

        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked){
            //Linker Joystick ändert auf GreiferModus
            joystickMode=true;
        }else{
            //Linker Joystick ändert auf Bewegungsmodus
            joystickMode=false;
        }
    }
}