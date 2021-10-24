package com.example.bluetooth;

import android.app.AppComponentFactory;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

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
                toolTip.setText("Mit diesem Modus können die den Roboter steuern");
            }
        });

        programms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                programms.setChecked(true);
                toolTip.setText("Mit diesem Modus können sie Programme erstellen und abspielen");
            }
        });

        chooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton r = findViewById(radioGroup.getCheckedRadioButtonId());
                if(r.equals(controller)) {
                    switchToMainActivity();
                }
            }
        });
    }

    public void switchToMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}