package com.example.bluetooth.programme.erstellen;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;

import com.example.bluetooth.R;
import com.example.bluetooth.programme.database.Connector;
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
import android.widget.TextView;

import java.util.ArrayList;


public class BearbeitenActivity extends AppCompatActivity implements OnTouchListener, SeekBar.OnSeekBarChangeListener, View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener{

    Connector connector;

    ErstellenFragment fragmentErstellen;
    int idProgramm;//ID von zu bearbeitendem Programm
    String nameProgramm;//Name des Programms

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
        init();


    }
    private void init(){
        connector=new Connector(getApplicationContext());
        fragmentErstellen=new ErstellenFragment();
        //Seekbars
        axisGeschwindigkeit = findViewById(R.id.axisGeschwindigkeit_ProgrammeActivity);
        axisGeschwindigkeit.setOnSeekBarChangeListener(this);

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
        arrayAdapter=new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(arrayAdapter);
        updateList();

        applyCurrentState();


        initJoystick();
    }
    private void initJoystick(){
        joystickRechts = (RelativeLayout)findViewById(R.id.joystickRechts);
        joystickLinks = (RelativeLayout)findViewById(R.id.joystickLinks);

        jsRechts = new JoyStickClass(getApplicationContext(), joystickRechts, R.drawable.ic_add);
        //jsRechts.setStickSize(150, 150);
        //jsRechts.setLayoutSize(500, 500);
        jsRechts.setLayoutAlpha(150);
        jsRechts.setStickAlpha(100);
        jsRechts.setOffset(90);
        jsRechts.setMinimumDistance(50);

        jsLinks = new JoyStickClass(getApplicationContext(), joystickLinks, R.drawable.ic_add);
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

    //Listen
    private void updateList(){
        arrayList=new ArrayList<>();
        for(int i=0;i<pointList.size();i++){
            PointG pg=pointList.get(i);
            int index=i+1;
            String pName=generatePointName(pg,index);
            arrayList.add(pName);
        }
        arrayAdapter=new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,arrayList);
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

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        return false;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }
}