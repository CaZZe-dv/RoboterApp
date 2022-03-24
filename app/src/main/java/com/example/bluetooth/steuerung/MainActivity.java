package com.example.bluetooth.steuerung;

import android.content.Intent;
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
/**
 * @author Matthias Fichtinger
 * @version 11.03.2022
 * Kümmert sich um die Steuerung des Roboters und um das
 * Anzeigen der Fragmente
 */
public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    /**
     * BottomNavigationBar für das Umschalten zwischen den Fragmenten
     */
    public BottomNavigationView bottomNavigationView;
    /**
     * Die drei Fragmente zwischen diesen gewechselt werden kann
     */
    public FragmentBewegen fragmentBewegen;
    public FragmentFunktionen fragmentFunktionen;
    public FragmentKonsole fragmentKonsole;
    /**
     * Objekt für die Bluetooth-Verbindung mit dem Arduino
     */
    public static BluetoothSteuerung bluetoothSteuerung;
    /**
     * Obejekt für die Simulation
     */
    public static Axes axes;
    /**
     * Eine statische Instanz der Klasse damit auf diese ohne Objekt zugegriffen
     * werden kann
     */
    public static MainActivity instanz;
    /**
     * Button damit jeder Zeit zur Controller-Ansicht gewechselt werden kann
     */
    public FloatingActionButton buttonController;

    /**
     * @param savedInstanceState
     * Die deklarierten Eigenschaften werden initialisiert
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steuerung);

        instanz = this;
        bluetoothSteuerung = new BluetoothSteuerung(this,"HC-05");

        axes = new Axes();

        fragmentBewegen = new FragmentBewegen(this,bluetoothSteuerung, axes);
        fragmentFunktionen = new FragmentFunktionen(this,bluetoothSteuerung, axes);
        fragmentKonsole = new FragmentKonsole(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragmentKonsole).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragmentFunktionen).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragmentBewegen).commit();

        bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        buttonController = findViewById(R.id.buttonController);
        /**
         * Wenn der Button gedrückt wird, wird die Methode switchToController
         * ausgeführt
         */
        buttonController.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToController();
            }
        });
    }

    /**
     * @param item
     * @return
     * Wenn ein Item in der BottomNavigationBar ausgewählt wurde, wird überprüft
     * welchem Fragment es entspricht. Je nach dem wird dann das FrameLayout zu dem
     * Fragment geändert.
     */
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
    /**
     * Mit der Methode switchToController kann zur Controller-Ansicht gewechselt werden,
     * dabei wird ein neues Intent von der Klasse FragmentBewegenController erstellt und gestartet.
     */
    public void switchToController(){
        Intent intent = new Intent(this, FragmentBewegenController.class);
        startActivity(intent);
    }
}