package com.example.bluetooth.programme.einstellungen;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.bluetooth.R;
import com.example.bluetooth.programme.robot.BTConnector;
import com.example.bluetooth.programme.tcp.RPoint;
import com.example.bluetooth.programme.tcp.TCP;
import com.example.bluetooth.programme.tcp.TCPFragment;

public class EinstellungenFragment extends Fragment implements View.OnClickListener {

    View view;
    Fragment[] fragArr;

    Button btnKonsole;
    Button btnTemp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_einstellungen, container, false);
        init();
        return view;
    }
    private void init(){
        fragArr= new Fragment[]{new ConsoleFragment(),new TCPFragment()};

        btnKonsole=view.findViewById(R.id.btnKonsole);
        btnKonsole.setOnClickListener(this);
        btnTemp=view.findViewById(R.id.btnTempTCP);
        btnTemp.setOnClickListener(this);
    }

    private void switchFrag(int identifier){
        getFragmentManager().beginTransaction().replace(R.id.fragmentLayout_programm,fragArr[identifier]).commit();
    }

    @Override
    public void onClick(View view) {
        if(view.equals(btnKonsole)){
            //Konsole
            switchFrag(0);
        }
        if(view.equals(btnTemp))
            switchFrag(1);
    }
}