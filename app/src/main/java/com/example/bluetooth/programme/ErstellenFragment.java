package com.example.bluetooth.programme;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.bluetooth.R;
import com.example.bluetooth.programme.database.Connector;

public class ErstellenFragment extends Fragment implements SeekBar.OnSeekBarChangeListener{

    View view;

    Connector connector;
    BTConnector btConnector;

    public SeekBar axisSix;
    public SeekBar axisFive;
    public SeekBar axisFour;
    public SeekBar axisThree;
    public SeekBar axisTwo;
    public SeekBar axisOne;

    public Button btnReset;
    public Button btnAddPoint;

    public TextView textViewProgrammName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_erstellen, container, false);
        init();
        return view;
    }
    private void init(){
        connector=new Connector(view.getContext());
        btConnector=new BTConnector();
        //Seekbars
        axisSix = view.findViewById(R.id.AxisSix_Programme);
        axisFive = view.findViewById(R.id.AxisFive_Programme);
        axisFour = view.findViewById(R.id.AxisFour_Programme);
        axisThree = view.findViewById(R.id.AxisThree_Programme);
        axisTwo = view.findViewById(R.id.AxisTwo_Programme);
        axisOne = view.findViewById(R.id.AxisOne_Programme);

        axisSix.setOnSeekBarChangeListener(this);
        axisFive.setOnSeekBarChangeListener(this);
        axisFour.setOnSeekBarChangeListener(this);
        axisThree.setOnSeekBarChangeListener(this);
        axisTwo.setOnSeekBarChangeListener(this);
        axisOne.setOnSeekBarChangeListener(this);
        //Buttons
        btnReset = view.findViewById(R.id.btnResetProgramm);
        btnAddPoint = view.findViewById(R.id.btnAddPunkt);
        //TextView
        textViewProgrammName = view.findViewById(R.id.textViewProgrammname);
    }



    //SeekBar Listener
    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
    }
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if(seekBar.equals(axisSix)){
            //btConnector.write("6"+axisSix.getProgress());
            //writeConsole("6 Achse auf "+axisSix.getProgress()+" Grad");
        }
        if(seekBar.equals(axisFive)){
            //btConnector.write("5"+axisFive.getProgress());
            //writeConsole("5 Achse auf "+axisFive.getProgress()+" Grad");
        }
        if(seekBar.equals(axisFour)){
            //btConnector.write("4"+axisFour.getProgress());
            //writeConsole("4 Achse auf "+axisFour.getProgress()+" Grad");
        }
        if(seekBar.equals(axisThree)){
            //btConnector.write("3"+axisThree.getProgress());
            //writeConsole("3 Achse auf "+axisThree.getProgress()+" Grad");
        }
        if(seekBar.equals(axisTwo)){
            //btConnector.write("2"+axisTwo.getProgress());
            //writeConsole("2 Achse auf "+axisTwo.getProgress()+" Grad");
        }
        if(seekBar.equals(axisOne)){
            //btConnector.write("1"+axisOne.getProgress());
            //writeConsole("1 Achse auf "+axisOne.getProgress()+" Grad");
        }

    }
}