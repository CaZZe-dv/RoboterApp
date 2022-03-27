package com.example.bluetooth.programme.erstellen;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.example.bluetooth.R;
import com.example.bluetooth.programme.ProgrammActivity;

public class JoyStickClass {
    public static final int conNone = 0;
    public static final int conUp = 1;
    public static final int conUpRight = 2;
    public static final int conRight = 3;
    public static final int conDownRight = 4;
    public static final int conDown = 5;
    public static final int conDownLeft = 6;
    public static final int conLeft = 7;
    public static final int conUpLeft = 8;

    private int offset = 0;

    private Context context;
    private ViewGroup layout;
    private LayoutParams params;
    private int stickWidth;
    private int stickHeight;

    private int positionX = 0;
    private int positionY = 0;
    private int min_distance = 0;
    private float distance = 0;
    private float angle = 0;

    private DrawCanvas canvas;
    private Paint paint;
    private Bitmap stick;

    private boolean touch_state = false;

    public JoyStickClass (Context context, ViewGroup layout) {
        this.context = context;
        this.layout=layout;
        init();
    }
    private void init(){
        Drawable drawable = context.getResources().getDrawable(R.drawable.ic_joystick_controller);
        stick = drawableToBitmap(drawable);
        stickWidth = stick.getWidth();
        stickHeight = stick.getHeight();

        canvas = new DrawCanvas(context);
        paint = new Paint();
        params = layout.getLayoutParams();

        setStickSize(90, 90);
        setLayoutSize(300, 300);
        setLayoutAlpha(150);
        setStickAlpha(100);
        setOffset(50);
        setMinimumDistance(30);
    }

    public void drawStick(MotionEvent arg1) {
        positionX = (int) (arg1.getX() - (params.width / 2));
        positionY = (int) (arg1.getY() - (params.height / 2));
        distance = (float) Math.sqrt(Math.pow(positionX, 2) + Math.pow(positionY, 2));
        angle = (float) calcAngle(positionX, positionY);


        if(arg1.getAction() == MotionEvent.ACTION_DOWN) {
            if(distance <= (params.width / 2) - offset) {
                canvas.position(arg1.getX(), arg1.getY());
                draw();
                touch_state = true;
            }
        } else if(arg1.getAction() == MotionEvent.ACTION_MOVE && touch_state) {
            if(distance <= (params.width / 2) - offset) {
                canvas.position(arg1.getX(), arg1.getY());
                draw();
            } else if(distance > (params.width / 2) - offset){
                float x = (float) (Math.cos(Math.toRadians(calcAngle(positionX, positionY)))
                        * ((params.width / 2) - offset));
                float y = (float) (Math.sin(Math.toRadians(calcAngle(positionX, positionY)))
                        * ((params.height / 2) - offset));
                x += (params.width / 2);
                y += (params.height / 2);
                canvas.position(x, y);
                draw();
            } else {
                layout.removeView(canvas);
            }
        } else if(arg1.getAction() == MotionEvent.ACTION_UP) {
            layout.removeView(canvas);
            touch_state = false;
        }
    }

    public void setMinimumDistance(int minDistance) {
        min_distance = minDistance;
    }

    public int getDir() {
        if(distance > min_distance && touch_state) {
            if(angle >= 247.5 && angle < 292.5 ) {
                return conUp;
            } else if(angle >= 292.5 && angle < 337.5 ) {
                return conUpRight;
            } else if(angle >= 337.5 || angle < 22.5 ) {
                return conRight;
            } else if(angle >= 22.5 && angle < 67.5 ) {
                return conDownRight;
            } else if(angle >= 67.5 && angle < 112.5 ) {
                return conDown;
            } else if(angle >= 112.5 && angle < 157.5 ) {
                return conDownLeft;
            } else if(angle >= 157.5 && angle < 202.5 ) {
                return conLeft;
            } else if(angle >= 202.5 && angle < 247.5 ) {
                return conUpLeft;
            }
        } else if(distance <= min_distance && touch_state) {
            return conNone;
        }
        return 0;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setStickAlpha(int alpha) {
        paint.setAlpha(alpha);
    }


    public void setLayoutAlpha(int alpha) {
        layout.getBackground().setAlpha(alpha);
    }


    public void setStickSize(int width, int height) {
        stick = Bitmap.createScaledBitmap(stick, width, height, false);
        stickWidth = stick.getWidth();
        stickHeight = stick.getHeight();
    }


    public void setLayoutSize(int width, int height) {
        params.width = width;
        params.height = height;
    }

    private double calcAngle(float x, float y) {
        if(x >= 0 && y >= 0)
            return Math.toDegrees(Math.atan(y / x));
        else if(x < 0 && y >= 0)
            return Math.toDegrees(Math.atan(y / x)) + 180;
        else if(x < 0 && y < 0)
            return Math.toDegrees(Math.atan(y / x)) + 180;
        else if(x >= 0 && y < 0)
            return Math.toDegrees(Math.atan(y / x)) + 360;
        return 0;
    }

    private void draw() {
        try {
            layout.removeView(canvas);
        } catch (Exception e) { }
        layout.addView(canvas);
    }

    private class DrawCanvas extends View{
        float x, y;

        private DrawCanvas(Context context) {
            super(context);
        }

        public void onDraw(Canvas canvas) {
            canvas.drawBitmap(stick, x, y, paint);
        }

        private void position(float pos_x, float pos_y) {
            x = pos_x - (stickWidth / 2);
            y = pos_y - (stickHeight / 2);
        }
    }

    private Bitmap drawableToBitmap (Drawable drawable) {

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }
}