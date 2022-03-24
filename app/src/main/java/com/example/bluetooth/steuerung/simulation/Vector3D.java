package com.example.bluetooth.steuerung.simulation;
/**
 * @author Matthias Fichtinger
 * @version 11.03.2022
 * Wird für die Simulation des Roboters benötigt
 */
public class Vector3D extends Vector2D{
    /**
     * Für den 3D Vektor wurde eine dritte Eigeschaft
     */
    public int z;

    /**
     * @param x
     * @param y
     * @param z
     * Im Konstruktor werden alle Eigenschaften intitialiesiert
     */
    public Vector3D(int x, int y, int z){
        super(x,y);
        this.z = z;
    }

    /**
     * @param v1
     * @param v2
     * @return
     * Für das Addieren wurde die Methode addVector3d geschreiben. Als Rückgabe
     * erhält man einen neuen Vector3D
     */
    public static Vector3D addVector3D(Vector3D v1, Vector3D v2){
        return new Vector3D(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
    }
}
