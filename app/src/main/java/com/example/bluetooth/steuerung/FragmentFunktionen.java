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

public class FragmentFunktionen extends Fragment{
    //
    public MainActivity mainActivity;
    public View view = null;
    public boolean isCreated = false;
    //
    public SeekBar delay;
    //
    public Button buttonHome;
    public Button buttonSleep;
    //
    public Switch switchGreifer;
    //
    public RelativeLayout relativeLayout;
    //
    public DrawCanvas drawCanvas;
    public Axes axes;
    //
    public BluetoothSteuerung bluetoothSteuerung;
    //
    public FragmentFunktionen(MainActivity mainActivity, BluetoothSteuerung bluetoothSteuerung, Axes axes){
        this.mainActivity = mainActivity;
        this.axes = axes;
        this.bluetoothSteuerung = bluetoothSteuerung;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view == null) {
            view = inflater.inflate(R.layout.fragment_funktionen, container, false);
        }
        return view;
    }

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

        delay.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (seekBar.equals(delay)){
                    bluetoothSteuerung.sendMessage("d" + (seekBar.getProgress() + 20));
                    mainActivity.fragmentKonsole.writeConsole("Delay wurde auf " + (seekBar.getProgress() + 20) + " ms gestellt");
                }
            }
        });

        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawCanvas.axes.changePosition(90, 120, 25, 50, 0, 60);
                drawCanvas.invalidate();
                bluetoothSteuerung.sendMessage(new String[]{"190","2120","325","450","50","660"});
                mainActivity.fragmentKonsole.writeConsole("Roboter wird in Home Position gefahren");
            }
        });

        buttonSleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawCanvas.axes.changePosition(90, 60, 33, 25, 0, 60);
                drawCanvas.invalidate();
                bluetoothSteuerung.sendMessage(new String[]{"190","260","333","425","50","660"});
                mainActivity.fragmentKonsole.writeConsole("Roboter wird in Schlaf Position gefahren");
            }
        });

        switchGreifer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    bluetoothSteuerung.sendMessage("670");
                    mainActivity.fragmentKonsole.writeConsole("6 Achse auf 70 Grad");
                }
                bluetoothSteuerung.sendMessage("60");
                mainActivity.fragmentKonsole.writeConsole("6 Achse auf 0 Grad");
            }
        });
    }
}
