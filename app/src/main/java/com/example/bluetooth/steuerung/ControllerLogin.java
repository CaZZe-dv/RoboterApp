package com.example.bluetooth.steuerung;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bluetooth.R;

//Kümmert sich um die Steuerung des Einloggens
public class ControllerLogin extends AppCompatActivity {
    //Button zum Bestätigen der Anmeldedaten
    public Button loginButton;
    //Input Feld für den Benutzernamen
    public TextView username;
    /**
     * Input Feld für das Passwort
     */
    public TextView password;

    public AlertDialog.Builder builder;
    public AlertDialog alert;
    //Methode onCreate überschreiben, damit man eigene Daten verwenden kann
    protected void onCreate(Bundle savedInstanceState) {
        //Methode der Überklasse AppCompatActivity aufrufen
        super.onCreate(savedInstanceState);
        //Als Content das XML-File für Login setzten
        setContentView(R.layout.login);
        //Eigenschaften username, password und loginButton deklarieren mit findViewById
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        username.setText("admin");
        password.setText("admin");

        builder = new AlertDialog.Builder(this);
        builder.setMessage("Benutzername oder Passwort sind falsch, bitte versuchen Sie es erneut!");
        builder.setCancelable(true);
        builder.setTitle("Hinweis");
        builder.setPositiveButton("OK",null);

        alert = builder.create();

        loginButton = findViewById(R.id.loginButton);
        //Dem Login Button einen ClickListener hinzufügen
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Kontrolliert ob der Username und Passwort gleich admin entsprechen
                if (username.getText().toString().equals("admin")
                        && password.getText().toString().equals("admin")) {
                    //Falls dies zutrifft wird zum nächsten Fenster gewechselt
                    switchToChoose();
                    return;
                }
                alert.show();
            }
        });
    }
    //Methode zum Wechseln zum ControllerChoose
    public void switchToChoose(){
        Intent intent = new Intent(this, ControllerChoose.class);
        startActivity(intent);
    }
}
