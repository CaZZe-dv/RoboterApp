package com.example.bluetooth.steuerung;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.ParcelUuid;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import com.example.bluetooth.R;
import com.example.bluetooth.steuerung.simulation.DrawCanvas;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import org.jetbrains.annotations.NotNull;
import java.io.InputStream;
import java.io.OutputStream;
//Kümmert sich um alle Bestandteile für die Steuerung des Roboters
public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, CompoundButton.OnCheckedChangeListener, BottomNavigationView.OnNavigationItemSelectedListener {
    //Für jede Achse eine Seekbar 1 bis 5 gehen von 0 bis 180 Grad
    //und die Achse 6 also der Greider geht von 0 bis 70 grad
    public SeekBar axisSix;
    public SeekBar axisFive;
    public SeekBar axisFour;
    public SeekBar axisThree;
    public SeekBar axisTwo;
    public SeekBar axisOne;
    //Simuliert den Roboter in der App
    public DrawCanvas drawCanvas;
    //TextView um den Text in der selbst erstellten Console anzeigen
    //zu können
    public TextView textView;
    //BottomNavigationView ist da um zwischen den Fragmenten zu wechseln
    public BottomNavigationView bottomNavigationView;
    //Alle Fragmente von 1 bis 3
    public FragmentHandler fragment1;
    public FragmentHandler fragment2;
    public FragmentHandler fragment3;
    //Seekbar für das Einstellen des Delays
    public SeekBar delay;
    //Einen Switch für das Schließen und Öffnen des Greifers
    public Switch greiferCloseAndOpen;
    //Home und Sleep Button für das Anfahren der fixen Positionen
    public Button homeButton;
    public Button sleepButton;
    //ein RelativeLayout wird für das DrawCanvas benötigt
    public RelativeLayout canvasContainer;

    public BluetoothConnection bluetoothConnection;

    //Überschreiben der onCreate Methode der Überklasse
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //onCreate Methoder der Überklasse aufrufen
        super.onCreate(savedInstanceState);
        //Als Content das XML-File für die Activity_Main setzen
        setContentView(R.layout.activity_steuerung);
        //Fragmente von 1 bis 3 deklarieren
        fragment1 = new FragmentHandler(R.layout.fragment_bewegen, this);
        fragment2 = new FragmentHandler(R.layout.fragment_funktionen, this);
        fragment3 = new FragmentHandler(R.layout.fragment_konsole, this);
        //Alle Fragmente einmal erstellen
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment3).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment2).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment1).commit();
        //Referenz zu BottomNAvigation erstellen
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        //BottomNAvigation einen setOnNAvigationItemSelectedListener hinzufügen
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        //Refereanz zum canvasContainer erstellen
        canvasContainer = findViewById(R.id.canvasContainer);
        //drawCanvas erstellen und MAinactivity übergeben
        drawCanvas = new DrawCanvas(this);
        //Auf dem canvasContainer das drawCanvas hinzufügen
        canvasContainer.addView(drawCanvas);

    }
    //Wenn Fragmente erstellt worden sind wird diese Methode
    public void addListeners() {
        //setOnSeekBarChangeListener hinzufügen
        axisSix.setOnSeekBarChangeListener(this);
        axisFive.setOnSeekBarChangeListener(this);
        axisFour.setOnSeekBarChangeListener(this);
        axisThree.setOnSeekBarChangeListener(this);
        axisTwo.setOnSeekBarChangeListener(this);
        axisOne.setOnSeekBarChangeListener(this);
        delay.setOnSeekBarChangeListener(this);

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    drawCanvas.axes.changePosition(90, 120, 25, 50, 0, 60);
                    bluetoothConnection.sendMessage(new String[]{"190","2120","325","450","50","660"});
                    writeConsole("Roboter wird in Home Position gefahren");
                } catch (Exception e) {
                    writeConsole(e.getMessage());
                }
            }
        });

        sleepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawCanvas.axes.changePosition(90, 60, 33, 25, 0, 60);
                bluetoothConnection.sendMessage(new String[]{"190","260","333","425","50","660"});
                writeConsole("Roboter wird in Schlaf Position gefahren");
            }
        });

        drawCanvas.axes.changePosition(axisOne.getProgress(), axisTwo.getProgress(), axisThree.getProgress(), axisFour.getProgress(), axisFive.getProgress(), axisSix.getProgress());
        bluetoothConnection = new BluetoothConnection(this,"HC-05");
    }


    public void writeConsole(String text) {
        textView.setText(textView.getText() + "\r\n" + text);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        drawCanvas.axes.changePosition(axisOne.getProgress(),axisTwo.getProgress(),axisThree.getProgress(),axisFour.getProgress(),axisFive.getProgress(),axisSix.getProgress());
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

        if(seekBar.equals(axisSix)){
            bluetoothConnection.sendMessage("6"+axisSix.getProgress());
            writeConsole("6 Achse auf "+axisSix.getProgress()+" Grad");
        }
        if(seekBar.equals(axisFive)){
            bluetoothConnection.sendMessage("5"+axisFive.getProgress());
            writeConsole("5 Achse auf "+axisFive.getProgress()+" Grad");
        }
        if(seekBar.equals(axisFour)){
            bluetoothConnection.sendMessage("4"+axisFour.getProgress());
            writeConsole("4 Achse auf "+axisFour.getProgress()+" Grad");
        }
        if(seekBar.equals(axisThree)){
            bluetoothConnection.sendMessage("3"+axisThree.getProgress());
            writeConsole("3 Achse auf "+axisThree.getProgress()+" Grad");
        }
        if(seekBar.equals(axisTwo)){
            bluetoothConnection.sendMessage("2"+axisTwo.getProgress());
            writeConsole("2 Achse auf "+axisTwo.getProgress()+" Grad");
        }
        if(seekBar.equals(axisOne)){
            bluetoothConnection.sendMessage("1"+axisOne.getProgress());
            writeConsole("1 Achse auf "+axisOne.getProgress()+" Grad");
        }
        if (seekBar.equals(delay)){
            bluetoothConnection.sendMessage("d" + (seekBar.getProgress() + 20));
            writeConsole("Delay wurde auf " + (seekBar.getProgress() + 20) + " ms gestellt");
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked){
            if(buttonView.equals(greiferCloseAndOpen)){
                bluetoothConnection.sendMessage("670");
                writeConsole("6 Achse auf 70 Grad");
            }
        }
        if(buttonView.equals(greiferCloseAndOpen)){
            bluetoothConnection.sendMessage("60");
            writeConsole("6 Achse auf 0 Grad");
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.item1:
                fragment = fragment1;
                break;
            case R.id.item2:
                fragment = fragment2;
                break;
            case R.id.item3:
                fragment = fragment3;
                break;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
        return true;
    }
}