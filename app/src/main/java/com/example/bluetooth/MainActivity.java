package com.example.bluetooth;

import android.app.ActionBar;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.ParcelUuid;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainer;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    private OutputStream outputStream;
    private InputStream inputStream;

    private SeekBar axisSix;
    private SeekBar axisFive;
    private SeekBar axisFour;
    private SeekBar axisThree;
    private SeekBar axisTwo;
    private SeekBar axisOne;

    private Switch switchAxisSix;
    private Switch switchAxisFive;
    private Switch switchAxisFour;
    private Switch switchAxisThree;
    private Switch switchAxisTwo;
    private Switch switchAxisOne;

    private BluetoothAdapter bt;

    public DrawLine drawLine;

    public TextView textView;

    public BottomNavigationView bottomNavigationView;

    public FragmentView fragment1;
    public FragmentView fragment2;
    public FragmentView fragment3;
    public FragmentView fragment4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragment1 = new FragmentView(R.layout.fragment1);
        fragment2 = new FragmentView(R.layout.fragment2);
        fragment3 = new FragmentView(R.layout.fragment3);
        fragment4 = new FragmentView(R.layout.fragment4);

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

        //axisSix = findViewById(R.id.AxisSix);
        //axisFive = findViewById(R.id.AxisFive);
        //axisFour = findViewById(R.id.AxisFour);
        //axisThree = findViewById(R.id.AxisThree);
        //axisTwo = findViewById(R.id.AxisTwo);
        //axisOne = findViewById(R.id.AxisOne);
//
        //switchAxisSix = findViewById(R.id.SwitchAxisSix);
        //switchAxisFive = findViewById(R.id.SwitchAxisFive);
        //switchAxisFour = findViewById(R.id.SwitchAxisFour);
        //switchAxisThree = findViewById(R.id.SwitchAxisThree);
        //switchAxisTwo = findViewById(R.id.SwitchAxisTwo);
        //switchAxisOne = findViewById(R.id.SwitchAxisOne);

        drawLine = new DrawLine(this);

        LinearLayout l = findViewById(R.id.View);

        l.addView(drawLine,new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        //switchAxisSix.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        //    @Override
        //    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        //        if(isChecked){
        //            write("6"+70);
        //            return;
        //        }
        //        write("6"+0);
        //    }
        //});
        //switchAxisFive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        //    @Override
        //    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        //        if(isChecked){
        //            write("5"+180);
        //            return;
        //        }
        //        write("5"+0);
        //    }
        //});
        //switchAxisFour.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        //    @Override
        //    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        //        if(isChecked){
        //            write("4"+180);
        //            return;
        //        }
        //        write("4"+0);
        //    }
        //});
        //switchAxisThree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        //    @Override
        //    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        //        if(isChecked){
        //            write("3"+180);
        //            return;
        //        }
        //        write("3"+0);
        //    }
        //});
        //switchAxisTwo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        //    @Override
        //    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        //        if(isChecked){
        //            write("2"+180);
        //            return;
        //        }
        //        write("2"+0);
        //    }
        //});
        //switchAxisOne.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        //    @Override
        //    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        //        if(isChecked){
        //            write("1"+180);
        //            return;
        //        }
        //        write("1"+0);
        //    }
        //});
//
        //axisSix.setOnSeekBarChangeListener(this);
        //axisFive.setOnSeekBarChangeListener(this);
        //axisFour.setOnSeekBarChangeListener(this);
        //axisThree.setOnSeekBarChangeListener(this);
        //axisTwo.setOnSeekBarChangeListener(this);
        //axisOne.setOnSeekBarChangeListener(this);

        //textLog = findViewById(R.id.textView);

        //connectBluetooth();


    }
    public void write(String s){
        try{
            outputStream.write(s.getBytes());
        }catch (Exception e){

        }

    }
    public void connectBluetooth(){
        bt = BluetoothAdapter.getDefaultAdapter();
        for(BluetoothDevice device : bt.getBondedDevices()){
            if(device.getName().equals("HC-05")){
                try {
                    ParcelUuid[] uuids = device.getUuids();
                    BluetoothSocket socket = device.createRfcommSocketToServiceRecord(uuids[0].getUuid());
                    socket.connect();

                    outputStream = socket.getOutputStream();
                    inputStream = socket.getInputStream();


                }catch (Exception e){

                }
            }
        }
    }
    //public void switchToControllerLogin(){
    //    Intent intent = new Intent(this, ControllerLogin.class);
    //    startActivity(intent);
    //}

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        drawLine.axes.changePosition(axisOne.getProgress(),axisTwo.getProgress(),axisThree.getProgress(),axisFour.getProgress());
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if(seekBar.equals(axisSix)){
            write("6"+axisSix.getProgress());
            textView.setText("6"+axisSix.getProgress());
        }
        if(seekBar.equals(axisFive)){
            write("5"+axisFive.getProgress());
            textView.setText("5"+axisFive.getProgress());
        }
        if(seekBar.equals(axisFour)){
            write("4"+axisFour.getProgress());
            textView.setText("4"+axisFour.getProgress());
        }
        if(seekBar.equals(axisThree)){
            write("3"+axisThree.getProgress());
            textView.setText("3"+axisThree.getProgress());
        }
        if(seekBar.equals(axisTwo)){
            write("2"+axisTwo.getProgress());
            textView.setText("2"+axisTwo.getProgress());
        }
        if(seekBar.equals(axisOne)){
            write("1"+axisOne.getProgress());
            textView.setText("1"+axisOne.getProgress());
        }
    }
}