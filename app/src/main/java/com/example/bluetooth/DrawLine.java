package com.example.bluetooth;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import com.google.android.material.transition.MaterialSharedAxis;

class DrawLine extends View {
    public Paint paint = new Paint();
    public Axes axes;
    public DrawLine(Context context) {
        super(context);
        paint.setColor(Color.BLACK);
        axes = new Axes(this);
    }
    @Override
    public void onDraw(Canvas canvas) {
        paint.setStrokeWidth(4);
        paint.setColor(Color.WHITE);
        paint.setTextSize(50);
        //Achsen Grade und Positionen in der Welt anzeigen lassen
        canvas.drawText("|Achse 1: "+axes.axis1.degree+"|Achse 2: "+axes.axis2.degree+"|Achse 3: "
                +axes.axis3.degree+"|",0,50,paint);
        canvas.drawText("|Achse 4: "+axes.axis4.degree+"|Achse 5: "+axes.axis5.degree
                +"|Achse 6: "+axes.axis6.degree+"|",0,100,paint);
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
        Vector3D vectorWorld = axes.getVectorWorld3D();
        canvas.drawText("(x: "+vectorWorld.x+" y: "+vectorWorld.y+" z: "+vectorWorld.z+")",(int)(axis4.x*axes.axis1.getDrawFactor()),-axis4.y,paint);
    }

}