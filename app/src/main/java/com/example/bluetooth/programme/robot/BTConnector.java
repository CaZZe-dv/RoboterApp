package com.example.bluetooth.programme.robot;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.ParcelUuid;

import com.example.bluetooth.programme.erstellen.Point;
import com.example.bluetooth.programme.erstellen.PointG;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class BTConnector {
    //Methoden für die Verbindung mit dem Bluetooth Modul
    private BluetoothAdapter bt;
    private OutputStream outputStream;
    private InputStream inputStream;
    private Point curPosition;
    private int defaultSpeed = 1;//TODO: richtigen defaultSpeed setzen
    public BTConnector(){
        //connectBluetooth();
        curPosition=new Point();
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
        //TODO: curPosition updaten
        try{
            s += ".";
            outputStream.write(s.getBytes());
        }catch (Exception e){
            //writeConsole("Senden der Anweisung hat nicht geklappt");
        }

    }
    public void playbackProgramm(ArrayList<PointG> points){
        playbackProgramm(points,points.size()-1,0);
    }
    private void playbackProgramm(final ArrayList<PointG> points, int size, int i){
        final CustomHandler handler = new CustomHandler(size,i,points);
        handler.postDelayed(new Runnable() {
            public void run() {
                int i= handler.getI();
                int size=handler.getSize();
                ArrayList<PointG> points=handler.getArrayList();
                PointG point=points.get(i);

                /*
                //Delay (= Geschwindigkeit)
                write("d"+String.valueOf(points.get(i).getGeschwindigkeit()));
                //Punkte
                write("1"+String.valueOf(points.get(i).getAxisOne()));
                write("1"+String.valueOf(points.get(i).getAxisTwo()));
                write("1"+String.valueOf(points.get(i).getAxisThree()));
                write("1"+String.valueOf(points.get(i).getAxisFour()));
                write("1"+String.valueOf(points.get(i).getAxisFive()));
                write("1"+String.valueOf(points.get(i).getAxisSix()));
                 */
                System.out.println("i = "+i+"/"+size+", Delay: "+point.getDelay());

                if(i<size) {
                    i++;
                    playbackProgramm(points,size,i);
                }
            }
        }, points.get(i).getDelay());
    }
    public void homePosition(){
        write("190");
        write("2120");
        write("325");
        write("450");
        write("50");
        write("660");
        curPosition.setAxisOne(90);
        curPosition.setAxisTwo(120);
        curPosition.setAxisThree(25);
        curPosition.setAxisFour(50);
        curPosition.setAxisFive(0);
        curPosition.setAxisSix(60);
    }
    public void goTo(Point p){
        write("d"+String.valueOf(defaultSpeed));

        write("1"+p.getAxisOne());
        write("1"+p.getAxisTwo());
        write("1"+p.getAxisThree());
        write("1"+p.getAxisFour());
        write("1"+p.getAxisFive());
        write("1"+p.getAxisSix());
    }
    public void setCurPosition(Point p){
        this.curPosition=p;
    }
    public Point getCurPosition(){
        return curPosition;
    }

}
