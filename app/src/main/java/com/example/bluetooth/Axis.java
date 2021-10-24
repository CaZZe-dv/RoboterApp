package com.example.bluetooth;
public class Axis {
    public int degree;
    public int axisLength;
    public Axis prev;

    public Axis(int degree, Axis prev, int axisLength){
        this.degree = degree;
        this.prev = prev;
        this.axisLength = axisLength;
    }

    public int getDegreeRelativ(){
        if(prev != null){
            int d = prev.getDegreeRelativ() - 90;
            return d + degree;
        }
        return degree;
    }

    public Vector3D getRelativVector(){
        int degreeRelativ = getDegreeRelativ();
        int x = (int)(Math.cos(Math.toRadians(degreeRelativ))*axisLength);
        int y = (int)(Math.sin(Math.toRadians(degreeRelativ))*axisLength);
        return new Vector3D(x,y,0);
    }

    public double getDrawFactor(){
        return Math.cos(Math.toRadians(degree));
    }
}
