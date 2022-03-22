package com.example.bluetooth.programme.tcp;

import com.example.bluetooth.programme.erstellen.Point;

import java.util.Random;

public class TCP {
    //alle Angaben in mm
    private RPoint base;
    private RPoint joint01;
    private RPoint joint02;
    private RPoint tcp;
    private int axis1DegreeTCP;//axis1Degree des TCPs
    private int axis2DegreeTCP;//axis2Degree des TCPs
    private int axis3DegreeTCP;//axis3Degree des TCPs
    private int axis4DegreeTCP;//axis4Degree des TCPs
    private int axis2DegreeOriginal;
    private int axis3DegreeOriginal;
    private int axis4DegreeOriginal;
    private double distanceBase_joint01;
    private double distanceJoint01_joint02;
    private double distanceJoint02_tcp;

    int reset;
    private RPoint rPointGesucht;
    private Point returnPoint;


    private String achse;//welche achse grade dran ist

    public TCP(){
        init();
    }

    private void init(){
        base=new RPoint(0,46.75,0);
        distanceBase_joint01=120;
        distanceJoint01_joint02=120;
        distanceJoint02_tcp=183.7;

        reset=0;
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
        //System.out.println("JOINT01 - X: "+joint01.getX()+", Y: "+joint01.getY()+", Z: "+joint01.getZ());
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
        //System.out.println("JOINT02 - X: "+joint02.getX()+", Y: "+joint02.getY()+", Z: "+joint02.getZ());
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
        //System.out.println("TCP - X: "+tcp.getX()+", Y: "+tcp.getY()+", Z: "+tcp.getZ());
    }
    public RPoint getTCP(Point point){
        int axis1Degree=point.getAxisOne();
        int axis2Degree=point.getAxisTwo();
        int axis3Degree=point.getAxisThree();
        int axis4Degree=point.getAxisFour();

        //den wahren Winkel der achse zur Bodenplatte berechnen -> Zettel dienen zur genauen Angabe wie gerechnet wurde
        axis3Degree = calcRealAngle(axis2Degree, axis3Degree);
        axis4Degree = calcRealAngle(axis3Degree,axis4Degree);

        axis1DegreeTCP=axis1Degree;
        axis2DegreeTCP=axis2Degree;
        axis3DegreeTCP=axis3Degree;
        axis4DegreeTCP=axis4Degree;

        calcJoint01(axis1Degree,axis2Degree);
        calcJoint02(axis1Degree,axis3Degree);
        calcTCP(axis1Degree,axis4Degree);

        return tcp;
    }

    public Point calcAxes(RPoint rPoint){
        //Gibt einen Point mit den Achsengraden zurück, an denen der Roboter am rPoint steht
        double x=rPoint.getX();
        double y=rPoint.getY();
        double z=rPoint.getZ();

        int axis1Degree= (int) Math.toDegrees(Math.atan(z/x));
        rPointGesucht=rPoint;

        axis2DegreeOriginal=axis2DegreeTCP;
        axis3DegreeOriginal=axis3DegreeTCP;
        axis4DegreeOriginal=axis4DegreeTCP;

        test(axis1Degree,axis2DegreeOriginal,axis3DegreeOriginal,axis4DegreeOriginal,getTCP(new Point(axis1DegreeTCP,axis2DegreeTCP,axis3DegreeTCP,axis4DegreeTCP,0,0)));



        return returnPoint;


    }
    private void test(int axis1Degree, int axis2Degree,int axis3Degree, int axis4Degree,RPoint curRPoint){
        double xSoll=rPointGesucht.getZ()/Math.sin(Math.toRadians(axis1Degree));
        double ySoll = rPointGesucht.getY();


        double xIst = curRPoint.getZ()/Math.sin(Math.toRadians(axis1Degree));
        double yIst = curRPoint.getY();
        System.out.println("xIst: "+xIst+", yIst: "+yIst);

        double yDiff=ySoll-yIst;//Um soviel höhe muss der TCP geändert werden
        double xDiff=xSoll-xIst;//Um soviel Abstand muss der TCP geändert werden
        double distanz=Math.sqrt(Math.abs(yDiff*yDiff)+Math.abs(xDiff*xDiff));//Distanz zum derzeitigen TCP

        //anhand dieser distanz wird nun im trial and error verfahren durchprobiert, welche achstenstellungen die distanz verringern
        Random rnd=new Random();
        boolean[] bool=new boolean[]{rnd.nextBoolean(),rnd.nextBoolean(),rnd.nextBoolean()};
        int axis2DegreeTemp=axis2Degree;
        int axis3DegreeTemp=axis3Degree;
        int axis4DegreeTemp=axis4Degree;
        if(bool[0]){
            axis2DegreeTemp++;
        }else{
            axis2DegreeTemp--;
        }
        if(bool[1]){
            axis3DegreeTemp++;
        }else{
            axis3DegreeTemp--;
        }
        if(bool[2]){
            axis4DegreeTemp++;
        }else{
            axis4DegreeTemp--;
        }
        RPoint tcpNew=getTCP(new Point(axis1Degree,axis2DegreeTemp,axis3DegreeTemp,axis4DegreeTemp,0,0));

        xIst=tcpNew.getZ()/Math.sin(Math.toRadians(axis1Degree));
        yIst = tcpNew.getY();

        yDiff=ySoll-yIst;//Um soviel höhe muss der TCP geändert werden
        xDiff=xSoll-xIst;//Um soviel Abstand muss der TCP geändert werden
        double distanzNew=Math.sqrt(Math.abs(yDiff*yDiff)+Math.abs(xDiff*xDiff));//Distanz zum gewünschten TCP
        if(distanzNew<=1.0){
            System.out.println("PUNKT GEFUNDEN");
            returnPoint= new Point(axis1Degree,axis2DegreeTemp,axis3DegreeTemp,axis4DegreeTemp,0,0);
            System.out.println("Achse1: "+returnPoint.getAxisOne()+", Achse2: "+returnPoint.getAxisTwo()+", Achse3: "+returnPoint.getAxisThree()+", Achse4: "+returnPoint.getAxisFour());
        }else if(axis2DegreeTemp>180||axis2DegreeTemp<0||axis3DegreeTemp>180||axis3DegreeTemp<0||axis4DegreeTemp>180||axis4DegreeTemp<0) {
            reset = 0;
            System.out.println("AUFRUF0");
            curRPoint=getTCP(new Point(axis1Degree,axis2DegreeOriginal,axis3DegreeOriginal,axis4DegreeOriginal,0,0));
            test(axis1Degree,axis2DegreeOriginal,axis3DegreeOriginal,axis4DegreeOriginal,curRPoint);
        }else{
            System.out.println("distanzNew: "+distanzNew+", distanz: "+distanz);
            if(distanzNew<distanz){
                //Änderungen haben sich dem neuen Punkt angenähert
                reset=0;
                System.out.println("IST ANGENÄHERT: "+distanzNew);
                System.out.println("AUFRUF1");
                test(axis1Degree,axis2DegreeTemp,axis3DegreeTemp,axis4DegreeTemp,tcpNew);
            }else{
                reset++;
                System.out.println("NICHT ANGENÄHERT: "+distanz+", reset wert ist auf "+reset);
                //Änderungen haben nichts gebracht
                //nun kann es sein, dass die änderungen nichts mehr bringen und eine komplett andere stellung benötigt wird.
                //wenn nach 5 achsenÄnderungen die distanz nicht verringert wird, dann wird resetet
                if(reset>=20){
                    System.out.println("RESET");
                    reset=0;
                    curRPoint=getTCP(new Point(axis1Degree,axis2DegreeOriginal,axis3DegreeOriginal,axis4DegreeOriginal,0,0));
                    System.out.println("AUFRUF2");
                    test(axis1Degree,axis2DegreeOriginal,axis3DegreeOriginal,axis4DegreeOriginal,curRPoint);
                }else{
                    System.out.println("AUFRUF3");
                    test(axis1Degree,axis2Degree,axis3Degree,axis4Degree,curRPoint);
                }
            }
        }
        System.out.println("RETURN");
    }


    private int calcRealAngle(int firstAxisDegree,int secondAxisDegree){
        int x=90+secondAxisDegree;

        int y1=firstAxisDegree;
        int y=180;
        int y2=y-y1;
        int x2=y2;
        int x1=x-x2;
        //System.out.println("Real Angle: "+x1);
        return x1;
    }
}
