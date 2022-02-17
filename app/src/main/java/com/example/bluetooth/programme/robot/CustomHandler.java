package com.example.bluetooth.programme.robot;

import android.os.Handler;

import com.example.bluetooth.programme.erstellen.PointG;

import java.util.ArrayList;

public class CustomHandler extends Handler {
    private int size;
    private int i;
    private ArrayList<PointG> arrayList;
    public CustomHandler(int size, int i, ArrayList<PointG> arrayList){
        super();
        this.size=size;
        this.i=i;
        this.arrayList=arrayList;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public ArrayList<PointG> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<PointG> arrayList) {
        this.arrayList = arrayList;
    }
}
