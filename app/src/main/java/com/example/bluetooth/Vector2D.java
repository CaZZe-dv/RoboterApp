package com.example.bluetooth;

public class Vector2D {
    public int x;
    public int y;
    public Vector2D(int x, int y){
        this.x = x;
        this.y = y;
    }
    public static Vector2D addVector2D(Vector2D v1, Vector2D v2){
        return new Vector2D(v1.x + v2.x, v1.y + v2.y);
    }
}
