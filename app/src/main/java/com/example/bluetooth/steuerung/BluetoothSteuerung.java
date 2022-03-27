package com.example.bluetooth.steuerung;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Build;
import android.os.ParcelUuid;

import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class BluetoothSteuerung extends Bluetooth{
    public MainActivity mainActivity;
    public BluetoothSteuerung(MainActivity mainActivity, String deviceName){
        super(deviceName);
        this.mainActivity = mainActivity;
    }
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
    @Override
    public void sendMessage(String message){
        try {
            message += ".";
            outputStream.write(message.getBytes());
        } catch (Exception e) {
            mainActivity.fragmentKonsole.writeConsole("Senden der Anweisung hat nicht geklappt");
        }
    }
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
