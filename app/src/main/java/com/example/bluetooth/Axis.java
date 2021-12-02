package com.example.bluetooth;
public class Axis {

    public int degree;
    public int axisLength;
    public Axis prev;
    public Axis axisOne;

    public Axis(int degree, Axis prev, int axisLength, Axis axisOne){
        this.degree = degree;
        this.prev = prev;
        this.axisLength = axisLength;
        this.axisOne = axisOne;
    }

    public int getDegreeRelativ(){
        if(prev != null){
            int d = prev.getDegreeRelativ() - 90;
            return d + degree;
        }
        return degree;
    }

    public Vector2D getRelativVector(){
        int degreeRelativ = getDegreeRelativ();
        int x = (int)(Math.cos(Math.toRadians(degreeRelativ))*axisLength);
        int y = (int)(Math.sin(Math.toRadians(degreeRelativ))*axisLength);
        return new Vector2D(x,y);
    }

    public Vector2D getVectorWorld2D(){
        if(prev != null){
            return Vector2D.addVector2D(getRelativVector(),prev.getVectorWorld2D());
        }
        return Vector2D.addVector2D(getRelativVector(),new Vector2D(0,0));
    }

    public double getDrawFactor(){
        return Math.cos(Math.toRadians(degree));
    }

}
