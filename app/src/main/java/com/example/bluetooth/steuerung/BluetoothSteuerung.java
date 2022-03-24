package com.example.bluetooth.steuerung;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Build;
import android.os.ParcelUuid;
import androidx.annotation.RequiresApi;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
/**
 * @author Matthias Fichtinger
 * @version 11.03.2022
 * Wird benötigt für den Aufbau der Bluetooth-Verbindung und für das
 * Senden der Nachrichten.
 */
public class BluetoothSteuerung extends Bluetooth{
    /**
     * Referenz zur MainActivity
     */
    public MainActivity mainActivity;
    /**
     * @param mainActivity
     * @param deviceName
     * Im Konstruktor wird die MainActivity und der Name des Bluetooth-Moduls
     * übergeben
     */
    public BluetoothSteuerung(MainActivity mainActivity, String deviceName){
        super(deviceName);
        this.mainActivity = mainActivity;
    }
    /**
     *
     */
    @Override
    public void connectBluetooth() {
        try {
            mainActivity.fragmentKonsole.writeConsole("Verbindung mit Bluetooth aufbauen");
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            for (BluetoothDevice device : bluetoothAdapter.getBondedDevices()) {
                if (device.getName().equals(deviceName)) {
                    ParcelUuid[] uuids = device.getUuids();
                    BluetoothSocket socket = device.createRfcommSocketToServiceRecord(uuids[0].getUuid());
                    socket.connect();
                    outputStream = socket.getOutputStream();
                    inputStream = socket.getInputStream();
                    mainActivity.fragmentKonsole.writeConsole("Verbindung mit Bluetooth erfolgreich");
                }
            }
        }catch (SecurityException e){
            mainActivity.fragmentKonsole.writeConsole("Verbindung mit Bluetooth fehlgeschlagen");
        }catch (Exception e){
            mainActivity.fragmentKonsole.writeConsole("Verbindung mit Bluetooth fehlgeschlagen");
        }
    }

    /**
     * @param messages
     *
     */
    @Override
    public void sendMessage(String[] messages){
        try {
            for (String message : messages){
                message += ".";
                outputStream.write(message.getBytes());
            }
        } catch (Exception e) {
            mainActivity.fragmentKonsole.writeConsole("Senden der Anweisung hat nicht geklappt");
        }
    }
    /**
     * @param message
     *
     */
    @Override
    public void sendMessage(String message){
        try {
            message += ".";
            outputStream.write(message.getBytes());
        } catch (Exception e) {
            mainActivity.fragmentKonsole.writeConsole("Senden der Anweisung hat nicht geklappt");
        }
    }

    /**
     * @return
     *
     */
    @Override
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String readMessage(){
        try{
            return new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)).readLine();
        }catch (Exception e){
            mainActivity.fragmentKonsole.writeConsole("Empfangen der Anweisung hat nicht geklappt");
        }
        return null;
    }
}
