package com.example.baard.mysqldemo;

import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//##### TO DO:  #####
//#####         fikse bugs i registrering   #####
//#####         Kryptere passord            #####


public class Registrer extends AppCompatActivity {

    EditText fornavn, etternavn, brukernavn, passord1, passord2;
    String str_fornavn, str_etternavn, str_brukernavn, str_passord1, str_passord2;
    Button registrer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrer);
        fornavn=(EditText)findViewById(R.id.editTextFornavn);
        etternavn=(EditText)findViewById(R.id.editTextEtternavn);
        brukernavn=(EditText)findViewById(R.id.editTextBrukernavn);
        passord1=(EditText)findViewById(R.id.editTextPassord1);
        passord2=(EditText)findViewById(R.id.editTextPassord2);
        registrer=(Button)findViewById(R.id.buttonOpprett);
    }

    public void OnReg(View view){
        str_fornavn=fornavn.getText().toString();
        str_etternavn=etternavn.getText().toString();
        str_brukernavn=brukernavn.getText().toString();
        str_passord1=passord1.getText().toString();
        str_passord2=passord2.getText().toString();
        String type = "reg";



        if(!str_passord1.equals(str_passord2)){

            Toast.makeText(this, "Passordene stemmer ikke overens, forsøk igjen", Toast.LENGTH_SHORT).show();
            passord1.setText("");
            passord2.setText("");

        }
        else if (str_passord1.length() < 5)
        {
            Toast.makeText(this, "Passord for kort. forsøk ett nytt", Toast.LENGTH_SHORT).show();
            passord1.setText("");
            passord2.setText("");
        }

        else {
            BackgroundWorker backgroundworker = new BackgroundWorker(this);
            backgroundworker.execute(type, str_fornavn, str_etternavn, str_brukernavn, str_passord1);
        }

    }
}
