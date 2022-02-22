package com.example.bluetooth.steuerung.simulation;
//Wird für Simulation benötigt
public class Vector2D {
    //Es gibt zwei Eigenschaften x und y die einen 2-Dimensionalen Vektor darstellen
    public int x;
    public int y;
    //Konstruktor zum zuweisen der Werte für x und y
    public Vector2D(int x, int y){
        this.x = x;
        this.y = y;
    }
    //ZUsätzliche Methode um zwei 2-Dimensionale Vektoren zu addieren
    public static Vector2D addVector2D(Vector2D v1, Vector2D v2){
        return new Vector2D(v1.x + v2.x, v1.y + v2.y);
    }
}
