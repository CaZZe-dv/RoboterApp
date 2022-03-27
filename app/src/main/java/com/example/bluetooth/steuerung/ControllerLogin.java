package com.example.bluetooth.steuerung;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.bluetooth.R;
/**
 * @author Matthias Fichtinger
 * @version 11.03.2022
 * Die Klasse ControllerLogin kümmert sich um die Anmeldemaske
 */
public class ControllerLogin extends AppCompatActivity {
    /**
     * Button zum Bestätigen der Anmeldedaten
     */
    public Button loginButton;
    /**
     * Input-Feld für den Benutzernamen
     */
    public TextView benutzername;
    /**
     * Input-Feld für das Passwort
     */
    public TextView passwort;
    /**
     * Der AlertDialog.Builder und der AlertDialog werden beide benötigt für das Popup-Fenster
     * das erscheint wenn die Benutzerdaten falsch eingegeben wurden.
     */
    public AlertDialog.Builder builder;
    public AlertDialog alert;
    /**
     * @param savedInstanceState
     * In der onCreate Methode wird das login.xml als Content gesetzt und alle Eigenschaften
     * die oben deklariert worden sind initialisiert. Dem Button für das Login wird ein Event
     * hinzugefügt.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        benutzername = findViewById(R.id.username);
        passwort = findViewById(R.id.password);

        benutzername.setText("admin");
        passwort.setText("admin");

        builder = new AlertDialog.Builder(this);
        builder.setMessage("Benutzername oder Passwort sind falsch, bitte versuchen Sie es erneut!");
        builder.setCancelable(true);
        builder.setTitle("Hinweis");
        builder.setPositiveButton("OK",null);

        alert = builder.create();

        loginButton = findViewById(R.id.loginButton);
        /**
         * Damit die Daten auf Richtigkeit überprüft werden können, wurde dem loginButton
         * ein ClickListener hinzugefügt. Beim KLicken auf den Button soll überprüft werden,
         * ob der Benutzername als auch das Passwort dem String admin entsprechen. Ist das der
         * Fall wird die Methode switchToChoose aufgerufen. Ist das nicht der Fall, erscheint ein
         * Popup-Fenster.
         */
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (benutzername.getText().toString().equals("admin")
                        && passwort.getText().toString().equals("admin")) {
                    switchToChoose();
                    return;
                }
                alert.show();
            }
        });
    }
    /**
     * Mit der Methode switchToChoose kann zum nächsten Fenster, dem Auswhalfenster
     * gewechselt werden, dabei wird en neues Intent von der Klasse ControllerChoose
     * erstellt und gestartet.
     */
    public void switchToChoose(){
        Intent intent = new Intent(this, ControllerChoose.class);
        startActivity(intent);
    }
}
