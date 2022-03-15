package com.example.bluetooth.programme.einstellungen;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.bluetooth.R;
import com.example.bluetooth.programme.ProgrammActivity;
import com.example.bluetooth.steuerung.ControllerChoose;

public class ConsoleFragment extends Fragment implements View.OnClickListener {

    View view;
    EinstellungenFragment einstellungenFragment;

    TextView textViewKonsole;

    ImageButton btnBack;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_console, container, false);
        init();
        return view;
    }
    private void init(){
        einstellungenFragment=new EinstellungenFragment();

        textViewKonsole=view.findViewById(R.id.textViewBearbeitenKonsole);
        textViewKonsole.setText(Console.getConsoleText());

        btnBack=view.findViewById(R.id.btnConsoleBack);
        btnBack.setOnClickListener(this);
    }

    @Override

    public void onClick(View v) {
        if(v.equals(btnBack)){
            Intent intent = new Intent(getActivity(), ControllerChoose.class);
            startActivity(intent);
        }
    }
}