package com.example.bluetooth.programme.tcp;

public class RPoint {
    private double x;
    private double y;
    private double z;
    public RPoint(){

    }
    public RPoint(double x,double y, double z){
        this.x=x;
        this.y=y;
        this.z=z;
    }
    public RPoint add(double x, double y, double z){
        double xNew=this.x+x;
        double yNew=this.y+y;
        double zNew=this.z+z;
        return new RPoint(xNew,yNew,zNew);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }
}
