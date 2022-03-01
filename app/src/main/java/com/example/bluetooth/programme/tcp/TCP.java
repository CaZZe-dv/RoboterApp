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
        System.out.println("Joint 01- X: "+joint01.getX()+", Y: "+joint01.getY()+", Z: "+joint01.getZ());
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
        System.out.println("Joint 02- X: "+joint02.getX()+", Y: "+joint02.getY()+", Z: "+joint02.getZ());
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
        System.out.println("Joint 02 Differences: X: "+x+", Y: "+y+", Z: "+z);
        System.out.println("TCP- X: "+tcp.getX()+", Y: "+tcp.getY()+", Z: "+tcp.getZ());
    }
    public RPoint getTCP(Point point){
        int axis1Degree=point.getAxisOne();
        int axis2Degree=point.getAxisTwo();
        int axis3Degree=point.getAxisThree()+90;
        int a=180-axis3Degree;
        int x=axis2Degree-a;
        axis3Degree=x;

        int axis4Degree=point.getAxisFour()+90;
        a=180-axis4Degree;
        x=axis3Degree-a;
        axis4Degree=x;
        System.out.println("axis1Degree: "+axis1Degree);
        System.out.println("axis2Degree: "+axis2Degree);
        System.out.println("axis3Degree: "+axis3Degree);
        System.out.println("axis4Degree: "+axis4Degree);
        calcJoint01(axis1Degree,axis2Degree);
        calcJoint02(axis1Degree,axis3Degree);
        calcTCP(axis1Degree,axis4Degree);

        return tcp;
    }
}
