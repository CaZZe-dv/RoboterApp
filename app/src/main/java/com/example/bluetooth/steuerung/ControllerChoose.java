package com.example.bluetooth.steuerung;

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
/**
 * @author Matthias Fichtinger
 * @version 11.03.2022
 * Die Klasse ControllerChoose kümmert sich um die Auswahl zwischen
 * Programmen und der Steuerung des Roboters
 */
public class ControllerChoose extends AppCompatActivity {
    /**
     * Button für das Bestätigen den Modus
     */
    public Button auswahlButton;
    /**
     * RadioButton für die Programme
     */
    public RadioButton programmeButton;
    /**
     * RadioButton für die Steuerung
     */
    public RadioButton controllerButton;
    /**
     * TextView in dem eine Kurzbeschreibung des ausgewählten Modus
     * angezeigt wird.
     */
    public TextView beschreibungModus;
    /**
     * RadioGroup, damit immer nur ein RadioButton aktiv sein kann
     */
    public RadioGroup radioGroup;
    /**
     * Der AlertDialog.Builder und der AlertDialog werden beide benötigt für das Popup-Fenster
     * das den Benutzer darauf hinweist, dass das Verbinden mit dem Bluetooth-Modul
     * einige Sekunden dauern könnte
     */
    public AlertDialog.Builder builder;
    public AlertDialog alert;

    /**
     * @param savedInstanceState
     * In der Methode onCreate werden alle Eigenschaften, die deklariert wurden initialisiert.
     * ZUsätzlich wurden den Komponenten die Ereignisse ausführen wurden Listeners hinzugefügt.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auswahl);

        auswahlButton = findViewById(R.id.ChooseButton);
        programmeButton = findViewById(R.id.Programms);
        controllerButton = findViewById(R.id.Controller);
        beschreibungModus = findViewById(R.id.Tooltip);
        radioGroup = findViewById(R.id.radioGroup);

        builder = new AlertDialog.Builder(this);
        builder.setMessage("Verbindung zum Bluetooth wird aufgebaut, kann einige Sekunden dauern!");
        builder.setCancelable(true);
        builder.setTitle("Hinweis");
        builder.setPositiveButton("OK",null);
        alert = builder.create();
        /**
         * Dem RadioButton für den Controller wird ein ClickListener hinzugefügt. Wenn dieser gedrückt wird, wird
         * er aufgrund der RadioGroup als ausgewählt angezeigt. Im TextView für die
         * Beschreibung wird der Text passend zum Modus verändert.
         */
        controllerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controllerButton.setChecked(true);
                beschreibungModus.setText("Mit diesem Modus können Sie den Roboter steuern");
            }
        });
        /**
         * Dem RadioButton für die Programme wird ein ClickListener hinzugefügt. Wenn dieser gedrückt wird, wird
         * er aufgrund der RadioGroup als ausgewählt angezeigt. Im TextView für die
         * Beschreibung wird der Text passend zum Modus verändert.
         */
        programmeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                programmeButton.setChecked(true);
                beschreibungModus.setText("Mit diesem Modus können Sie Programme erstellen und abspielen");
            }
        });
        /**
         * Der Button für das Bestätigen der Auswahl hat ebenfalls einen ClickListener. Wenn dieser
         * gedrückt wurde, wird sich eine Referenz zum Ausgewählten RadioButton in der RadioGroup gemacht,
         * anschließend wird überprüft welcher der beiden Buttons der Referenz gleich ist und der Modus wird
         * gewechselt. Anschließend öffnet sich ein Pupup-Fenster das darauf hinweist, dass das Verbinden zum
         * Bluetooth-Modul einige Sekunden dauern könnte.
         */
        auswahlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton r = findViewById(radioGroup.getCheckedRadioButtonId());
                if(r.equals(controllerButton)) {
                    switchToMainActivity();
                }else if(r.equals(programmeButton)){
                    switchToProgrammActivity();
                }
                alert.show();
            }
        });
    }
    /**
     * Mit der Methode switchToMainActivity wird zur Steuerung des Roboter gewechselt.
     * Dabei wird ein neues Intent mit der Klasse MainActivity erstellt und gestartet.
     */
    public void switchToMainActivity(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
    /**
     * Mit der Methode switchToProgrammActivity wird zu den Programmen des Roboter gewechselt.
     * Dabei wird ein neues Intent mit der Klasse ProgrammActivity erstellt und gestartet.
     */
    public void switchToProgrammActivity(){
        Intent intent = new Intent(this, ProgrammActivity.class);
        startActivity(intent);
    }
}