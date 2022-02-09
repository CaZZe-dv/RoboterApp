package com.example.bluetooth.programme.erstellen;

public class PointG extends Point{
    int geschwindigkeit;
    public PointG(int axisOne, int axisTwo, int axisThree, int axisFour, int axisFive, int axisSix, int geschwindigkeit){
        super(axisOne,axisTwo,axisThree,axisFour,axisFive,axisSix);
        this.geschwindigkeit=geschwindigkeit;
    }
    public PointG(Point p, int geschwindigkeit){
        super(p);
        this.geschwindigkeit=geschwindigkeit;
    }

    public int getGeschwindigkeit() {
        return geschwindigkeit;
    }

    public void setGeschwindigkeit(int geschwindigkeit) {
        this.geschwindigkeit = geschwindigkeit;
    }
}
