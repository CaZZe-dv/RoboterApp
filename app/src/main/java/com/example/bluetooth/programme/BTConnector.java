package com.example.bluetooth.programme;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.ParcelUuid;

import java.io.InputStream;
import java.io.OutputStream;

public class BTConnector {
    //Methoden für die Verbindung mit dem Bluetooth Modul
    private BluetoothAdapter bt;
    private OutputStream outputStream;
    private InputStream inputStream;
    public BTConnector(){
        //connectBluetooth();
    }

    private void connectBluetooth(){
        try {
            bt = BluetoothAdapter.getDefaultAdapter();
            for(BluetoothDevice device : bt.getBondedDevices()){
                if(device.getName().equals("HC-05")){
                    ParcelUuid[] uuids = device.getUuids();
                    BluetoothSocket socket = device.createRfcommSocketToServiceRecord(uuids[0].getUuid());
                    socket.connect();
                    outputStream = socket.getOutputStream();
                    inputStream = socket.getInputStream();
                    //writeConsole("Verbindung mit Bluetooth erfolgreich");
                }
            }
        }catch (Exception e){
            //writeConsole("Verbindung mit Bluetooth fehlgeschlagen");
        }
    }
    public void write(String s){
        try{
            s += ".";
            outputStream.write(s.getBytes());
        }catch (Exception e){
            //writeConsole("Senden der Anweisung hat nicht geklappt");
        }

    }
}
