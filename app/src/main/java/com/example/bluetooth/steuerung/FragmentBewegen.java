package com.example.bluetooth.steuerung;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class FragmentBewegen extends Fragment implements SeekBar.OnSeekBarChangeListener{
    //
    public MainActivity mainActivity;
    public View view = null;
    public boolean isCreated = false;
    //
    public SeekBar axisOne;
    public SeekBar axisTwo;
    public SeekBar axisThree;
    public SeekBar axisFour;
    public SeekBar axisFive;
    public SeekBar axisSix;
    //
    public Switch switchAnsicht;
    //
    public RelativeLayout relativeLayout;
    //
    public DrawCanvas drawCanvas;
    public Axes axes;
    //
    public BluetoothSteuerung bluetoothSteuerung;
    //
    public Intent intent;
    public FragmentBewegenKontroller fragmentBewegenKontroller;
    public FragmentBewegen(MainActivity mainActivity, BluetoothSteuerung bluetoothSteuerung, Axes axes){
        this.mainActivity = mainActivity;
        this.axes = axes;
        this.bluetoothSteuerung = bluetoothSteuerung;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view == null) {
            view = inflater.inflate(R.layout.fragment_bewegen, container, false);
        }
        return view;
    }

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

        switchAnsicht = getView().findViewById(R.id.switchKontroller);

        relativeLayout = getView().findViewById(R.id.relativLayoutBewegen);
        drawCanvas = new DrawCanvas(this.getContext(),axes);
        relativeLayout.addView(drawCanvas);

        axisOne.setOnSeekBarChangeListener(this);
        axisTwo.setOnSeekBarChangeListener(this);
        axisThree.setOnSeekBarChangeListener(this);
        axisFour.setOnSeekBarChangeListener(this);
        axisFive.setOnSeekBarChangeListener(this);
        axisSix.setOnSeekBarChangeListener(this);

        switchAnsicht.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mainActivity.switchToKontroller();
                }
            }
        });

        mainActivity.bluetoothSteuerung.connectBluetooth();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        drawCanvas.axes.changePosition(axisOne.getProgress(),axisTwo.getProgress(),axisThree.getProgress(),axisFour.getProgress(),axisFive.getProgress(),axisSix.getProgress());
        drawCanvas.invalidate();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

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
