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
    private double distanceBase_joint01;
    private double distanceJoint01_joint02;
    private double distanceJoint02_tcp;

    int reset;
    RPoint rPointGesucht;


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
        System.out.println("JOINT01 - X: "+joint01.getX()+", Y: "+joint01.getY()+", Z: "+joint01.getZ());
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
        System.out.println("JOINT02 - X: "+joint02.getX()+", Y: "+joint02.getY()+", Z: "+joint02.getZ());
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
        System.out.println("TCP - X: "+tcp.getX()+", Y: "+tcp.getY()+", Z: "+tcp.getZ());
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

        return test(axis1Degree,axis2DegreeTCP,axis3DegreeTCP,axis4DegreeTCP,rPointGesucht);


    }
    private Point test(int axis1Degree, int axis2Degree,int axis3Degree, int axis4Degree,RPoint rPoint){
        Point returnPoint=null;
        double x=rPoint.getX();
        double y=rPoint.getY();
        double z=rPoint.getZ();



        double xSoll=z/Math.sin(Math.toRadians(axis1Degree));
        double xIst=rPoint.getZ()/Math.sin(Math.toRadians(axis1Degree));
        double ySoll = y;
        double yIst = rPoint.getY();

        double yDiff=ySoll-yIst;//Um soviel höhe muss der TCP geändert werden
        double xDiff=xSoll-xIst;//Um soviel Abstand muss der TCP geändert werden
        double distanz=Math.sqrt(Math.abs(yDiff*yDiff)+Math.abs(xDiff*xDiff));//Distanz zum gewünschten TCP
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
        if(distanzNew<=5.0){
            System.out.println("PUNKT GEFUNDEN");
            returnPoint= new Point(axis1Degree,axis2Degree,axis3Degree,axis4Degree,0,0);
            System.out.println("Achse1: "+returnPoint.getAxisOne()+", Achse2: "+returnPoint.getAxisTwo()+", Achse3: "+returnPoint.getAxisThree()+", Achse4: "+returnPoint.getAxisFour());
        }else if(axis2DegreeTemp>180||axis2DegreeTemp<0||axis3DegreeTemp>180||axis3DegreeTemp<0||axis4DegreeTemp>180||axis4DegreeTemp<0) {
            System.out.println("RESET WEIL ZU GRO");
            reset = 0;
            test(axis1Degree, axis2DegreeTCP, axis3DegreeTCP, axis4DegreeTCP, rPointGesucht);
        }else{
            if(distanzNew<distanz){
                //Änderungen haben sich dem neuen Punkt angenähert
                test(axis1Degree,axis2DegreeTemp,axis3DegreeTemp,axis4DegreeTemp,tcpNew);
            }else{
                reset++;
                //Änderungen haben nichts gebracht
                //nun kann es sein, dass die änderungen nichts mehr bringen und eine komplett andere stellung benötigt wird.
                //wenn nach 5 achsenÄnderungen die distanz nicht verringert wird, dann wird resetet
                if(reset>=5){
                    System.out.println("RESET");
                    reset=0;
                    test(axis1Degree,axis2DegreeTCP,axis3DegreeTCP,axis4DegreeTCP,rPointGesucht);
                }else{
                    test(axis1Degree,axis2Degree,axis3Degree,axis4Degree,rPoint);
                }
            }
        }
        System.out.println("RETURN");
        return returnPoint;
    }




    /*
    public Point calcAxes(RPoint rPoint){
        //Schritt 1: axis1Degree ausrechnen, um dann den Rest als 2D sehen zu können#

        /*von oben betrachtet
         _________
        |         |  ^ z
        |    O    |  |
        |_________|  |______> x
        Vorderseite

        Höhe = y-Koordinate


        double x=rPoint.getX();
        double y=rPoint.getY();
        double z=rPoint.getZ();

        double axis1Degree=Math.toDegrees(Math.atan(z/x));

        double abstandSoll=z/Math.sin(Math.toRadians(axis1Degree));
        double abstandIst=tcp.getZ()/Math.sin(Math.toRadians(axis1DegreeTCP));
        double hoeheSoll = y;
        double hoeheIst = tcp.getY();

        double hoeheDiff=hoeheSoll-hoeheIst;//Um soviel höhe muss der TCP geändert werden
        double abstandDiff=abstandSoll-abstandIst;//Um soviel Abstand muss der TCP geändert werden
        //wenn Diff > 0 -> höhe bzw. abstand muss erhöht werden
        //wenn Diff < 0 -> höhe bzw. abstand müssen verkleinert werden

        achse="Achse2";
        check(hoeheDiff,abstandDiff);

        return null;
    }
    private void check(double hoeheDiff,double abstandDiff){
        RPoint tcpOld;
        double abdstandOld;
        double hoeheOld;
        RPoint tcpNew;
        double abdstandNew;
        double hoeheNew;
        //alte werte berechnen
        //Anpassen bei erneutem aufrufen sind die TCP werte nicht mehr korrekt
        tcpOld=getTCP(new Point(axis1DegreeTCP,axis1DegreeTCP,axis3DegreeTCP,axis4DegreeTCP,0,0));
        abdstandOld=tcpOld.getZ()/Math.sin(Math.toRadians(axis1DegreeTCP));
        hoeheOld = tcpOld.getY();
        //Axis2 Degree verringern
        axis2DegreeTemp=axis2DegreeTCP;
        axis2DegreeTemp--;
        tcpNew=getTCP(new Point(axis1DegreeTCP,axis2DegreeTemp,axis3DegreeTCP,axis4DegreeTCP,0,0));
        abdstandNew=tcpNew.getZ()/Math.sin(Math.toRadians(axis1DegreeTCP));
        hoeheNew = tcpNew.getY();
        if(hoeheNew>=hoeheOld){
            //wenn achse 2 verringert wird, wird höhe größer
            effectAxis2Verringern[0]="+";
        }else{
            effectAxis2Verringern[0]="-";
        }
        if(abdstandNew>=abdstandOld){
            effectAxis2Verringern[1]="+";
        }else{
            effectAxis2Verringern[1]="-";
        }

        //Axis2 Degree vergrößern
        axis2DegreeTemp=axis2DegreeTCP;
        axis2DegreeTemp++;
        tcpNew=getTCP(new Point(axis1DegreeTCP,axis2DegreeTemp,axis3DegreeTCP,axis4DegreeTCP,0,0));
        abdstandNew=tcpNew.getZ()/Math.sin(Math.toRadians(axis1DegreeTCP));
        hoeheNew = tcpNew.getY();
        if(hoeheNew>=hoeheOld){
            //wenn achse 2 verringert wird, wird höhe größer
            effectAxis2Vergroessern[0]="+";
        }else{
            effectAxis2Vergroessern[0]="-";
        }
        if(abdstandNew>=abdstandOld){
            effectAxis2Vergroessern[1]="+";
        }else{
            effectAxis2Vergroessern[1]="-";
        }

        //Axis3 Degree verringern
        axis3DegreeTemp=axis3DegreeTCP;
        axis3DegreeTemp--;
        tcpNew=getTCP(new Point(axis1DegreeTCP,axis2DegreeTCP,axis3DegreeTemp,axis4DegreeTCP,0,0));
        abdstandNew=tcpNew.getZ()/Math.sin(Math.toRadians(axis1DegreeTCP));
        hoeheNew = tcpNew.getY();
        if(hoeheNew>=hoeheOld){
            //wenn achse 3 verringert wird, wird höhe größer
            effectAxis3Verringern[0]="+";
        }else{
            effectAxis3Verringern[0]="-";
        }
        if(abdstandNew>=abdstandOld){
            effectAxis3Verringern[1]="+";
        }else{
            effectAxis3Verringern[1]="-";
        }

        //Axis3 Degree vergrößern
        axis3DegreeTemp=axis3DegreeTCP;
        axis3DegreeTemp++;
        tcpNew=getTCP(new Point(axis1DegreeTCP,axis2DegreeTCP,axis3DegreeTemp,axis4DegreeTCP,0,0));
        abdstandNew=tcpNew.getZ()/Math.sin(Math.toRadians(axis1DegreeTCP));
        hoeheNew = tcpNew.getY();
        if(hoeheNew>=hoeheOld){
            //wenn achse 2 verringert wird, wird höhe größer
            effectAxis3Vergroessern[0]="+";
        }else{
            effectAxis3Vergroessern[0]="-";
        }
        if(abdstandNew>=abdstandOld){
            effectAxis3Vergroessern[1]="+";
        }else{
            effectAxis3Vergroessern[1]="-";
        }

        //Axis4 Degree verringern
        axis4DegreeTemp=axis4DegreeTCP;
        axis4DegreeTemp--;
        tcpNew=getTCP(new Point(axis1DegreeTCP,axis2DegreeTCP,axis3DegreeTCP,axis4DegreeTemp,0,0));
        abdstandNew=tcpNew.getZ()/Math.sin(Math.toRadians(axis1DegreeTCP));
        hoeheNew = tcpNew.getY();
        if(hoeheNew>=hoeheOld){
            //wenn achse 2 verringert wird, wird höhe größer
            effectAxis4Verringern[0]="+";
        }else{
            effectAxis4Verringern[0]="-";
        }
        if(abdstandNew>=abdstandOld){
            effectAxis4Verringern[1]="+";
        }else{
            effectAxis4Verringern[1]="-";
        }

        //Axis4 Degree vergrößern
        axis4DegreeTemp=axis4DegreeTCP;
        axis4DegreeTemp++;
        tcpNew=getTCP(new Point(axis1DegreeTCP,axis2DegreeTCP,axis3DegreeTCP,axis4DegreeTemp,0,0));
        abdstandNew=tcpNew.getZ()/Math.sin(Math.toRadians(axis1DegreeTCP));
        hoeheNew = tcpNew.getY();
        if(hoeheNew>=hoeheOld){
            //wenn achse 2 verringert wird, wird höhe größer
            effectAxis4Vergroessern[0]="+";
        }else{
            effectAxis4Vergroessern[0]="-";
        }
        if(abdstandNew>=abdstandOld){
            effectAxis4Vergroessern[1]="+";
        }else{
            effectAxis4Vergroessern[1]="-";
        }
        //Nun ist klar, wie sich die Änderungen der jeweiligen Achsen auf den Abstand und die Höhe auswirken
        //Immer erst achse2 bewegen, dann achse 3 und dann achse 4. wenn dann der TCP immer noch nicht genau ist das ganze nochmal wiederholen

        if((hoeheDiff+abstandDiff)<=1.0){
            return;
        }else if(hoeheDiff>=0&&abstandDiff>=0){
            hoehePlusAbstandPlus(hoeheDiff,abstandDiff);
        }else if(hoeheDiff>=0&&abstandDiff<0){
            hoehePlusAbstandMinus(hoeheDiff,abstandDiff);
        }else if(hoeheDiff<0&&abstandDiff>=0){
            hoeheMinusAbstandPlus(hoeheDiff,abstandDiff);
        }else if(hoeheDiff<0&&abstandDiff<0){
            hoeheMinusAbstandMinus(hoeheDiff,abstandDiff);
        }
    }
    private void hoehePlusAbstandPlus(double hoeheDiff,double abstandDiff){
        switch (achse){
            case "Achse2":
                //Hoehe erhöhen
                if(effectAxis2Verringern[0].equals("+")){

                }else if(effectAxis2Vergroessern[0].equals("+")){

                }

                //Abstand erhöhen

                achse="Achse3";
                break;
            case "Achse3":

                achse="Achse4";
                break;
            case "Achse4":

                achse="Achse2";
                break;
        }

    }
    private void hoehePlusAbstandMinus(double hoeheDiff,double abstandDiff){

    }
    private void hoeheMinusAbstandPlus(double hoeheDiff,double abstandDiff){

    }
    private void hoeheMinusAbstandMinus(double hoeheDiff,double abstandDiff){

    }
    */


    private int calcRealAngle(int firstAxisDegree,int secondAxisDegree){
        int x=90+secondAxisDegree;

        int y1=firstAxisDegree;
        int y=180;
        int y2=y-y1;
        int x2=y2;
        int x1=x-x2;
        System.out.println("Real Angle: "+x1);
        return x1;
    }
}
