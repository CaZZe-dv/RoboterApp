package com.example.bluetooth.programme.einstellungen;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.bluetooth.R;
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
    public void onClick(View view) {
        getFragmentManager().beginTransaction().replace(R.id.fragmentLayout_programm,einstellungenFragment).commit();
    }
}