package com.example.bluetooth.programme;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.bluetooth.R;
import com.example.bluetooth.programme.database.Connector;

public class ErstellenFragment extends Fragment{

    View view;

    Connector connector;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_erstellen, container, false);
        init();
        return view;
    }
    private void init(){
        connector=new Connector(view.getContext());

    }
}