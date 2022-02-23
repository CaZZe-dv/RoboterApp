package com.example.bluetooth.programme.erstellen;

public class Point {
    //Achsenstellungen fÃ¼r einen exakten Punkt
    //Progress der Achsen ist ein IntegerWert (z.B.: bei AxisSix von 0 bis 70)
    private int axisSix;
    private int axisFive;
    private int axisFour;
    private int axisThree;
    private int axisTwo;
    private int axisOne;
    public Point(){
    }
    public Point(int axisOne, int axisTwo, int axisThree, int axisFour, int axisFive, int axisSix){
        this.axisOne=axisOne;
        this.axisTwo=axisTwo;
        this.axisThree=axisThree;
        this.axisFour=axisFour;
        this.axisFive=axisFive;
        this.axisSix=axisSix;
    }
    public Point(Point p){
        this.axisOne=p.getAxisOne();
        this.axisTwo=p.getAxisTwo();
        this.axisThree=p.getAxisThree();
        this.axisFour=p.getAxisFour();
        this.axisFive=p.getAxisFive();
        this.axisSix=p.getAxisSix();
    }
    public void setAxis(int axisOne, int axisTwo, int axisThree, int axisFour, int axisFive, int axisSix) {
        this.axisOne=axisOne;
        this.axisTwo=axisTwo;
        this.axisThree=axisThree;
        this.axisFour=axisFour;
        this.axisFive=axisFive;
        this.axisSix=axisSix;
    }
    public void setHomePosition(){
        this.axisOne=90;
        this.axisTwo=120;
        this.axisThree=25;
        this.axisFour=50;
        this.axisFive=0;
        this.axisSix=60;
    }

    public int getAxisSix() {
        return axisSix;
    }

    public void setAxisSix(int axisSix) {
        this.axisSix = axisSix;
    }

    public int getAxisFive() {
        return axisFive;
    }

    public void setAxisFive(int axisFive) {
        this.axisFive = axisFive;
    }

    public int getAxisFour() {
        return axisFour;
    }

    public void setAxisFour(int axisFour) {
        this.axisFour = axisFour;
    }

    public int getAxisThree() {
        return axisThree;
    }

    public void setAxisThree(int axisThree) {
        this.axisThree = axisThree;
    }

    public int getAxisTwo() {
        return axisTwo;
    }

    public void setAxisTwo(int axisTwo) {
        this.axisTwo = axisTwo;
    }

    public int getAxisOne() {
        return axisOne;
    }

    public void setAxisOne(int axisOne) {
        this.axisOne = axisOne;
    }
}
