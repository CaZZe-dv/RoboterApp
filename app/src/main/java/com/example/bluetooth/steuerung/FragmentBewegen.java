package com.example.bluetooth.steuerung;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
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
 * Kümmert sich um die Steuerung des Robters mittels Seekbars
 */
public class FragmentBewegen extends Fragment implements SeekBar.OnSeekBarChangeListener{
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
     * Seekbars von eins bis sechs für das Steruern der einzelnen Achsen
     */
    public SeekBar axisOne;
    public SeekBar axisTwo;
    public SeekBar axisThree;
    public SeekBar axisFour;
    public SeekBar axisFive;
    public SeekBar axisSix;
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
     * Im Konstruktor der FragmentBewegen Klasse wird die MainActivity die BluetoothVerbindung und die Referenz zur
     * Axes Klasse übergeben
     */
    public FragmentBewegen(MainActivity mainActivity, BluetoothSteuerung bluetoothSteuerung, Axes axes){
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
            view = inflater.inflate(R.layout.fragment_steuern, container, false);
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
        axisSix = getView().findViewById(R.id.AxisSix);
        axisFive = getView().findViewById(R.id.AxisFive);
        axisFour = getView().findViewById(R.id.AxisFour);
        axisThree = getView().findViewById(R.id.AxisThree);
        axisTwo = getView().findViewById(R.id.AxisTwo);
        axisOne = getView().findViewById(R.id.AxisOne);

        relativeLayout = getView().findViewById(R.id.relativLayoutBewegen);
        drawCanvas = new DrawCanvas(this.getContext(),axes);
        relativeLayout.addView(drawCanvas);

        axisOne.setOnSeekBarChangeListener(this);
        axisTwo.setOnSeekBarChangeListener(this);
        axisThree.setOnSeekBarChangeListener(this);
        axisFour.setOnSeekBarChangeListener(this);
        axisFive.setOnSeekBarChangeListener(this);
        axisSix.setOnSeekBarChangeListener(this);

        mainActivity.bluetoothSteuerung.connectBluetooth();
    }

    /**
     * @param seekBar
     * @param progress
     * @param fromUser
     * Wenn sich der Wert der Seekbar verändert hat wird diese Methode aufgerfuen, dabei wird aber nur die
     * Simulation des Roboters geändert. Dies geschieht mittels der Methode chnagePosition. Anschließend
     * muss die Simulation neu gezeichnet werden, mithilfe der Methode invalidate kann mna dies tun.
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        drawCanvas.axes.changePosition(axisOne.getProgress(),axisTwo.getProgress(),axisThree.getProgress(),
                                       axisFour.getProgress(),axisFive.getProgress(),axisSix.getProgress());
        drawCanvas.invalidate();
    }

    /**
     * @param seekBar
     * Diese Methode die ebenfalls vom Interface implementiert wurde, wird nicht benötigt und
     * ist daher leer.
     */
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    /**
     * @param seekBar
     * Wenn der Benuterz die Seekbar zur gewünschten Position gebracht hat, wird diese
     * Methode aufgerufen. Dabei wird die veränderte Seekbar übergeben. Anschließend wird überprüft
     * welcher Seekbar diese entspricht. Dementsprechend wird dem Roboterarm die neue Position übermittelt.
     * Außerdem wird der Vorgang in der Konsole mitdokumentiert.
     */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

        if(seekBar.equals(axisSix)){
            bluetoothSteuerung.sendMessage("6"+axisSix.getProgress());
            mainActivity.fragmentKonsole.writeConsole("6 Achse auf "+axisSix.getProgress()+" Grad");
        }
        if(seekBar.equals(axisFive)){
            bluetoothSteuerung.sendMessage("5"+axisFive.getProgress());
            mainActivity.fragmentKonsole.writeConsole("5 Achse auf "+axisFive.getProgress()+" Grad");
        }
        if(seekBar.equals(axisFour)){
            bluetoothSteuerung.sendMessage("4"+axisFour.getProgress());
            mainActivity.fragmentKonsole.writeConsole("4 Achse auf "+axisFour.getProgress()+" Grad");
        }
        if(seekBar.equals(axisThree)){
            bluetoothSteuerung.sendMessage("3"+axisThree.getProgress());
            mainActivity.fragmentKonsole.writeConsole("3 Achse auf "+axisThree.getProgress()+" Grad");
        }
        if(seekBar.equals(axisTwo)){
            bluetoothSteuerung.sendMessage("2"+axisTwo.getProgress());
            mainActivity.fragmentKonsole.writeConsole("2 Achse auf "+axisTwo.getProgress()+" Grad");
        }
        if(seekBar.equals(axisOne)){
            bluetoothSteuerung.sendMessage("1"+axisOne.getProgress());
            mainActivity.fragmentKonsole.writeConsole("1 Achse auf "+axisOne.getProgress()+" Grad");
        }
    }
}
