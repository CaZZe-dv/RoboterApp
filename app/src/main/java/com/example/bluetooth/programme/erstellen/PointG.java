package com.example.bluetooth.programme.erstellen;

public class PointG extends Point{
    private int geschwindigkeit;
    private int delay;
    public PointG(int axisOne, int axisTwo, int axisThree, int axisFour, int axisFive, int axisSix, int geschwindigkeit, int delay){
        super(axisOne,axisTwo,axisThree,axisFour,axisFive,axisSix);
        this.geschwindigkeit=geschwindigkeit;
        this.delay=delay;
    }
    public PointG(Point p, int geschwindigkeit, int delay){
        super(p);
        this.geschwindigkeit=geschwindigkeit;
        this.delay=delay;
    }

    public int getGeschwindigkeit() {
        return geschwindigkeit;
    }

    public void setGeschwindigkeit(int geschwindigkeit) {
        this.geschwindigkeit = geschwindigkeit;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }
}
