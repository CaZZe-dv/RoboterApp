package com.example.bluetooth;
public class Vector3D {
    public int x;
    public int y;
    public int z;
    public Vector3D(int x, int y, int z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static Vector3D addVector3D(Vector3D v1, Vector3D v2){
        return new Vector3D(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
    }
}
