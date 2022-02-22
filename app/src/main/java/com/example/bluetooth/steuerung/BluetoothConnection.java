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

public class BluetoothConnection {
    public OutputStream outputStream;
    public InputStream inputStream;
    public BluetoothAdapter bluetoothAdapter;
    public MainActivity mainActivity;
    public String deviceName;
    public BluetoothConnection(MainActivity mainActivity,String deviceName){
        this.mainActivity = mainActivity;
        this.deviceName = deviceName;
        connectBluetooth();
    }
    public void connectBluetooth() {
        try {
            mainActivity.writeConsole("Verbindung mit Bluetooth aufbauen");
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            for (BluetoothDevice device : bluetoothAdapter.getBondedDevices()) {
                if (device.getName().equals(deviceName)) {
                    ParcelUuid[] uuids = device.getUuids();
                    BluetoothSocket socket = device.createRfcommSocketToServiceRecord(uuids[0].getUuid());
                    socket.connect();
                    outputStream = socket.getOutputStream();
                    inputStream = socket.getInputStream();
                    mainActivity.writeConsole("Verbindung mit Bluetooth erfolgreich");
                }
            }
        }catch (SecurityException e){
            mainActivity.writeConsole("Verbindung mit Bluetooth fehlgeschlagen");
        }catch (Exception e){
            mainActivity.writeConsole("Verbindung mit Bluetooth fehlgeschlagen");
        }
    }
    public void sendMessage(String[] messages){
        try {
            for (String message : messages){
                message += ".";
                outputStream.write(message.getBytes());
            }
        } catch (Exception e) {
            mainActivity.writeConsole("Senden der Nachricht hat nicht geklappt");
        }
    }
    public void sendMessage(String message){
        try {
            message += ".";
            outputStream.write(message.getBytes());
        } catch (Exception e) {
            mainActivity.writeConsole("Senden der Nachricht hat nicht geklappt");
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String readMessage(){
        try{
            return new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)).readLine();
        }catch (Exception e){
            mainActivity.writeConsole("Empfangen der Nachricht hat nicht geklappt");
        }
        return null;
    }
}
