package com.example.bluetooth.programme.robot;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.ParcelUuid;

import com.example.bluetooth.programme.erstellen.Point;
import com.example.bluetooth.programme.erstellen.PointG;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class BTConnector {
    //Methoden fÃ¼r die Verbindung mit dem Bluetooth Modul
    private static OutputStream outputStream;
    private static InputStream inputStream;
    private static BluetoothAdapter bluetoothAdapter;
    final private static String deviceName = "HC-05";

    private static Point curPosition;
    private static int curSpeed;
    private static int defaultSpeed = 20;

    public static void init(){
        connectBluetooth();
        curPosition=new Point();
        homePosition();
    }

    private static void connectBluetooth() {
        try {
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            for (BluetoothDevice device : bluetoothAdapter.getBondedDevices()) {
                if (device.getName().equals(deviceName)) {
                    ParcelUuid[] uuids = device.getUuids();
                    BluetoothSocket socket = device.createRfcommSocketToServiceRecord(uuids[0].getUuid());
                    socket.connect();
                    outputStream = socket.getOutputStream();
                    inputStream = socket.getInputStream();
                }
            }
        }catch (Exception e){
            //Bluetooth Verbindung konnte nicht hergestellt werden

        }
    }
    public static void sendMessage(String axis,String message){
        //curPosition updaten
        switch (axis){
            case "1":
                curPosition.setAxisOne(Integer.valueOf(message));
                break;
            case "2":
                curPosition.setAxisTwo(Integer.valueOf(message));
                break;
            case "3":
                curPosition.setAxisThree(Integer.valueOf(message));
                break;
            case "4":
                curPosition.setAxisFour(Integer.valueOf(message));
                break;
            case "5":
                curPosition.setAxisFive(Integer.valueOf(message));
                break;
            case "6":
                curPosition.setAxisSix(Integer.valueOf(message));
                break;
            case "d":
                curSpeed=Integer.valueOf(message);
                break;
        }

        message=axis+message;
        try{
            message += ".";
            outputStream.write(message.getBytes());
        }catch (Exception e){
        }

    }
    public static void playbackProgramm(ArrayList<PointG> pointsG){
        ArrayList<PointG> points=calculateDelay(pointsG);
        PointG point=points.get(0);
        Point p=new Point(point.getAxisOne(),point.getAxisTwo(),point.getAxisThree(),point.getAxisFour(),point.getAxisFive(),point.getAxisSix());
        goTo(p,point.getGeschwindigkeit());

        if(points.size()>1){
            playbackProgramm(points,points.size()-1,1);
        }
    }
    private static void playbackProgramm(final ArrayList<PointG> points, int size, int i){

        final CustomHandler handler = new CustomHandler(size,i,points);
        handler.postDelayed(new Runnable() {
            public void run() {
                int i= handler.getI();
                int size=handler.getSize();
                ArrayList<PointG> points=handler.getArrayList();
                PointG point=points.get(i);

                Point p=new Point(point.getAxisOne(),point.getAxisTwo(),point.getAxisThree(),point.getAxisFour(),point.getAxisFive(),point.getAxisSix());
                goTo(p,point.getGeschwindigkeit());


                if(i<size) {
                    i++;
                    playbackProgramm(points,size,i);
                }
            }
        }, points.get(i-1).getDelay());
    }
    private static ArrayList<PointG>  calculateDelay(ArrayList<PointG> points){
        //Größten Abstand finden
        //von derzeitigem Stand zu 1. Punkt
        int[] axisOld=curPosition.getAxis();
        int[] axisNew=points.get(0).getAxis();
        int diff;
        int highest=0;
        for(int i=0;i<axisNew.length;i++){
            diff=Math.abs(axisOld[i]-axisNew[i]);
            if(diff>highest){
                highest=diff;
            }
        }
        int speed=points.get(0).getGeschwindigkeit();
        int additionalDelay=speed*highest;
        int delayNew=points.get(0).getDelay()+additionalDelay;
        points.get(0).setDelay(delayNew);
        if(points.size()>2){//größer 2, da ja der letzte punkt vom delay her egal ist
            for(int i=0;i<points.size()-1;i++){

                axisOld=points.get(i).getAxis();
                axisNew=points.get(i+1).getAxis();
                highest=0;
                for(int j=0;j<axisNew.length;j++){
                    diff=Math.abs(axisOld[j]-axisNew[j]);
                    if(diff>highest){
                        highest=diff;
                    }
                }
                speed=points.get(i).getGeschwindigkeit();
                additionalDelay=speed*highest;
                delayNew=points.get(i).getDelay()+additionalDelay;
                points.get(i).setDelay(delayNew);
            }
        }
        return points;
    }
    public static void homePosition(){
        sendMessage("1","90");
        sendMessage("2","120");
        sendMessage("3","25");
        sendMessage("4","50");
        sendMessage("5","0");
        sendMessage("6","60");
        sendMessage("d","20");
    }
    public static void sleepPosition(){
        sendMessage("1","90");
        sendMessage("2","60");
        sendMessage("3","33");
        sendMessage("4","25");
        sendMessage("5","0");
        sendMessage("6","60");
        sendMessage("d","20");
    }
    public static void goTo(Point p,int geschw){
        sendMessage("d", String.valueOf(geschw));

        sendMessage("1",String.valueOf(p.getAxisOne()));
        sendMessage("2",String.valueOf(p.getAxisTwo()));
        sendMessage("3",String.valueOf(p.getAxisThree()));
        sendMessage("4",String.valueOf(p.getAxisFour()));
        sendMessage("5",String.valueOf(p.getAxisFive()));
        sendMessage("6",String.valueOf(p.getAxisSix()));
    }
    public static void setCurPosition(Point p){
        curPosition=p;
    }
    public static Point getCurPosition(){
        return curPosition;
    }

}