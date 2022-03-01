package com.example.bluetooth.steuerung;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bluetooth.R;
import com.example.bluetooth.programme.ProgrammActivity;

//Kümmert sich um die Steuerung des Auswählens
public class ControllerChoose extends AppCompatActivity {
    //Button für das Auswählen des Modus
    public Button chooseButton;
    //RadioButton für die Programme
    public RadioButton programms;
    //RadioButton für den Controller
    public RadioButton controller;
    //Wenn ein RadioButton ausgewählt ist, dann wird in diesem TextView eine
    //kurze Beschreibung angezeigt
    public TextView toolTip;
    //RadioGroup damit immer nur ein RadioButton aktiv sein kann
    public RadioGroup radioGroup;

    public AlertDialog.Builder builder;
    public AlertDialog alert;
    //Methode onCreate der Überklasse überschreiben
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //onCreate MEthode der Überklasse aufrufen
        super.onCreate(savedInstanceState);
        //Als Content unser XML-File für Choose setzen
        setContentView(R.layout.choose);
        //Eigenschaften chooseButton, programms, controller, toolTip und radioGroup deklarieren
        chooseButton = findViewById(R.id.ChooseButton);
        programms = findViewById(R.id.Programms);
        controller = findViewById(R.id.Controller);
        toolTip = findViewById(R.id.Tooltip);
        radioGroup = findViewById(R.id.radioGroup);

        builder = new AlertDialog.Builder(this);
        builder.setMessage("Verbindung zum Bluetooth wird aufgebaut, kann einige Sekunden dauern!");
        builder.setCancelable(true);
        builder.setTitle("Hinweis");
        builder.setPositiveButton("OK",null);
        alert = builder.create();
        //Dem Controller RadioButton einen ClickListener hinzufügen
        controller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setChecked auf true setzen und dem TextView toolTip eine kurze
                //Beschreibung für den Modus Hinzufügen
                controller.setChecked(true);
                toolTip.setText("Mit diesem Modus können Sie den Roboter steuern");
            }
        });
        //Dem Programms RadioButton einen ClickListener hinzufügen
        programms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setChecked auf true setzen und dem TextView toolTip eine kurze
                //Beschreibung für den Modus Hinzufügen
                programms.setChecked(true);
                toolTip.setText("Mit diesem Modus können Sie Programme erstellen und abspielen");
            }
        });
        //dem chooseButton einen ClickListener hinzufügen
        chooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Aus der RadioGroup den ausgwählten RadioButton herrausziehen
                RadioButton r = findViewById(radioGroup.getCheckedRadioButtonId());
                //Den Radiobutton mit dem Controller und Programms vergleichen und dann
                //je nach dem welcher RadioButton ausgewählt wurde wird das nächste Fenster gestartet
                if(r.equals(controller)) {//Controller Modus
                    switchToMainActivity();
                }else if(r.equals(programms)){//Programm Modus
                    switchToProgrammActivity();
                }
                alert.show();
            }
        });
    }
    //Zum Controller XML-File wechseln
    public void switchToMainActivity(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
    //Zum Programm XML-File wechseln
    public void switchToProgrammActivity(){
        Intent intent = new Intent(this, ProgrammActivity.class);
        startActivity(intent);
    }
}