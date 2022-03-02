package com.example.bluetooth.programme.tcp;

import com.example.bluetooth.programme.erstellen.Point;

public class TCP {
    //alle Angaben in mm
    private RPoint base;
    private RPoint joint01;
    private RPoint joint02;
    private RPoint tcp;
    private double distanceBase_joint01;
    private double distanceJoint01_joint02;
    private double distanceJoint02_tcp;

    public TCP(){
        init();
    }

    private void init(){
        base=new RPoint(0,46.75,0);
        distanceBase_joint01=120;
        distanceJoint01_joint02=120;
        distanceJoint02_tcp=183.7;
    }
    private void calcJoint01(int axis1Degree,int axis2Degree){
        //Axis2Degree = derzeitge Grad der Achse 2
        //Achse 2 = Servomotoren an Base
        axis1Degree-=90;

        //Y, welches quasi die Höhe dieses Punktes darstellt, ist von der Stellung von Achse 1 unabhängig, da die Höhe immer gleich ist.
        double y=Math.sin(Math.toRadians(axis2Degree))*distanceBase_joint01;

        double tempHY=Math.cos(Math.toRadians(axis2Degree))*distanceBase_joint01;
        double z=Math.cos(Math.toRadians(axis1Degree))*tempHY;
        double x=Math.sin(Math.toRadians(axis1Degree))*tempHY;

        joint01=base.add(x,y,z);
    }
    private void calcJoint02(int axis1Degree, int axis3Degree){
        //Axis3Degree = derzeitge Grad der Achse 3
        //Achse 3 = Servomotoren an Joint01
        axis1Degree-=90;

        //Y, welches quasi die Höhe dieses Punktes darstellt, ist von der Stellung von Achse 1 unabhängig, da die Höhe an jeder Drehposition gleich ist.
        double y=Math.sin(Math.toRadians(axis3Degree))*distanceJoint01_joint02;

        double tempHY=Math.cos(Math.toRadians(axis3Degree))*distanceJoint01_joint02;
        double z=Math.cos(Math.toRadians(axis1Degree))*tempHY;
        double x=Math.sin(Math.toRadians(axis1Degree))*tempHY;

        joint02=joint01.add(x,y,z);
    }
    private void calcTCP(int axis1Degree, int axis4Degree){
        //Axis3Degree = derzeitge Grad der Achse 4
        //Achse 4 = Servomotoren an Joint02
        axis1Degree-=90;

        //Y, welches quasi die Höhe dieses Punktes darstellt, ist von der Stellung von Achse 1 unabhängig, da die Höhe an jeder Drehposition gleich ist.
        double y=Math.sin(Math.toRadians(axis4Degree))*distanceJoint02_tcp;

        double tempHY=Math.cos(Math.toRadians(axis4Degree))*distanceJoint02_tcp;
        double z=Math.cos(Math.toRadians(axis1Degree))*tempHY;
        double x=Math.sin(Math.toRadians(axis1Degree))*tempHY;

        tcp=joint02.add(x,y,z);
    }
    public RPoint getTCP(Point point){
        int axis1Degree=point.getAxisOne();
        int axis2Degree=point.getAxisTwo();
        int axis3Degree=point.getAxisThree();
        int axis4Degree=point.getAxisFour();

        //den wahren Winkel der achse zur Bodenplatte berechnen -> Zettel dienen zur genauen Angabe wie gerechnet wurde
        axis3Degree = calcRealAngle(axis2Degree, axis3Degree);
        axis4Degree = calcRealAngle(axis3Degree,axis4Degree);

        calcJoint01(axis1Degree,axis2Degree);
        calcJoint02(axis1Degree,axis3Degree);
        calcTCP(axis1Degree,axis4Degree);

        return tcp;
    }
    private int calcRealAngle(int firstAxisDegree,int secondAxisDegree){
        int x=90+secondAxisDegree;
        int y1=firstAxisDegree;
        int y=180;
        int y2=y-y1;
        int x2=y2;
        int x1=x-x2;
        return x1;
    }
}
