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
    //
    public Axes(){
        this.axis1 = new Axis(90,null, 0,null);
        this.axis2 = new Axis(120,null,120,axis1);
        this.axis3 = new Axis(25,axis2,120,axis1);
        this.axis4 = new Axis(50,axis3,183,axis1);
        this.axis5 = new Axis(0,null,0,null);
        this.axis6 = new Axis(60,null,0,null);
    }
    //Wenn sic hdie Achsen ändern werden sie mit dieser Methode verändert und nach
    //der Änderung wird das DrawCanvas neu gezeichnet
    public void changePosition(int d1, int d2, int d3, int d4, int d5, int d6){
        if(d1 != -1)
            axis1.degree = d1;
        if(d2 != -1)
            axis2.degree = d2;
        if(d3 != -1)
            axis3.degree = d3;
        if(d4 != -1)
            axis4.degree = d4;
        if(d5 != -1)
            axis5.degree = d5;
        if(d6 != -1)
            axis6.degree = d6;
    }
}
