package com.example.bluetooth.steuerung.simulation;
/**
 * @author Matthias Fichtinger
 * @version 11.03.2022
 * Wird für die Simulation benötigt
 */
public class Axis {
    /**
     * Winkel am dem sich die Achse gerade befindet
     */
    public int degree;
    /**
     * Länge dieser Achse
     */
    public int axisLength;
    /**
     * Vorherige Achse relativ zur dieser Achse
     */
    public Axis prev;
    /**
     * Referenz zur ersten Achse
     */
    public Axis axisOne;

    /**
     * @param degree
     * @param prev
     * @param axisLength
     * @param axisOne
     * Im Konstruktor werden die Eigenschaften, die deklariert wurden initialisiert
     */
    public Axis(int degree, Axis prev, int axisLength, Axis axisOne){
        this.degree = degree;
        this.prev = prev;
        this.axisLength = axisLength;
        this.axisOne = axisOne;
    }
    /**
     * @return
     * Die Methode getDegreeRelativ gibt die relativen Grade der Achse wie diese im
     * Raum steht zurück.
     */
    public int getDegreeRelativ(){
        if(prev != null){
            int d = prev.getDegreeRelativ() - 90;
            return d + degree;
        }
        return degree;
    }

    /**
     * @return
     * Die Methode getRelativeVector gibt bezogen auf die relativen Grade den 2D Vektor,
     * wie diese Achse im Raum steht zurück.
     */
    public Vector2D getRelativVector(){
        int degreeRelativ = getDegreeRelativ();
        int x = (int)(Math.cos(Math.toRadians(degreeRelativ))*axisLength);
        int y = (int)(Math.sin(Math.toRadians(degreeRelativ))*axisLength);
        return new Vector2D(x,y);
    }

    /**
     * @return
     * Die Methode getVectorWorld2D gibt bezogen auf die angepsrohene Achse, den Welt 2D Vektor
     * zurück.
     */
    public Vector2D getVectorWorld2D(){
        if(prev != null){
            return Vector2D.addVector2D(getRelativVector(),prev.getVectorWorld2D());
        }
        return getRelativVector();
    }

    /**
     * @return
     * Die Methode getVectorWorld3D berechnet sich aus dem 2D Vektor den 3D Vektor, dazu wird die
     * erste Achse verwendet um die neunen Koordinaten zu bestimmen.
     */
    public Vector3D getVectorWorld3D(){
        int degree = axisOne.degree;
        Vector2D vectorWorld2D = getVectorWorld2D();
        int x = (int)(Math.cos(Math.toRadians(degree))*vectorWorld2D.x);
        int z = (int)(Math.sin(Math.toRadians(degree))*vectorWorld2D.x);
        int y = vectorWorld2D.y;
        return new Vector3D(x,y,z);
    }

    /**
     * @return
     * Damit die Achse 1 in der Simulation simuliert werden kann benötigt man den Faktor,
     * der beim Zeichnen benötigt wird.
     */
    public double getDrawFactor(){
        return Math.cos(Math.toRadians(degree));
    }
}
