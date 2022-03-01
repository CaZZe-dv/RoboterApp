package com.example.bluetooth.steuerung;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.bluetooth.R;
import com.example.bluetooth.steuerung.simulation.Axes;
import com.example.bluetooth.steuerung.simulation.DrawCanvas;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;

public class FragmentBewegenKontroller extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener, Runnable{
    //Linkes Gamepad
    public FloatingActionButton buttonUpLeft;
    public FloatingActionButton buttonLeftLeft;
    public FloatingActionButton buttonRightLeft;
    public FloatingActionButton buttonDownLeft;
    //Rechtes Gamepad
    public FloatingActionButton buttonUpRight;
    public FloatingActionButton buttonLeftRight;
    public FloatingActionButton buttonRightRight;
    public FloatingActionButton buttonDownRight;
    //Simulation
    public RelativeLayout relativeLayout;
    public DrawCanvas drawCanvas;
    public Axes axes;
    //Home Button
    public FloatingActionButton buttonHome;
    //SleepButton
    public FloatingActionButton buttonSleep;
    //Seekbar für die Geschwindigkeit
    public SeekBar seekbar;
    //
    public TabLayout tabLayout;
    //
    public MainActivity mainActivity;
    //
    public BluetoothSteuerung bluetoothSteuerung;
    //
    public Thread runner;
    //
    public boolean axisOneLeft = false;
    public boolean axisOneRight = false;
    //
    public boolean axisTwoLeft = false;
    public boolean axisTwoRight = false;
    //
    public boolean axisThreeLeft = false;
    public boolean axisThreeRight = false;
    //
    public boolean axisFourLeft = false;
    public boolean axisFourRight = false;
    //
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_bewegen_kontroller);
        //
        axes = MainActivity.axes;
        bluetoothSteuerung = MainActivity.bluetoothSteuerung;
        mainActivity = MainActivity.instance;
        //Linkes Gamepad initialisieren
        buttonUpLeft = findViewById(R.id.buttonUpLeft);
        buttonLeftLeft = findViewById(R.id.buttonLeftLeft);
        buttonRightLeft = findViewById(R.id.buttonRightLeft);
        buttonDownLeft = findViewById(R.id.buttonDownLeft);
        //
        buttonUpLeft.setOnClickListener(this);
        buttonLeftLeft.setOnClickListener(this);
        buttonRightLeft.setOnClickListener(this);
        buttonDownLeft.setOnClickListener(this);
        //
        buttonUpLeft.setOnTouchListener(this);
        buttonLeftLeft.setOnTouchListener(this);
        buttonRightLeft.setOnTouchListener(this);
        buttonDownLeft.setOnTouchListener(this);
        //Rechtes Gamepas initialisieren
        buttonUpRight = findViewById(R.id.buttonUpRight);
        buttonLeftRight = findViewById(R.id.buttonLeftRight);
        buttonRightRight = findViewById(R.id.buttonRightRight);
        buttonDownRight = findViewById(R.id.buttonDownRight);
        //
        buttonUpRight.setOnClickListener(this);
        buttonLeftRight.setOnClickListener(this);
        buttonRightRight.setOnClickListener(this);
        buttonDownRight.setOnClickListener(this);
        //
        buttonUpRight.setOnTouchListener(this);
        buttonLeftRight.setOnTouchListener(this);
        buttonRightRight.setOnTouchListener(this);
        buttonDownRight.setOnTouchListener(this);
        //
        relativeLayout = findViewById(R.id.relativeLayoutBewgenKontroller);
        drawCanvas = new DrawCanvas(this,axes);
        relativeLayout.addView(drawCanvas);
        //
        buttonHome = findViewById(R.id.buttonHomeController);
        buttonHome.setOnClickListener(this);
        //
        buttonSleep = findViewById(R.id.buttonSleepController);
        buttonSleep.setOnClickListener(this);
        //
        seekbar = findViewById(R.id.seekbar);
        //
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getText().toString().equals("Drehen")){
                    seekbar.setMax(180);
                }
                if(tab.getText().toString().equals("Greifer")){
                    seekbar.setMax(70);
                }
                if(tab.getText().toString().equals("Delay")){
                    seekbar.setMax(80);
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(seekBar.getMax() == 180){
                    drawCanvas.axes.axis5.degree = seekBar.getProgress();
                    drawCanvas.invalidate();
                    bluetoothSteuerung.sendMessage("5"+seekBar.getProgress());
                    mainActivity.fragmentKonsole.writeConsole("Achse 5 auf "+seekBar.getProgress()+" Grad");
                }
                if(seekBar.getMax() == 70){
                    drawCanvas.axes.axis6.degree = seekBar.getProgress();
                    drawCanvas.invalidate();
                    bluetoothSteuerung.sendMessage("6"+seekBar.getProgress());
                    mainActivity.fragmentKonsole.writeConsole("Achse 6 auf "+seekBar.getProgress()+" Grad");
                }
                if(seekBar.getMax() == 80){
                    bluetoothSteuerung.sendMessage("d"+(seekBar.getProgress()+20));
                    mainActivity.fragmentKonsole.writeConsole("Delay auf "+(seekBar.getProgress()+20)+" ms");
                }
            }
        });

        startThread();
    }

    @Override
    public void onClick(View v) {
        FloatingActionButton f = (FloatingActionButton)v;
        //Linkes Gamepad
        if(f.equals(buttonDownLeft)){
            axisTwoLeft = false;
        }
        if(f.equals(buttonUpLeft)){
            axisTwoRight = false;
        }
        if(f.equals(buttonLeftLeft)){
            axisOneLeft = false;
        }
        if(f.equals(buttonRightLeft)){
            axisOneRight = false;
        }
        //Rechtes Gamepad
        if(f.equals(buttonDownRight)){
            axisThreeLeft = false;
        }
        if(f.equals(buttonUpRight)){
            axisThreeRight = false;
        }
        if(f.equals(buttonLeftRight)){
            axisFourLeft = false;
        }
        if(f.equals(buttonRightRight)){
            axisFourRight = false;
        }
        //Home und Sleep Button
        if(f.equals(buttonHome)){
            drawCanvas.axes.changePosition(90, 120, 25, 50, 0, 60);
            drawCanvas.invalidate();
            bluetoothSteuerung.sendMessage(new String[]{"190","2120","325","450","50","660"});
            mainActivity.fragmentKonsole.writeConsole("Roboter wird in Home Position gefahren");
        }
        if(f.equals(buttonSleep)){
            drawCanvas.axes.changePosition(90, 60, 33, 25, 0, 60);
            drawCanvas.invalidate();
            bluetoothSteuerung.sendMessage(new String[]{"190","260","333","425","50","660"});
            mainActivity.fragmentKonsole.writeConsole("Roboter wird in Schlaf Position gefahren");
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        FloatingActionButton f = (FloatingActionButton)v;
        //Linkes Gamepad
        if(f.equals(buttonDownLeft)){
            axisTwoLeft = true;
        }
        if(f.equals(buttonUpLeft)){
            axisTwoRight = true;
        }
        if(f.equals(buttonLeftLeft)){
            axisOneLeft = true;
        }
        if(f.equals(buttonRightLeft)){
            axisOneRight = true;
        }
        //Rechtes Gamepad
        if(f.equals(buttonDownRight)){
            axisThreeLeft = true;
        }
        if(f.equals(buttonUpRight)){
            axisThreeRight = true;
        }
        if(f.equals(buttonLeftRight)){
            axisFourLeft = true;
        }
        if(f.equals(buttonRightRight)){
            axisFourRight = true;
        }
        return false;
    }
    public void startThread(){
        runner = new Thread(this);
        runner.start();
    }
    public void stopThread(){
        runner = null;
    }
    @Override
    public void run() {
        try {
            while (Thread.currentThread() == runner) {
                if(axisOneLeft){
                    if(axes.axis1.degree < 180){
                        axes.axis1.degree++;
                    }
                }else if(axisOneRight){
                    if(axes.axis1.degree > 0){
                        axes.axis1.degree--;
                    }
                }
                if(axisTwoLeft){
                    if(axes.axis2.degree < 180){
                        axes.axis2.degree++;
                    }
                }else if(axisTwoRight){
                    if(axes.axis2.degree > 0){
                        axes.axis2.degree--;
                    }
                }
                if(axisThreeLeft){
                    if(axes.axis3.degree < 180){
                        axes.axis3.degree++;
                    }
                }else if(axisThreeRight){
                    if(axes.axis3.degree > 0){
                        axes.axis3.degree--;
                    }
                }
                if(axisFourLeft){
                    if(axes.axis4.degree < 180){
                        axes.axis4.degree++;
                    }
                }else if(axisFourRight){
                    if(axes.axis4.degree > 0){
                        axes.axis4.degree--;
                    }
                }
                drawCanvas.invalidate();
                bluetoothSteuerung.sendMessage(new String[]{"1"+axes.axis1.degree,"2"+axes.axis2.degree,"3"+axes.axis3.degree,
                                                            "4"+axes.axis4.degree,"5"+axes.axis5.degree,"6"+axes.axis6.degree});
                Thread.sleep(100);
            }
        }catch (Exception e){

        }
    }
}
