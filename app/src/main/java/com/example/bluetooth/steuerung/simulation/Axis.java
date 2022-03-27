package com.example.bluetooth.steuerung.simulation;
//Ein Teil der Simulation des Roboters
public class Axis {
    //Jede Achse verfügt über einen Winkel in dem er sich gearde befindet
    //einer Länge der vorherigen Achse und der Achse eins
    public int degree;
    public int axisLength;
    public Axis prev;
    public Axis axisOne;
    //IM Konstruktor werden dann zu den EIgenschaften die Werte zugewiesen
    public Axis(int degree, Axis prev, int axisLength, Axis axisOne){
        this.degree = degree;
        this.prev = prev;
        this.axisLength = axisLength;
        this.axisOne = axisOne;
    }
    //MIt dieser Methode bekommt man die relativen Grade an der sich der Roboter befindet
    //Wird für die Simulation benötigt
    public int getDegreeRelativ(){
        if(prev != null){
            int d = prev.getDegreeRelativ() - 90;
            return d + degree;
        }
        return degree;
    }
    //Mit dieser MEthode erhält man den relative Vector im Raum
    public Vector2D getRelativVector(){
        int degreeRelativ = getDegreeRelativ();
        int x = (int)(Math.cos(Math.toRadians(degreeRelativ))*axisLength);
        int y = (int)(Math.sin(Math.toRadians(degreeRelativ))*axisLength);
        return new Vector2D(x,y);
    }
    //Mit dieser Methode erhält man den Weltpunkt im Raum für diese Achse als 2-Dimensionaler Vector
    public Vector2D getVectorWorld2D(){
        if(prev != null){
            return Vector2D.addVector2D(getRelativVector(),prev.getVectorWorld2D());
        }
        return getRelativVector();
    }
    //
    public Vector3D getVectorWorld3D(){
        int degree = axisOne.degree;
        Vector2D vectorWorld2D = getVectorWorld2D();
        int x = (int)(Math.cos(Math.toRadians(degree))*vectorWorld2D.x);
        int z = (int)(Math.sin(Math.toRadians(degree))*vectorWorld2D.x);
        int y = vectorWorld2D.y;
        return new Vector3D(x,y,z);
    }
    //Wird ebenfalls für die Simulation benötigt
    public double getDrawFactor(){
        return Math.cos(Math.toRadians(degree));
    }
}
