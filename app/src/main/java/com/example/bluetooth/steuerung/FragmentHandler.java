package com.example.bluetooth.steuerung;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bluetooth.R;

import org.jetbrains.annotations.NotNull;
//Wird für das Anzeigen der Fragmente Benötigt
public class FragmentHandler extends Fragment {
    //Jedes Fragment besteht aus einer id, die das XML-File beschreibt das anzuzeigen ist
    //einer Referanz zur MainActivity,
    public int id;
    public MainActivity mainActivity;
    public static int viewsCreated = 0;
    public View view = null;
    public boolean isCreated = false;

    public FragmentHandler(int id, MainActivity mainActivity){
        this.id = id;
        this.mainActivity = mainActivity;
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @org.jetbrains.annotations.NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        if(view == null) {
            view = inflater.inflate(id, container, false);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        if(isCreated){
            return;
        }
        isCreated = true;
        super.onViewCreated(view, savedInstanceState);
        switch (id){
            case R.layout.fragment_bewegen:
                mainActivity.axisSix = getView().findViewById(R.id.AxisSix);
                mainActivity.axisFive = getView().findViewById(R.id.AxisFive);
                mainActivity.axisFour = getView().findViewById(R.id.AxisFour);
                mainActivity.axisThree = getView().findViewById(R.id.AxisThree);
                mainActivity.axisTwo = getView().findViewById(R.id.AxisTwo);
                mainActivity.axisOne = getView().findViewById(R.id.AxisOne);
                break;
            case R.layout.fragment_funktionen:
                mainActivity.delay = getView().findViewById(R.id.delay);
                mainActivity.homeButton = getView().findViewById(R.id.homeButton);
                mainActivity.sleepButton = getView().findViewById(R.id.sleepButton);
                mainActivity.switchGreifer = getView().findViewById(R.id.switchGreifer);
                break;
            case R.layout.fragment_konsole:
                mainActivity.textView = getView().findViewById(R.id.ConsolenView);
                mainActivity.scrollViewConsole = getView().findViewById(R.id.scrollViewConsole);
                break;
        }
        viewsCreated++;
        if(viewsCreated == 3) {
            mainActivity.addListeners();
        }
    }
}
