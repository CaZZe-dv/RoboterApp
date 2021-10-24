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
        canvas.translate(getWidth()/2,getHeight()-100);
        paint.setStrokeWidth(4);
        paint.setColor(Color.BLACK);

        Vector3D v2 = axes.axis2.getRelativVector();

        canvas.drawLine(0,0,(int)(v2.x*axes.axis1.getDrawFactor()),-v2.y,paint);

        Vector3D v3 = Vector3D.addVector3D(v2,axes.axis3.getRelativVector());

        canvas.drawLine((int)(v2.x*axes.axis1.getDrawFactor()),-v2.y,(int)(v3.x*axes.axis1.getDrawFactor()),-v3.y,paint);

        Vector3D v4 = Vector3D.addVector3D(v3,axes.axis4.getRelativVector());

        canvas.drawLine((int)(v3.x*axes.axis1.getDrawFactor()),-v3.y ,(int)(v4.x*axes.axis1.getDrawFactor()),-v4.y,paint);

        paint.setColor(Color.RED);

        canvas.drawLine(0,0,(int)(v4.x*axes.axis1.getDrawFactor()),-v4.y,paint);

        int x = (int)(Math.cos(Math.toRadians(axes.axis1.degree))*v4.x);
        int z = (int)(Math.sin(Math.toRadians(axes.axis1.degree))*v4.x);

        Vector3D vector3DWorld = new Vector3D(x,v4.y,z);

        canvas.drawText("(x: "+vector3DWorld.x+", y: "+vector3DWorld.y+", z: "+vector3DWorld.z+")",(int)(v4.x*axes.axis1.getDrawFactor()),-v4.y,paint);
    }

}