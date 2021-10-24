package com.example.bluetooth;
public class Axes {
    public Axis axis1;
    public Axis axis2;
    public Axis axis3;
    public Axis axis4;
    public DrawLine drawLine;
    public Axes(DrawLine drawLine){

        this.axis1 = new Axis(0,null, 0);
        this.axis2 = new Axis(45,null,150);
        this.axis3 = new Axis(45,axis2,150);
        this.axis4 = new Axis(45,axis3,150);
        this.drawLine = drawLine;
    }

    public void changePosition(int d1, int d2, int d3, int d4){
        axis1.degree = d1;
        axis2.degree = d2;
        axis3.degree = d3;
        axis4.degree = d4;

        drawLine.invalidate();
    }
}
