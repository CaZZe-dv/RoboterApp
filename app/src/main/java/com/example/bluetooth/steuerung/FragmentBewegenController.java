package com.example.bluetooth.steuerung;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.bluetooth.R;
import com.example.bluetooth.steuerung.simulation.Axes;
import com.example.bluetooth.steuerung.simulation.DrawCanvas;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
/**
 * @author Matthias Fichtinger
 * @version 11.03.2022
 * Diese Klasse kümmert sic hebenfalls um die Steuerung des Roboterarms
 */
public class FragmentBewegenController extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener, Runnable{
    /**
     * Diese Buttons sind für das linke GAmepad zuständig
     */
    public FloatingActionButton buttonUpLeft;
    public FloatingActionButton buttonLeftLeft;
    public FloatingActionButton buttonRightLeft;
    public FloatingActionButton buttonDownLeft;
    /**
     * Diese Buttons sind für das rechte Gamepad zuständig
     */
    public FloatingActionButton buttonUpRight;
    public FloatingActionButton buttonLeftRight;
    public FloatingActionButton buttonRightRight;
    public FloatingActionButton buttonDownRight;
    /**
     * RelativLayout, DrawCanvas und Axes werden für die Simulation
     * des Robterarms benötigt
     */
    public RelativeLayout relativeLayout;
    public DrawCanvas drawCanvas;
    public Axes axes;
    /**
     * Dieser Button ist für das Anfahren der Homeposition zuständig
     */
    public FloatingActionButton buttonHome;
    /**
     * Dieser Button ist für das Anfahren der Schlafposition zuständig
     */
    public FloatingActionButton buttonSleep;
    /**
     * Diese Seekbar würd für die Funktionen des TabLayouts benötigt
     */
    public SeekBar seekbar;
    /**
     * Wird für den Greifer und das Delay benötigt
     */
    public TabLayout tabLayout;
    /**
     * Referenz zur MainActivity
     */
    public MainActivity mainActivity;
    /**
     * Referenz zur Bluetooth Schnittstelle
     */
    public BluetoothSteuerung bluetoothSteuerung;
    /**
     * Der Thread wird für Gamepads benötigt
     */
    public Thread runner;
    /**
     * Zwischenspeichern welcher Button aktiv ist
     */
    public boolean axisOneLeft = false;
    public boolean axisOneRight = false;
    public boolean axisTwoLeft = false;
    public boolean axisTwoRight = false;
    public boolean axisThreeLeft = false;
    public boolean axisThreeRight = false;
    public boolean axisFourLeft = false;
    public boolean axisFourRight = false;
    /**
     * Varibale für das Zwischenspeichern des Delays
     */
    public int delay = 100;

    /**
     * @param savedInstanceState
     * Deklarierte Eigenschaften werden initisiert und Events wenn nötig hinzugefügt
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_bewegen_controller);
        //
        axes = MainActivity.axes;
        bluetoothSteuerung = MainActivity.bluetoothSteuerung;
        mainActivity = MainActivity.instanz;
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
        /**
         * Dem TabLayout wird ein Listener hinzugefügt. Wenn ein neuer Tab ausgewählt wurde wird diese
         * Methode aufgerufen. Dann wird Verglichen welcher Tab aktiv ist und der maximale Wert der Seekbar
         * wird fetsgelegt.
         */
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
        /**
         * Der Seekbar wurde ebenfalls ein Listener hinzugefügt. Wenn sich der WErt ändert, werden
         * diese Methoden aufgerufen. Verglichen wird dann welchen maximalen WErt die Seekbar haben kann.
         * Darauf aufgebaut weiß man dann welcher Wert an den Roboter gschickt werden muss.
         */
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
                    delay = seekBar.getProgress()+20;
                    mainActivity.fragmentKonsole.writeConsole("Delay auf "+(seekBar.getProgress()+20)+" ms");
                }
            }
        });

        startThread();
    }

    /**
     * @param v
     * Wenn einer der FloatinActionButtons angeklickt wird, wird diese Methode aufgerufen.
     * Weil das Event erst ausgeführt wird, wenn der BEnutzer den Button geklickt und wieder
     * losgelassen hat, weiß man das der Button nicht mehr aktiv ist. Deswegen wird der Zustand
     * auf false gesetzt. Wenn der Button für die Home- oder Schlafposition angelickt wurde, wird
     * die festgelegte Position an den Roboter übermittelt.
     */
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
        //Home- und Sleepbutton
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

    /**
     * @param v
     * @param event
     * @return
     * Wenn einer der FloatingActionButtons berührt wird, wird diese Methode ausgeführt.
     * Dann wird kontrolliert welcher Button berührt wurde und der zuständige boolean wird
     * auf true gesetzt.
     */
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

    /**
     * Methode für das Starten des Threads, dabei wird der Thread initilaisiert und
     * diese Klasse mit this übergeben. Anschließend wird der Thread mit der Methode start
     * gestartet.
     */
    public void startThread(){
        runner = new Thread(this);
        runner.start();
    }

    /**
     * Gestoppt werden kann der Thread, indem man dieses null setzt.
     */
    public void stopThread(){
        runner = null;
    }
    /**
     * Für den Thread wurde diese Methode aus dem Interface Runnable implementiert.
     * Wenn der jetzige Thread dem erstelten Thread entspricht wird die Schleife ausgeführt.
     * Dann wird je nach dem welche Achse bewegt werden soll der Wert bei jedem Durchgang um eins
     * erhöht bzw verrigert.
     */
    @Override
    public void run() {
        try {
            while (Thread.currentThread() == runner) {
                if(axisOneLeft){
                    if(axes.axis1.degree < 180){
                        axes.axis1.degree++;
                        bluetoothSteuerung.sendMessage("1"+axes.axis1.degree);
                    }
                }else if(axisOneRight){
                    if(axes.axis1.degree > 0){
                        axes.axis1.degree--;
                        bluetoothSteuerung.sendMessage("1"+axes.axis1.degree);
                    }
                }
                if(axisTwoLeft){
                    if(axes.axis2.degree < 180){
                        axes.axis2.degree++;
                        bluetoothSteuerung.sendMessage("2"+axes.axis2.degree);
                    }
                }else if(axisTwoRight){
                    if(axes.axis2.degree > 0){
                        axes.axis2.degree--;
                        bluetoothSteuerung.sendMessage("2"+axes.axis2.degree);
                    }
                }
                if(axisThreeLeft){
                    if(axes.axis3.degree < 180){
                        axes.axis3.degree++;
                        bluetoothSteuerung.sendMessage("3"+axes.axis3.degree);
                    }
                }else if(axisThreeRight){
                    if(axes.axis3.degree > 0){
                        axes.axis3.degree--;
                        bluetoothSteuerung.sendMessage("3"+axes.axis3.degree);
                    }
                }
                if(axisFourLeft){
                    if(axes.axis4.degree < 180){
                        axes.axis4.degree++;
                        bluetoothSteuerung.sendMessage("4"+axes.axis4.degree);
                    }
                }else if(axisFourRight){
                    if(axes.axis4.degree > 0){
                        axes.axis4.degree--;
                        bluetoothSteuerung.sendMessage("4"+axes.axis4.degree);
                    }
                }
                drawCanvas.invalidate();
                Thread.sleep(delay);
            }
        }catch (Exception e){

        }
    }
}
