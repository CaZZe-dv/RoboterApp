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
/**
 * @author Matthias Fichtinger
 * @version 11.03.2022
 * Dokumentiert die Vorgänge in der App mit
 */
public class FragmentKonsole extends Fragment {
    /**
     * Referenz zu MainActivity
     */
    public MainActivity mainActivity;
    /**
     * Variable für View damit dieser nur inemal erstellt werden muss
     */
    public View view = null;
    /**
     * Boolean Variable damit gepesichert werden kann ob der View schon erstellt wurde
     */
    public boolean isCreated = false;
    /**
     * TExtView für das Aneziegen des Texts
     */
    public TextView textView;
    /**
     * ScrollView, damit man in der Konsole scrollen kann
     */
    public ScrollView scrollViewConsole;

    /**
     * @param mainActivity
     * Im Konstruktor wird nur die Refereanz zur MAinActivity übergeben
     */
    public FragmentKonsole(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }
    /**
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     * Die onCreateView Methode wird beim erstellen dieses Views aufgerufen, dabei setzten wir falls der
     * View noch null ist unser XML-File. Danach wird der View zurückgegeben.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view == null) {
            view = inflater.inflate(R.layout.fragment_konsole, container, false);
        }
        return view;
    }
    /**
     * @param view
     * @param savedInstanceState
     * Wenn der View erstellt worden ist, wird die onViewCreated Methode aufgerufen. Diese wird aber nur beim ersten
     * mal aufgerufen. In der Methode werden alle Eigenschfaten die deklariert worden sind initialisiert und Events wenn
     * nötig hinzugefügt.
     */
    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        if(isCreated){
            return;
        }
        isCreated = true;
        textView = getView().findViewById(R.id.consolenView);
        scrollViewConsole = getView().findViewById(R.id.scrollViewConsole);
    }
    /**
     * @param text
     * In der Methode writeConsole wird der Text, der auf die Konsole geschreiben werden soll übergeben.
     * Davor wird noch die Uhrzeit, an der dieser Eintrag erstellt worden ist festegelegt und mit der Nachricht
     * mit setText dme TextView hinzugefügt.
     */
    public void writeConsole(String text) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd/HH:mm:ss", Locale.getDefault());
        String uhrzeit = sdf.format(new Date());
        textView.setText(textView.getText() + "\r\n----------"+uhrzeit+"----------" + "\r\n" + text);
        scrollViewConsole.fullScroll(ScrollView.FOCUS_DOWN);
    }
}
