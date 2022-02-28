package com.example.bluetooth.programme.robot;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.ParcelUuid;

import com.example.bluetooth.programme.einstellungen.Console;
import com.example.bluetooth.programme.erstellen.Point;
import com.example.bluetooth.programme.erstellen.PointG;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.Inet4Address;
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
                    Console.addBluetoothSuccess();
                }
            }
        }catch (Exception e){
            Console.addBluetoothNoSuccess();
        }
    }
    public static void sendMessage(String axis,String message){
        //curPosition updaten
        String axisConsole="";
        int wertOld=0;
        int wertNew=Integer.valueOf(message);
        switch (axis){
            case "1":
                wertOld=curPosition.getAxisOne();
                curPosition.setAxisOne(wertNew);
                axisConsole="Achse 1";
                break;
            case "2":
                wertOld=curPosition.getAxisTwo();
                curPosition.setAxisTwo(wertNew);
                axisConsole="Achse 2";
                break;
            case "3":
                wertOld=curPosition.getAxisThree();
                curPosition.setAxisThree(wertNew);
                axisConsole="Achse 3";
                break;
            case "4":
                wertOld=curPosition.getAxisFour();
                curPosition.setAxisFour(wertNew);
                axisConsole="Achse 4";
                break;
            case "5":
                wertOld=curPosition.getAxisFive();
                curPosition.setAxisFive(wertNew);
                axisConsole="Achse 5";
                break;
            case "6":
                wertOld=curPosition.getAxisSix();
                curPosition.setAxisSix(wertNew);
                axisConsole="Achse 6";
                break;
            case "d":
                wertOld=curSpeed;
                curSpeed=wertNew;
                axisConsole="Geschwindigkeit";
                break;
        }
        if(wertOld!=wertNew){
            message=axis+message+".";
            sendMessage(message,axisConsole,wertNew);
        }
    }
    private static void sendMessage(String message,String axisConsole,int wert){
        try{
            outputStream.write(message.getBytes());
            Console.axisMove(axisConsole,wert);
        }catch (Exception e){
            Console.axisFail(axisConsole);
        }
    }
    public static void playbackProgramm(ArrayList<PointG> points,String programmName){
        Console.startProgramm(programmName);
        calculateDelay(points);
        PointG point=points.get(0);
        Point p=new Point(point.getAxisOne(),point.getAxisTwo(),point.getAxisThree(),point.getAxisFour(),point.getAxisFive(),point.getAxisSix());
        goTo(p,point.getGeschwindigkeit());

        if(points.size()>1){
            playbackProgramm(points,points.size()-1,1,programmName);
        }else{
            Console.stopProgramm(programmName);
        }
    }
    private static void playbackProgramm(final ArrayList<PointG> points, int size, int i,String programmName){
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
                    playbackProgramm(points,size,i,programmName);
                }else{
                    Console.stopProgramm(programmName);
                }
            }
        }, points.get(i-1).getDelay());
    }
    private static void calculateDelay(ArrayList<PointG> points){
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
            for(int i=1;i<points.size();i++){

                axisOld=points.get(i-1).getAxis();
                axisNew=points.get(i).getAxis();
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
    }
    public static void homePosition(){
        Console.homePosition();
        sendMessage("1","90");
        sendMessage("2","120");
        sendMessage("3","25");
        sendMessage("4","50");
        sendMessage("5","0");
        sendMessage("6","60");
        sendMessage("d","20");
    }
    public static void sleepPosition(){
        Console.sleepPosition();
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