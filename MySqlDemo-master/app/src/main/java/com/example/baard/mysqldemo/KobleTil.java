package com.example.baard.mysqldemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Random;

//##### TOD DO: #####
//#####         Implementere oppkobling av BT                       #####
//#####         plukke opp og videresende bruker ID for sessions    #####
//#####

public class KobleTil extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spinner;
    private Button koble, logge, overvaak;
    private static final String[] paths = {"Ingen enhet", "E4 1", "E4 2", "E4 3"};
    String mac_1 = "00ABCDEF";
    String mac_2 = "00AACDEF";
    String mac_3 = "00ABBDEF";
    String mac_con;
    String bruker_ID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_koble_til);

        bruker_ID= getIntent().getStringExtra("Bruker_ID");


        spinner = (Spinner) findViewById(R.id.spinner);
        koble = (Button) findViewById(R.id.buttonKobletil);
        logge = (Button) findViewById(R.id.buttonStartLogging);
        overvaak = (Button) findViewById(R.id.buttonOvervak);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(KobleTil.this, android.R.layout.simple_spinner_item, paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        logge.setEnabled(false);

            if (bruker_ID=="139") {
        overvaak.setClickable(true);
        }
        else overvaak.setClickable(false);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

        switch (position) {
            case 0:
                mac_con = "false";
                break;
            case 1:
                mac_con = mac_1;
                break;
            case 2:
                mac_con = mac_2;
                break;
            case 3:
                mac_con = mac_3;
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        mac_con = "false";
    }

    public void OnOppkobling(View view) {
        if (mac_con.equals("false")) {
            Toast.makeText(this, "Venligst velg en enhet du ønsker å koble til", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "Kobler til enhet: " + mac_con, Toast.LENGTH_SHORT).show();
            logge.setEnabled(true);
        }
    }

    public void OnLogge(View view) {
        Log.i("**********"," TRYKKET KNAPP LOGGE *************");
        Intent intent = new Intent(this, Logge.class);
        intent.putExtra("ID", mac_con);
        intent.putExtra("Bruker_ID",bruker_ID);
        startActivity(intent); //starter Register aktiviteten
    }

    public void OnOvervak(){
        Log.i("*****","OVERVÅK TRYKKET");
        String sesjon = "";


        Intent intent =new Intent(this, overvake.class);
        intent.putExtra("sesjon",sesjon);
        startActivity(intent);

    }
}
