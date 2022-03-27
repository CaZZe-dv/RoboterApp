package com.example.bluetooth.steuerung;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Parcelable;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.example.bluetooth.R;
import com.example.bluetooth.steuerung.simulation.Axes;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

//Kümmert sich um alle Bestandteile für die Steuerung des Roboters
public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    //BottomNavigationView ist da um zwischen den Fragmenten zu wechseln
    public BottomNavigationView bottomNavigationView;
    //Alle Fragmente von 1 bis 3
    public FragmentBewegen fragmentBewegen;
    public FragmentFunktionen fragmentFunktionen;
    public FragmentKonsole fragmentKonsole;
    //Bluetooth Verbindung mit Arduino
    public static BluetoothSteuerung bluetoothSteuerung;
    public static Axes axes;
    public static MainActivity instance;
    //
    public FloatingActionButton buttonController;
    //Überschreiben der onCreate Methode der Überklasse
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //onCreate Methoder der Überklasse aufrufen
        super.onCreate(savedInstanceState);
        //Als Content das XML-File für die Activity_Main setzen
        setContentView(R.layout.activity_steuerung);
        //
        instance = this;
        //Bluetooth Verbindung erstellen
        bluetoothSteuerung = new BluetoothSteuerung(this,"HC-05");
        //
        axes = new Axes();
        //Fragmente von 1 bis 3 deklarieren
        fragmentBewegen = new FragmentBewegen(this,bluetoothSteuerung, axes);
        fragmentFunktionen = new FragmentFunktionen(this,bluetoothSteuerung, axes);
        fragmentKonsole = new FragmentKonsole(this);
        //Fragmente einmal aufrufen
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragmentKonsole).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragmentFunktionen).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragmentBewegen).commit();
        //Referenz zu BottomNAvigation erstellen
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        //BottomNAvigation einen setOnNAvigationItemSelectedListener hinzufügen
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        //
        buttonController = findViewById(R.id.buttonController);
        buttonController.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToController();
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.item1:
                fragment = fragmentBewegen;
                break;
            case R.id.item2:
                fragment = fragmentFunktionen;
                break;
            case R.id.item3:
                fragment = fragmentKonsole;
                break;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
        return true;
    }

    public void switchToController(){
        Intent intent = new Intent(this,FragmentBewegenKontroller.class);
        startActivity(intent);
    }
}