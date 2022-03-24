package com.example.bluetooth.steuerung.simulation;
/**
 * @author Matthias Fichtinger
 * @version 11.03.2022
 * Wird für die Simulation des Roboters benötigt
 */
public class Vector2D {
    /**
     * Für den 2D Vektor gibt es die Eigenschaften x und y
     */
    public int x;
    public int y;
    /**
     * @param x
     * @param y
     * IM Konstruktor werden die zwei Eigenschaften initialisiert
     */
    public Vector2D(int x, int y){
        this.x = x;
        this.y = y;
    }

    /**
     * @param v1
     * @param v2
     * @return
     * Für das Addieren zweier Vektoren wurde die Methode addVector2D, die einen
     * neuen 2D Vector zurückgibt.
     */
    public static Vector2D addVector2D(Vector2D v1, Vector2D v2){
        return new Vector2D(v1.x + v2.x, v1.y + v2.y);
    }
}
