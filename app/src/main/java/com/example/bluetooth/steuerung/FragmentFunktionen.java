package com.example.bluetooth.steuerung;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.bluetooth.R;
import com.example.bluetooth.steuerung.simulation.Axes;
import com.example.bluetooth.steuerung.simulation.DrawCanvas;
import org.jetbrains.annotations.NotNull;
/**
 * @author Matthias Fichtinger
 * @version 11.03.2022
 * Beinhaltet grundlegende Funktionen des Roboters
 */
public class FragmentFunktionen extends Fragment{
    /**
     * Referenz zu MainActivity
     */
    public MainActivity mainActivity;
    /**
     * Variable für View damit dieser nur inemal erstellt werden muss
     */
    public View view = null;
    /**
     * Boolean Variable damit gepesichert werden kann ob der View schon erstellt wurde
     */
    public boolean isCreated = false;
    /**
     * Seekbar zum Einstellen des Delays vom Roboterarm
     */
    public SeekBar delay;
    /**
     * Buttons für die Home- und Schlafposition
     */
    public Button buttonHome;
    public Button buttonSleep;
    /**
     * Switch für das Öffnen und Schließen des Greifers
     */
    public Switch switchGreifer;
    /**
     * RelativeLayout damit die Simulation des Roboters angezeigt werden kann
     */
    public RelativeLayout relativeLayout;
    /**
     * DrawCanvas Instanz für die Simulation ertsellen
     */
    public DrawCanvas drawCanvas;
    /**
     * Referenz zu den Achsenwerten
     */
    public Axes axes;
    /**
     * Referenz zur Bluetoothverbindung
     */
    public BluetoothSteuerung bluetoothSteuerung;
    /**
     * @param mainActivity
     * @param bluetoothSteuerung
     * @param axes
     * Beim Konstruktor dieser Klasse werden die MainActivity, die BluetoothSteuerung und die Referenz
     * zu Axes übergeben.
     */
    public FragmentFunktionen(MainActivity mainActivity, BluetoothSteuerung bluetoothSteuerung, Axes axes){
        this.mainActivity = mainActivity;
        this.axes = axes;
        this.bluetoothSteuerung = bluetoothSteuerung;
    }
    /**
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     * Die onCreateView Methode wird beim erstellen dieses Views aufgerufen, dabei setzten wir falls der
     * View noch null ist unser XML-File. Danach wird der View zurückgegeben.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view == null) {
            view = inflater.inflate(R.layout.fragment_funktionen, container, false);
        }
        return view;
    }
    /**
     * @param view
     * @param savedInstanceState
     * Wenn der View erstellt worden ist, wird die onViewCreated Methode aufgerufen. Diese wird aber nur beim ersten
     * mal aufgerufen. In der Methode werden alle Eigenschfaten die deklariert worden sind initialisiert und Events wenn
     * nötig hinzugefügt.
     */
    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        if(isCreated){
            return;
        }
        isCreated = true;

        delay = getView().findViewById(R.id.delay);

        buttonHome = getView().findViewById(R.id.homeButton);
        buttonSleep = getView().findViewById(R.id.sleepButton);

        switchGreifer = getView().findViewById(R.id.switchGreifer);

        relativeLayout = getView().findViewById(R.id.relativeLayoutFunktionen);
        drawCanvas = new DrawCanvas(this.getContext(),axes);
        relativeLayout.addView(drawCanvas);
        /**
         * Die Seekbar für das Delay des Roboterarms erhält einen Listener. Wenn der Benutzer
         * den Wert ändert wird die Änderung nach dem Loslassen der Seekbar an den Roboter
         * übermittelt. Zusätzlich wird dies in der Konsole mitdokumentiert.
         */
        delay.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                bluetoothSteuerung.sendMessage("d" + (seekBar.getProgress() + 20));
                mainActivity.fragmentKonsole.writeConsole("Delay wurde auf " + (seekBar.getProgress() + 20) + " ms gestellt");
            }
        });
        /**
         * Wenn der Button für die Homeposition gedrückt wird, wird die Simulation des Roboter
         * aktualisiert und an den Roboter geschickt. Wird ebenfalls in der Konsole dokumentiert.
         */
        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawCanvas.axes.changePosition(90, 120, 25, 50, 0, 60);
                drawCanvas.invalidate();
                bluetoothSteuerung.sendMessage(new String[]{"190","2120","325","450","50","660"});
                mainActivity.fragmentKonsole.writeConsole("Roboter wird in Home Position gefahren");
            }
        });
        /**
         * Wenn der Button für die Schlafposition gedrückt wird, wird die Simulation des Roboter
         * aktualisiert und an den Roboter geschickt. Wird ebenfalls in der Konsole dokumentiert.
         */
        buttonSleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawCanvas.axes.changePosition(90, 60, 33, 25, 0, 60);
                drawCanvas.invalidate();
                bluetoothSteuerung.sendMessage(new String[]{"190","260","333","425","50","660"});
                mainActivity.fragmentKonsole.writeConsole("Roboter wird in Schlaf Position gefahren");
            }
        });
        /**
         * Wenn der Switch für den Griefer gedrückt wird, wird ein Erieignis ausgelöst.
         * Dabei wird darauf geachtet, ob dieser aktiviert oder deaktiviert wird. Mit dem
         * Boolean der in der Funktion übergeben wird, kann dies überprüft werden. Wenn der
         * Switch aktiviert wurde, soll der Greifer geschlossen werden, wenn er deaktviert wurde,
         * soll der Greifer geöffnet werden. Beides wird in der Konsole dokumentiert.
         */
        switchGreifer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    bluetoothSteuerung.sendMessage("670");
                    mainActivity.fragmentKonsole.writeConsole("6 Achse auf 70 Grad");
                    return;
                }
                bluetoothSteuerung.sendMessage("60");
                mainActivity.fragmentKonsole.writeConsole("6 Achse auf 0 Grad");
            }
        });
    }
}
