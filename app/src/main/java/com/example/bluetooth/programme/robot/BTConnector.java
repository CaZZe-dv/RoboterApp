package com.example.bluetooth.programme.robot;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.ParcelUuid;

import com.example.bluetooth.programme.erstellen.PointG;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

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
    public void playbackProgramm(ArrayList<PointG> points){
        for(int i=0;i<points.size();i++){
            //Delay (= Geschwindigkeit)
            write("d"+String.valueOf(points.get(i).getGeschwindigkeit()));
            //Punkte
            write("1"+String.valueOf(points.get(i).getAxisOne()));
            write("1"+String.valueOf(points.get(i).getAxisTwo()));
            write("1"+String.valueOf(points.get(i).getAxisThree()));
            write("1"+String.valueOf(points.get(i).getAxisFour()));
            write("1"+String.valueOf(points.get(i).getAxisFive()));
            write("1"+String.valueOf(points.get(i).getAxisSix()));
        }
    }
    public void test(int l){
        for(int i=0;i<l;i++){
            System.out.print("Nach 1 Sekunde ");
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    System.out.println("kommt der Rest des Textes");
                }
            }, 1000);
        }
    }

}
