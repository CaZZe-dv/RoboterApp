package com.example.bluetooth.steuerung.simulation;
//Wird für die Simulation des Roboers in der App benötigt
public class Axes {
    //Für jede Achse des Roboters gibt es eine Eigenschaft
    public Axis axis1;
    public Axis axis2;
    public Axis axis3;
    public Axis axis4;
    public Axis axis5;
    public Axis axis6;
    //Es wird eine Referenz auf das DrawCanvas benötigt
    public DrawCanvas drawCanvas;
    //Im Konstruktor wird das DrawCanvas übergeben und
    //die Achsen auf ihre Länge eingestellt
    public Axes(DrawCanvas drawCanvas){
        this.axis1 = new Axis(0,null, 0,null);
        this.axis2 = new Axis(0,null,120,axis1);
        this.axis3 = new Axis(0,axis2,120,axis1);
        this.axis4 = new Axis(0,axis3,190,axis1);
        this.axis5 = new Axis(0,null,0,null);
        this.axis6 = new Axis(0,null,0,null);
        this.drawCanvas = drawCanvas;
    }
    //Wenn sic hdie Achsen ändern werden sie mit dieser Methode verändert und nach
    //der Änderung wird das DrawCanvas neu gezeichnet
    public void changePosition(int d1, int d2, int d3, int d4, int d5, int d6){
        axis1.degree = d1;
        axis2.degree = d2;
        axis3.degree = d3;
        axis4.degree = d4;
        axis5.degree = d5;
        axis6.degree = d6;
        drawCanvas.invalidate();
    }
    //Gibt den Weltpunkt des Woboters zurück
    public Vector3D getVectorWorld3D(){
        int degree = axis1.degree;
        Vector2D vectorWorld2D = axis4.getVectorWorld2D();
        int x = (int)(Math.cos(Math.toRadians(degree))*vectorWorld2D.x);
        int z = (int)(Math.sin(Math.toRadians(degree))*vectorWorld2D.x);
        int y = vectorWorld2D.y;

        return new Vector3D(x,y,z);
    }
}
