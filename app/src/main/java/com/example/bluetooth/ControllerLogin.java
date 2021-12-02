package com.example.bluetooth;

import android.app.AppComponentFactory;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class ControllerLogin extends AppCompatActivity {
    public Button loginButton;
    public TextView username;
    public TextView password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);

        username.setText("admin");
        password.setText("admin");

        try {
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (username.getText().toString().equals("admin") && password.getText().toString().equals("admin")) {
                        switchToChoose();
                    }
                }
            });
        }catch (Exception e){
            System.out.println(e);
        }
    }

    public void switchToChoose(){
        Intent intent = new Intent(this, ControllerChoose.class);
        startActivity(intent);
    }
}
