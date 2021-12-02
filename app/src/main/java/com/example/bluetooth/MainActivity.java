package com.example.bluetooth;

import android.app.ActionBar;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.ParcelUuid;
import android.renderscript.RenderScript;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainer;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, CompoundButton.OnCheckedChangeListener {

    private OutputStream outputStream;
    private InputStream inputStream;

    public SeekBar axisSix;
    public SeekBar axisFive;
    public SeekBar axisFour;
    public SeekBar axisThree;
    public SeekBar axisTwo;
    public SeekBar axisOne;

    public Switch switchAxisSix;
    public Switch switchAxisFive;
    public Switch switchAxisFour;
    public Switch switchAxisThree;
    public Switch switchAxisTwo;
    public Switch switchAxisOne;

    private BluetoothAdapter bt;

    public DrawLine drawLine;

    public TextView textView;

    public BottomNavigationView bottomNavigationView;

    public FragmentView fragment1;
    public FragmentView fragment2;
    public FragmentView fragment3;
    public FragmentView fragment4;

    public SeekBar delay;

    public Button homeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragment1 = new FragmentView(R.layout.fragment1,this);
        fragment2 = new FragmentView(R.layout.fragment2,this);
        fragment3 = new FragmentView(R.layout.fragment3,this);
        fragment4 = new FragmentView(R.layout.fragment4,this);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,fragment4).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,fragment3).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,fragment2).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,fragment1).commit();

        bottomNavigationView = findViewById(R.id.bottomNavigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                Fragment fragment = null;

                switch (item.getItemId()){
                    case R.id.item1:
                        fragment = fragment1;
                        break;
                    case R.id.item2:
                        fragment = fragment2;
                        break;
                    case R.id.item3:
                        fragment = fragment3;
                        break;
                    case R.id.item4:
                        fragment = fragment4;
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,fragment).commit();
                return true;
            }
        });

        drawLine = new DrawLine(this);

        LinearLayout l = findViewById(R.id.View);

        l.addView(drawLine,new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));


    }

    public void addListeners(){
        axisSix.setOnSeekBarChangeListener(this);
        axisFive.setOnSeekBarChangeListener(this);
        axisFour.setOnSeekBarChangeListener(this);
        axisThree.setOnSeekBarChangeListener(this);
        axisTwo.setOnSeekBarChangeListener(this);
        axisOne.setOnSeekBarChangeListener(this);

        switchAxisOne.setOnCheckedChangeListener(this);
        switchAxisTwo.setOnCheckedChangeListener(this);
        switchAxisThree.setOnCheckedChangeListener(this);
        switchAxisFour.setOnCheckedChangeListener(this);
        switchAxisFive.setOnCheckedChangeListener(this);
        switchAxisSix.setOnCheckedChangeListener(this);

        delay.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                write("d"+(seekBar.getProgress()+20));
                writeConsole("Delay wurde auf "+(seekBar.getProgress()+20)+" ms gestellt");
            }
        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    drawLine.axes.changePosition(90, 120, 25, 50,0,60);
                    write("190");
                    write("2120");
                    write("325");
                    write("450");
                    write("50");
                    write("660");
                    writeConsole("Roboter wird in Home Position gefahren");
                }catch (Exception e){
                    writeConsole(e.getMessage());
                }
            }
        });

        drawLine.axes.changePosition(axisOne.getProgress(),axisTwo.getProgress(),axisThree.getProgress(),axisFour.getProgress(),axisFive.getProgress(),axisSix.getProgress());
    }

    public void writeConsole(String text){
        textView.setText(textView.getText()+"\r\n"+text);
    }

    public void write(String s){
        try{
            s += ".";
            outputStream.write(s.getBytes());
        }catch (Exception e){
            writeConsole("Senden der Anweisung hat nicht geklappt");
        }

    }
    public void connectBluetooth(){
        try {
            bt = BluetoothAdapter.getDefaultAdapter();
            for(BluetoothDevice device : bt.getBondedDevices()){
                if(device.getName().equals("HC-05")){
                    ParcelUuid[] uuids = device.getUuids();
                    BluetoothSocket socket = device.createRfcommSocketToServiceRecord(uuids[0].getUuid());
                    socket.connect();
                    outputStream = socket.getOutputStream();
                    inputStream = socket.getInputStream();
                    writeConsole("Verbindung mit Bluetooth erfolgreich");
                }
            }
        }catch (Exception e){
            writeConsole("Verbindung mit Bluetooth fehlgeschlagen");
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        drawLine.axes.changePosition(axisOne.getProgress(),axisTwo.getProgress(),axisThree.getProgress(),axisFour.getProgress(),axisFive.getProgress(),axisSix.getProgress());
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if(seekBar.equals(axisSix)){
            write("6"+axisSix.getProgress());
            writeConsole("6 Achse auf "+axisSix.getProgress()+" Grad");
        }
        if(seekBar.equals(axisFive)){
            write("5"+axisFive.getProgress());
            writeConsole("5 Achse auf "+axisFive.getProgress()+" Grad");
        }
        if(seekBar.equals(axisFour)){
            write("4"+axisFour.getProgress());
            writeConsole("4 Achse auf "+axisFour.getProgress()+" Grad");
        }
        if(seekBar.equals(axisThree)){
            write("3"+axisThree.getProgress());
            writeConsole("3 Achse auf "+axisThree.getProgress()+" Grad");
        }
        if(seekBar.equals(axisTwo)){
            write("2"+axisTwo.getProgress());
            writeConsole("2 Achse auf "+axisTwo.getProgress()+" Grad");
        }
        if(seekBar.equals(axisOne)){
            write("1"+axisOne.getProgress());
            writeConsole("1 Achse auf "+axisOne.getProgress()+" Grad");
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked){
            if(buttonView.equals(switchAxisSix)){
                write("6180");
                writeConsole("6 Achse auf 180 Grad");
            }
            if(buttonView.equals(switchAxisFive)){
                write("5180");
                writeConsole("5 Achse auf 180 Grad");
            }
            if(buttonView.equals(switchAxisFour)){
                write("4180");
                writeConsole("4 Achse auf 180 Grad");
            }
            if(buttonView.equals(switchAxisThree)){
                write("3180");
                writeConsole("3 Achse auf 180 Grad");
            }
            if(buttonView.equals(switchAxisTwo)){
                write("2180");
                writeConsole("2 Achse auf 180 Grad");
            }
            if(buttonView.equals(switchAxisOne)){
                write("1180");
                writeConsole("1 Achse auf 180 Grad");
            }
        }
        if(buttonView.equals(switchAxisSix)){
            write("60");
            writeConsole("6 Achse auf 0 Grad");
        }
        if(buttonView.equals(switchAxisFive)){
            write("50");
            writeConsole("5 Achse auf 0 Grad");
        }
        if(buttonView.equals(switchAxisFour)){
            write("40");
            writeConsole("4 Achse auf 0 Grad");
        }
        if(buttonView.equals(switchAxisThree)){
            write("30");
            writeConsole("3 Achse auf 0 Grad");
        }
        if(buttonView.equals(switchAxisTwo)){
            write("20");
            writeConsole("2 Achse auf 0 Grad");
        }
        if(buttonView.equals(switchAxisOne)){
            write("10");
            writeConsole("1 Achse auf 0 Grad");
        }

    }
}