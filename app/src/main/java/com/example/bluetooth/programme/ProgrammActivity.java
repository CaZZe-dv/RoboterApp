package com.example.bluetooth.programme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.bluetooth.R;
import com.example.bluetooth.programme.einstellungen.Console;
import com.example.bluetooth.programme.einstellungen.ConsoleFragment;
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
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programm);

        fragmentProgramme = new ProgrammeFragment();
        fragmentErstellen = new ErstellenFragment();
        fragmentKonsole = new ConsoleFragment();


        getSupportFragmentManager().beginTransaction().replace(R.id.fragLayout_activity_programm, fragmentKonsole).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragLayout_activity_programm, fragmentErstellen).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragLayout_activity_programm, fragmentProgramme).commit();

        bottomNavigationView = findViewById(R.id.botNav_prog_programm);

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
                getSupportActionBar().show();
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragLayout_activity_programm, fragment).commit();
                return true;
            }
        });

        //Bluetooth & Console
        Console.init();
        BTConnector.init();
    }
}