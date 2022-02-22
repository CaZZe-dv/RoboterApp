package com.example.bluetooth.steuerung.simulation;

//Wird für Simulation benötigt
public class Vector3D extends Vector2D{
    //Erbt von Vector2D und bekommt zusätzliche Eigenschaft z für die 3 Dimension
    public int z;
    //Konstruktor zum Einlesen der Werte
    public Vector3D(int x, int y, int z){
        super(x,y);
        this.z = z;
    }
    //Zusätzliche MEthode zum addieren von zwei 3-Dimensionalen Vektoren
    public static Vector3D addVector3D(Vector3D v1, Vector3D v2){
        return new Vector3D(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
    }
}
