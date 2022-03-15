package com.example.bluetooth.programme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.bluetooth.R;
import com.example.bluetooth.programme.einstellungen.Console;
import com.example.bluetooth.programme.einstellungen.ConsoleFragment;
import com.example.bluetooth.programme.einstellungen.EinstellungenFragment;
import com.example.bluetooth.programme.erstellen.ErstellenFragment;
import com.example.bluetooth.programme.liste.ProgrammeFragment;
import com.example.bluetooth.programme.robot.BTConnector;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

public class ProgrammActivity extends AppCompatActivity {

    public BottomNavigationView bottomNavigationView;

    public ProgrammeFragment fragmentProgramme;
    public ErstellenFragment fragmentErstellen;
    public ConsoleFragment fragmentKonsole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programm);

        fragmentProgramme = new ProgrammeFragment();
        fragmentErstellen = new ErstellenFragment();
        fragmentKonsole = new ConsoleFragment();


        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayout_programm, fragmentKonsole).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayout_programm, fragmentErstellen).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayout_programm, fragmentProgramme).commit();

        bottomNavigationView = findViewById(R.id.bottomNavigation_Programm);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                Fragment fragment = null;

                switch (item.getItemId()) {
                    case R.id.menuitem_programme:
                        fragment = fragmentProgramme;
                        break;
                    case R.id.menuitem_erstellen:
                        fragment = fragmentErstellen;
                        break;
                    case R.id.menuitem_konsole:
                        fragment = fragmentKonsole;
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayout_programm, fragment).commit();
                return true;
            }
        });

        //Bluetooth & Console
        Console.init();
        BTConnector.init();
    }
}