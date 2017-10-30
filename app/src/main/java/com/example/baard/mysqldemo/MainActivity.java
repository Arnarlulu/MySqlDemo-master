package com.example.baard.mysqldemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

//##### TO DO:  #####
//#####         hente ned bruker-ID for å lagre session     #####
//#####         Implememtere metode for sanntidsovervåkning #####
//#####         Ikke sende passord i klartekst              #####
//#####         Kommentere                                  #####

public class MainActivity extends AppCompatActivity {
    EditText Brukernavn_et, Passord_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Brukernavn_et = (EditText)findViewById(R.id.editTextBrukernavn);
        Passord_et = (EditText)findViewById(R.id.editTextPassord);

    }

    public void OnLogin(View view){
        String Brukernavn = Brukernavn_et.getText().toString();
        String Passord = Passord_et.getText().toString();
        String type = "login";

        BackgroundWorker backgroundworker = new BackgroundWorker(this);
        backgroundworker.execute(type, Brukernavn, Passord);
    }

    public void OpenReg(View view){
       startActivity(new Intent(this, Registrer.class)); //starter Register aktiviteten
    }


}