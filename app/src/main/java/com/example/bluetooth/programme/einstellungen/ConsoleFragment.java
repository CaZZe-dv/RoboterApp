package com.example.bluetooth.programme.einstellungen;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.example.bluetooth.programme.robot.BTConnector;
import com.example.bluetooth.steuerung.ControllerChoose;

public class ConsoleFragment extends Fragment implements View.OnClickListener {

    View view;
    EinstellungenFragment einstellungenFragment;

    TextView textViewKonsole;
    TextView textViewBluetooth;

    ImageButton btnBack;
    Button btnReconnectBT;

    AlertDialog dialog;
    AlertDialog.Builder dialogBuilder;

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
        textViewBluetooth=view.findViewById(R.id.textViewConsoleBluetooth);

        btnBack=view.findViewById(R.id.btnConsoleBack);
        btnBack.setOnClickListener(this);
        btnReconnectBT=view.findViewById(R.id.btnReconnectBluetooth);
        btnReconnectBT.setOnClickListener(this);

        if(BTConnector.isConnected()){
            textViewBluetooth.setText("Bluetoothverbindung erfolgreich");
            btnReconnectBT.setEnabled(false);
        }else{
            textViewBluetooth.setText("Bluetoothverbindung fehlgeschlagen");
            btnReconnectBT.setEnabled(true);
        }

    }

    @Override

    public void onClick(View v) {
        if(v.equals(btnBack)){
            Intent intent = new Intent(getActivity(), ControllerChoose.class);
            startActivity(intent);
        }else if(v.equals(btnReconnectBT)){
            BTConnector.reconnect();
            if(BTConnector.isConnected()){
                dialogBluetooth("Bluetoothverbindung erfolgreich!");
                textViewBluetooth.setText("Bluetoothverbindung erfolgreich");
                btnReconnectBT.setEnabled(false);
            }else{
                dialogBluetooth("Bluetoothverbindung fehlgeschlagen!");
            }
        }
    }
    private void dialogBluetooth(String message){
        dialogBuilder = new android.app.AlertDialog.Builder(view.getContext());
        dialogBuilder.setMessage(message);
        dialogBuilder.setTitle("Hinweis");
        dialogBuilder.setCancelable(true);

        dialogBuilder.setPositiveButton("OK",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        dialog = dialogBuilder.create();
        dialog.show();
    }
}