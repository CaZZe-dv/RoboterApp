package com.example.bluetooth;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import org.jetbrains.annotations.NotNull;

public class FragmentView extends Fragment {
    public int id;
    public MainActivity mainActivity;
    public static int viewsCreated = 0;
    public View view = null;
    public boolean isCreated = false;
    public FragmentView(int id, MainActivity mainActivity){
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
            case R.layout.fragment1:
                mainActivity.axisSix = getView().findViewById(R.id.AxisSix);
                mainActivity.axisFive = getView().findViewById(R.id.AxisFive);
                mainActivity.axisFour = getView().findViewById(R.id.AxisFour);
                mainActivity.axisThree = getView().findViewById(R.id.AxisThree);
                mainActivity.axisTwo = getView().findViewById(R.id.AxisTwo);
                mainActivity.axisOne = getView().findViewById(R.id.AxisOne);
                break;
            case R.layout.fragment2:
                mainActivity.switchAxisSix = getView().findViewById(R.id.SwitchAxisSix);
                mainActivity.switchAxisFive = getView().findViewById(R.id.SwitchAxisFive);
                mainActivity.switchAxisFour = getView().findViewById(R.id.SwitchAxisFour);
                mainActivity.switchAxisThree = getView().findViewById(R.id.SwitchAxisThree);
                mainActivity.switchAxisTwo = getView().findViewById(R.id.SwitchAxisTwo);
                mainActivity.switchAxisOne = getView().findViewById(R.id.SwitchAxisOne);
                break;
            case R.layout.fragment3:
                mainActivity.delay = getView().findViewById(R.id.Delay);
                mainActivity.homeButton = getView().findViewById(R.id.homeButton);
                break;
            case R.layout.fragment4:
                mainActivity.textView = getView().findViewById(R.id.ConsolenView);
                break;
        }
        viewsCreated++;
        if(viewsCreated == 4) {
            mainActivity.addListeners();
            mainActivity.connectBluetooth();
        }
    }
}
