package com.example.bluetooth.programme.einstellungen;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Console {

    //Gesamter Text der Console
    private static String consoleText;

    public static void init(){
        consoleText="";
    }

    //Methode für spezielle Nachrichten
    public static void addLine(String line){
        consoleText+=getCurTime()+line+"\n";
    }
    //Bluetoothverbindung erfolgreich
    public static void addBluetoothSuccess(){
        consoleText+=getCurTime()+"Bluetooth-Connection erfolgreich"+"\n";
    }
    //Bluetoothverbindung nicht erfolgreich
    public static void addBluetoothNoSuccess(){
        consoleText+=getCurTime()+"Bluetooth-Connection nicht erfolgreich"+"\n";
    }
    //Achsenbewegung erfolgreich
    public static void axisMove(String achsenName,int wert){
        consoleText+=getCurTime()+achsenName+" auf "+wert+" gesetzt"+"\n";
    }
    //Achsenbewegung fehlgeschlagen
    public static void axisFail(String achsenName){
        consoleText+=getCurTime()+achsenName+" konnte nicht geändert werden"+"\n";
    }
    //Fährt in Home Position
    public static void homePosition(){
        consoleText+=getCurTime()+"Roboter fährt in Homeposition"+"\n";
    }
    //Fährt in Sleep Position
    public static void sleepPosition(){
        consoleText+=getCurTime()+"Roboter fährt in Sleepposition"+"\n";
    }
    //Startet Programm
    public static void startProgramm(String programmName){
        consoleText+=getCurTime()+"Programm "+programmName+" wird gestartet"+"\n";
    }
    //Stoppt Programm
    public static void stopProgramm(String programmName){
        consoleText+=getCurTime()+"Programm "+programmName+" erfolgreich beendet"+"\n";
    }
    //Gibt den Consolentext zurück
    public static String getConsoleText(){
        return consoleText;
    }
    //Gibt die derzeitige Zeit zurück, die am Anfang jeder Consolenausgabe steht
    private static String getCurTime(){
        Format f = new SimpleDateFormat("HH:mm:ss");
        return "["+f.format(new Date())+"] ";
    }
}
