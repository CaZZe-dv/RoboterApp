package com.example.bluetooth.programme.erstellen;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.bluetooth.R;
import com.example.bluetooth.programme.database.Connector;
import com.example.bluetooth.programme.robot.BTConnector;
import com.example.bluetooth.steuerung.ControllerChoose;

import java.util.ArrayList;

public class ErstellenFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener{

    View view;

    BearbeitenFragment fragmentBearbeiten;

    Connector connector;

    AlertDialog dialog;
    AlertDialog.Builder dialogBuilder;

    ImageButton btnBack;
    Button btnAddProgramm;
    int btnAddProgrammState;
    int editProgramm;

    EditText textViewProgrammName;
    EditText textViewBeschreibung;

    ListView listView;
    ArrayList<SpannableString> arrayList;
    ArrayList<Integer> idList;
    ArrayAdapter<SpannableString> arrayAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_erstellen, container, false);
        init();
        return view;
    }
    private void init(){
        connector=new Connector(view.getContext());
        fragmentBearbeiten=new BearbeitenFragment();

        //Buttons
        btnAddProgramm = view.findViewById(R.id.btnAddProgramm);
        btnAddProgrammState=1;
        btnBack=view.findViewById(R.id.btnErstellenBack);

        btnBack.setOnClickListener(this);
        btnAddProgramm.setOnClickListener(this);
        //TextView
        textViewProgrammName = view.findViewById(R.id.textViewProgrammname);
        textViewBeschreibung = view.findViewById(R.id.textViewProgrammBeschreibung);
        textViewProgrammName.setText("");
        textViewBeschreibung.setText("");

        //Liste
        listView=(ListView)view.findViewById(R.id.listViewProgramme);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);

        arrayList=new ArrayList<SpannableString>();
        idList=new ArrayList<Integer>();
        arrayAdapter=new ArrayAdapter(view.getContext(),android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(arrayAdapter);
        updateList();
    }

    //Listen
    private void updateList(){
        ArrayList<String> nameList=connector.getProgrammListe();
        ArrayList<String> beschreibungList=connector.getBeschreibungListe();
        arrayList=new ArrayList<SpannableString>();
        for(int i=0;i<nameList.size();i++){
            String name=nameList.get(i);
            String beschreibung=beschreibungList.get(i);
            SpannableString sString = new SpannableString(name+"\n"+beschreibung);
            sString.setSpan(new RelativeSizeSpan(1f), 0, name.length(), 0);
            sString.setSpan(new RelativeSizeSpan(0.7f), name.length(), name.length()+beschreibung.length()+1, 0);

            arrayList.add(sString);
        }
        idList = connector.getIDListe();

        arrayAdapter=new ArrayAdapter(view.getContext(),android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(arrayAdapter);
    }

    //Button Listeners
    @Override
    public void onClick(View view) {
        if(view.equals(btnAddProgramm)){
            if(btnAddProgrammState==1){
                String name;
                String beschreibung;
                if((name=textViewProgrammName.getText().toString()).isEmpty()){
                    dialogError("Bitte Namen angeben");
                }else if((beschreibung=textViewBeschreibung.getText().toString()).isEmpty()){
                    dialogError("Bitte Beschreibung angeben");
                }else{
                    connector.newProgramm(name,beschreibung);
                    idList=connector.getIDListe();
                    int index = idList.size()-1;
                    int id=idList.get(index);
                    BTConnector.homePosition();
                    switchFrag(id);
                }
            }else if(btnAddProgrammState==2){
                String name=textViewProgrammName.getText().toString();
                String beschreibung=textViewBeschreibung.getText().toString();
                int id=idList.get(editProgramm);
                connector.alterProgrammName(id,name);
                connector.alterProgrammBeschreibung(id,beschreibung);

                textViewProgrammName.setText("");
                textViewBeschreibung.setText("");
                updateList();

                btnAddProgrammState=1;
                btnAddProgramm.setText("Hinzufügen");
            }

        }else if(view.equals(btnBack)){
            Intent intent = new Intent(getActivity(), ControllerChoose.class);
            startActivity(intent);
        }
    }
    //Liste Listeners
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //Bei einfachem Klick wird das ausgewählte Programm bearbeitet

        int id=idList.get(i);
        switchFrag(id);
    }
    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        //Programm umbenennen
        dialogChangeName();
        editProgramm=i;
        return true;
        //Bei halten kann das Item gelöscht werden
    }
    private void switchFrag(int id){
        getFragmentManager().beginTransaction().replace(R.id.fragmentLayout_programm,fragmentBearbeiten).commit();
        fragmentBearbeiten.setId(id);
        fragmentBearbeiten.setProgrammName(connector.getProgrammName(id));
    }
    private void changeName(){
        int id=idList.get(editProgramm);
        String name=connector.getProgrammName(id);
        String beschreibung = connector.getProgrammBeschreibung(id);
        textViewProgrammName.setText(name);
        textViewBeschreibung.setText(beschreibung);

        btnAddProgramm.setText("Änderungen übernehmen");
        btnAddProgrammState=2;
    }

    //AlertDialogs
    private void dialogChangeName(){
        dialogBuilder = new AlertDialog.Builder(view.getContext());
        dialogBuilder.setMessage("Programm und Beschreibung ändern?");
        dialogBuilder.setTitle("Name ändern");
        dialogBuilder.setCancelable(true);

        dialogBuilder.setPositiveButton("OK",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                changeName();
                dialog.cancel();
            }
        });
        dialogBuilder.setNegativeButton("Abbruch",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //nur schließen
                dialog.cancel();
            }
        });

        dialog = dialogBuilder.create();
        dialog.show();
    }
    private void dialogError(String errorMessage){
        dialogBuilder = new AlertDialog.Builder(view.getContext());
        dialogBuilder.setMessage(errorMessage);
        dialogBuilder.setTitle("Fehler");
        dialogBuilder.setCancelable(true);

        dialogBuilder.setPositiveButton("OK",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        dialog = dialogBuilder.create();
        dialog.show();
    }
}