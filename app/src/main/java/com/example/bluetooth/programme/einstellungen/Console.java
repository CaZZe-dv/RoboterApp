package com.example.bluetooth.programme.einstellungen;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Console {
    private static String consoleText;
    public static void init(){
        consoleText="";
    }

    public static void addLine(String line){
        consoleText+=getCurTime()+line+"\n";
    }
    public static void addBluetoothSuccess(){
        consoleText+=getCurTime()+"Bluetooth-Connection erfolgreich"+"\n";
    }
    public static void addBluetoothNoSuccess(){
        consoleText+=getCurTime()+"Bluetooth-Connection nicht erfolgreich"+"\n";
    }
    public static void axisMove(String achsenName,int wert){
        consoleText+=getCurTime()+achsenName+" auf "+wert+" gesetzt"+"\n";
    }
    public static void axisFail(String achsenName){
        consoleText+=getCurTime()+achsenName+" konnte nicht geändert werden"+"\n";
    }
    public static void homePosition(){
        consoleText+=getCurTime()+"Roboter fährt in Homeposition"+"\n";
    }
    public static void sleepPosition(){
        consoleText+=getCurTime()+"Roboter fährt in Sleepposition"+"\n";
    }
    public static void startProgramm(String programmName){
        consoleText+=getCurTime()+"Programm "+programmName+" wird gestartet"+"\n";
    }
    public static void stopProgramm(String programmName){
        consoleText+=getCurTime()+"Programm "+programmName+" erfolgreich beendet"+"\n";
    }
    public static String getConsoleText(){
        return consoleText;
    }
    private static String getCurTime(){
        Format f = new SimpleDateFormat("HH.mm.ss");
        return "["+f.format(new Date())+"] ";
    }
}
