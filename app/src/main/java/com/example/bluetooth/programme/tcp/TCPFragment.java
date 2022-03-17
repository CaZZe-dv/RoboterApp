package com.example.bluetooth.programme.tcp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.bluetooth.R;
import com.example.bluetooth.programme.erstellen.Point;
import com.example.bluetooth.programme.robot.BTConnector;

public class TCPFragment extends Fragment implements View.OnClickListener {
    View view;

    TextView textViewCurTCP;

    //X
    TextView textViewXProgress;
    ImageButton btnXLower;
    ImageButton btnXHigher;
    //Y
    TextView textViewYProgress;
    ImageButton btnYLower;
    ImageButton btnYHigher;
    //Z
    TextView textViewZProgress;
    ImageButton btnZLower;
    ImageButton btnZHigher;

    RPoint curTCP;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_prog_tcp, container, false);
        init();
        return view;
    }
    private void init(){
        BTConnector.homePosition();
        TCP tcp=new TCP();
        curTCP=tcp.getTCP(BTConnector.getCurPosition());

        textViewCurTCP=view.findViewById(R.id.tempTextViewCurTCP);
        updateTCPTextView();

        textViewXProgress=view.findViewById(R.id.tempTextViewXProgress);
        textViewYProgress=view.findViewById(R.id.tempTextViewYProgress);
        textViewZProgress=view.findViewById(R.id.tempTextViewZProgress);

        btnXLower=view.findViewById(R.id.tempBtnXLower);
        btnXHigher=view.findViewById(R.id.tempBtnXHigher);
        btnYLower=view.findViewById(R.id.tempBtnYLower);
        btnYHigher=view.findViewById(R.id.tempBtnYHigher);
        btnZLower=view.findViewById(R.id.tempBtnZLower);
        btnZHigher=view.findViewById(R.id.tempBtnZHigher);

        btnXLower.setOnClickListener(this);
        btnXHigher.setOnClickListener(this);
        btnYLower.setOnClickListener(this);
        btnYHigher.setOnClickListener(this);
        btnZLower.setOnClickListener(this);
        btnZHigher.setOnClickListener(this);

        BTConnector.homePosition();
        RPoint rPoint= tcp.getTCP(BTConnector.getCurPosition());
        //System.out.println("HomePos X:"+rPoint.getX()+", Y:"+rPoint.getY()+", Z:"+rPoint.getZ());
        Point point=tcp.calcAxes(new RPoint(0,310,170));
        System.out.println("Achse1: "+point.getAxisOne()+", Achse2: "+point.getAxisTwo()+", Achse3: "+point.getAxisThree()+", Achse4: "+point.getAxisFour());
        RPoint rPointNew=tcp.getTCP(point);
        System.out.println("Neue Position X:"+rPointNew.getX()+", Y:"+rPointNew.getY()+", Z:"+rPointNew.getZ());


    }


    @Override
    public void onClick(View v) {
        //immer in 5 mm schritten verringern
        if(v.equals(btnXLower)){
            curTCP.setX(curTCP.getX()-5);
        }else if(v.equals(btnXHigher)){
            curTCP.setX(curTCP.getX()+5);
        }else if(v.equals(btnYLower)){
            curTCP.setY(curTCP.getY()-5);
        }else if(v.equals(btnYHigher)){
            curTCP.setY(curTCP.getY()-5);
        }else if(v.equals(btnZLower)){
            curTCP.setZ(curTCP.getZ()-5);
        }else if(v.equals(btnZHigher)){
            curTCP.setZ(curTCP.getZ()+5);
        }
        updateTCPTextView();
    }
    private void updateTCPTextView(){
        textViewCurTCP.setText("Derzeitiger TCP: X: "+(int)(curTCP.getX())+", Y: "+(int)(curTCP.getY())+", Z: "+(int)(curTCP.getZ()));
    }
}