package com.example.bluetooth.steuerung;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bluetooth.R;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FragmentKonsole extends Fragment {
    //
    public MainActivity mainActivity;
    public View view = null;
    public boolean isCreated = false;
    //
    public TextView textView;
    public ScrollView scrollViewConsole;
    //
    public FragmentKonsole(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view == null) {
            view = inflater.inflate(R.layout.fragment_konsole, container, false);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        if(isCreated){
            return;
        }
        isCreated = true;
        textView = getView().findViewById(R.id.consolenView);
        scrollViewConsole = getView().findViewById(R.id.scrollViewConsole);
    }

    public void writeConsole(String text) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd/HH:mm:ss", Locale.getDefault());
        String uhrzeit = sdf.format(new Date());
        textView.setText(textView.getText() + "\r\n----------"+uhrzeit+"----------" + "\r\n" + text);
        scrollViewConsole.fullScroll(ScrollView.FOCUS_DOWN);
    }
}
