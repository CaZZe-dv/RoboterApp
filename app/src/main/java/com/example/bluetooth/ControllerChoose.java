package com.example.bluetooth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bluetooth.programme.ProgrammActivity;

public class ControllerChoose extends AppCompatActivity {
    public Button chooseButton;
    public RadioButton programms;
    public RadioButton controller;
    public TextView toolTip;
    public RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose);

        chooseButton = findViewById(R.id.ChooseButton);
        programms = findViewById(R.id.Programms);
        controller = findViewById(R.id.Controller);
        toolTip = findViewById(R.id.Tooltip);
        radioGroup = findViewById(R.id.radioGroup);

        controller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.setChecked(true);
                toolTip.setText("Mit diesem Modus können Sie den Roboter steuern");
            }
        });

        programms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                programms.setChecked(true);
                toolTip.setText("Mit diesem Modus können Sie Programme erstellen und abspielen");
            }
        });

        chooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton r = findViewById(radioGroup.getCheckedRadioButtonId());
                if(r.equals(controller)) {//Controller Modus
                    switchToMainActivity();
                }else if(r.equals(programms)){//Programm Modus
                    switchToProgrammActivity();
                }
            }
        });
    }

    public void switchToMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void switchToProgrammActivity(){
        Intent intent = new Intent(this, ProgrammActivity.class);
        startActivity(intent);
    }
}