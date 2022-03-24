package com.example.bluetooth.steuerung.simulation;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
/**
 * @author Matthias Fichtinger
 * @version 11.03.2022
 * Wird für das Zeichnen der Simulation benötigt und erbt deswegen von View
 */
public class DrawCanvas extends View {
    /**
     * Für das Zeichnen auf dem View benötigt man ein Paint Objekt
     */
    public Paint paint = new Paint();
    /**
     * Eine Referenz zu Axes
     */
    public Axes axes;
    /**
     * @param context
     * @param axes
     * Im Konstruktor werden der Context und die Referenz zu Axes übergeben.
     */
    public DrawCanvas(Context context,Axes axes) {
        super(context);
        this.axes = axes;
    }
    /**
     * @param canvas
     * Die onDraw Methode muss überschreiben werden. In dieser kann man dann mit dem Paint Objekt
     * die nötigen Parameter setzten und mit canvas können diese dann gezeichnet werden.
     */
    @Override
    public void onDraw(android.graphics.Canvas canvas) {
        paint.setStrokeWidth(4);
        paint.setColor(Color.WHITE);
        paint.setTextSize(50);
        //Achsengrade und Positionen in der Welt anzeigen lassen
        canvas.drawText("|Achse 1: "+axes.axis1.degree+"|Achse 2: "+axes.axis2.degree+"|Achse 3: "
                +axes.axis3.degree+"|",0,50,paint);
        canvas.drawText("|Achse 4: "+axes.axis4.degree+"|Achse 5: "+axes.axis5.degree
                +"|Achse 6: "+axes.axis6.degree+"|",0,100,paint);
        paint.setColor(Color.RED);
        Vector3D vectorWorld = axes.axis4.getVectorWorld3D();
        canvas.drawText("(x: "+vectorWorld.x+" y: "+vectorWorld.y+" z: "+vectorWorld.z+")",0,150,paint);
        paint.setColor(Color.WHITE);
        canvas.translate(getWidth()/2,getHeight()-100);
        //Von Achse 2 bis Achse 3
        Vector2D axis2 = axes.axis2.getVectorWorld2D();
        canvas.drawLine(0,0,(int)(axis2.x*axes.axis1.getDrawFactor()),-axis2.y,paint);
        //Von Achse 3 bis Achse 4
        Vector2D axis3 = axes.axis3.getVectorWorld2D();
        canvas.drawLine((int)(axis2.x*axes.axis1.getDrawFactor()),-axis2.y,(int)(axis3.x*axes.axis1.getDrawFactor()),-axis3.y,paint);
        //Von Achse 4 bis Ende
        Vector2D axis4 = axes.axis4.getVectorWorld2D();
        canvas.drawLine((int)(axis3.x*axes.axis1.getDrawFactor()),-axis3.y,(int)(axis4.x*axes.axis1.getDrawFactor()),-axis4.y,paint);
        //Weltpunkt einzeichen
        paint.setColor(Color.RED);
        canvas.drawLine(0,0,(int)(axis4.x*axes.axis1.getDrawFactor()),-axis4.y,paint);
        //Weltpunkt Text einfügen
        canvas.drawText("World",(int)(axis4.x*axes.axis1.getDrawFactor()),-axis4.y,paint);
    }
}

