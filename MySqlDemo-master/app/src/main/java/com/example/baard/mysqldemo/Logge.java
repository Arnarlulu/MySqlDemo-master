package com.example.baard.mysqldemo;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
//import java.util.logging.Handler;


//##### TO DO:  #####
//#####         finne ut hvordan funksjonen kan kjøre uten glitch med låst skjerm   #####
//#####         Legge til tilbake knapp                                             #####
//#####         Vatte inn data fra BT                                               #####
//#####         Implementere sessions                                               #####
//#####         Kommentere                                                          #####
//#####         kartlegge ressursbruk, burde dette vært en under-funskjon?           #####

public class Logge extends AppCompatActivity {

    public TextView tv_EDR, tv_HR, tv_BVP, tv_aks_x, tv_aks_y, tv_aks_z;
    public Button stopp, avslutt;
    public SeekBar stress;
    public String ID,bruker_ID;
    public Boolean forste;

    public Context context;

    private Timer timer;
    private TimerTask timerTask;
    final Handler handler = new Handler();


    public boolean fortsett;

    //Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i("********", " LOGGE initiert ************");
        ID = getIntent().getStringExtra("ID");
        bruker_ID = getIntent().getStringExtra("Bruker_ID");
        Log.i("*******","LOGGE HENTER EKSTRA VARIABLER. ID: "+ID+" Bruker_ID: "+bruker_ID);
        fortsett = false;
        forste = true;
        context=this;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logge);

        tv_EDR = (TextView) findViewById(R.id.textViewEDR);
        tv_HR = (TextView) findViewById(R.id.textViewHR);
        tv_BVP = (TextView) findViewById(R.id.textViewBVP);
        tv_aks_x = (TextView) findViewById(R.id.textViewAksX);
        tv_aks_y = (TextView) findViewById(R.id.textViewAksY);
        tv_aks_z = (TextView) findViewById(R.id.textViewAksZ);
        stopp = (Button) findViewById(R.id.buttonStopp);
        avslutt = (Button) findViewById(R.id.buttonAvslutt);
        stress = (SeekBar) findViewById(R.id.seekBarStress);

        stress.setClickable(false);
        stress.setMax(600);

        stopp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!fortsett) {
                    stopTimerTask();
                    fortsett = true;
                }
                else {
                    startTimer();
                    fortsett = false;
                }
            }
        });

        avslutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String type ="siste";
                BackgroundWorker backgroundworker1 = new BackgroundWorker(context);
                backgroundworker1.execute(type, ID, bruker_ID);

                startActivity(new Intent(context, MainActivity.class));
            }
        });


    }
    @Override
    protected void onPause(){
        super.onPause();
        stopTimerTask();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startTimer();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

        startActivity(new Intent(this, MainActivity.class));
    }

    public void startTimer(){
        timer = new Timer();
        startTimerTask();
        timer.schedule(timerTask, 1000, 1000);
    }

    public void stopTimerTask(){
        if (timer != null){
            timer.cancel();
            timer = null;
        }
    }

    public void startTimerTask(){
        Log.i("*******","TIMER TASK STARTET");
        timerTask = new TimerTask() {
            @Override
            public void run() {
              handler.post(new Runnable() {
                  @Override
                  public void run() {
                      // SETT INN HER
                      Log.i("*******","TIMERTASK KALLER LOGGE");
                      logge();
                  }
              });
            }
        };
    }



    public void logge(){
        Random randtall = new Random();
        Double hjelp;
        int stressint;

        String type = "forste";


        hjelp=  (100* (4.5*randtall.nextDouble()) )  ;
        hjelp = Double.valueOf(Math.round(hjelp));
        stressint=hjelp.intValue();
        hjelp= hjelp/100;
        String EDR = Double.toString(hjelp);

        hjelp=  (100* (50+(230-50)*randtall.nextDouble()) )  ;
        hjelp = Double.valueOf(Math.round(hjelp));
        hjelp= hjelp/100;
        String HR = Double.toString(hjelp);

        hjelp=  (100* (1+(20-1)*randtall.nextDouble()) )  ;
        hjelp = Double.valueOf(Math.round(hjelp));
        hjelp= hjelp/100;
        String BVP = Double.toString(hjelp);

        hjelp=  (100* (10*randtall.nextDouble()) )  ;
        hjelp = Double.valueOf(Math.round(hjelp));
        hjelp= hjelp/100;
        String aks_x = Double.toString(hjelp);

        hjelp=  (100* (10*randtall.nextDouble()) )  ;
        hjelp = Double.valueOf(Math.round(hjelp));
        hjelp= hjelp/100;
        String aks_y = Double.toString(hjelp);

        hjelp=  (100* (10*randtall.nextDouble()) )  ;
        hjelp = Double.valueOf(Math.round(hjelp));
        hjelp= hjelp/100;
        String aks_z = Double.toString(hjelp);

        stress.setProgress(stressint);

        tv_EDR.setText("EDR: "+EDR);
        tv_HR.setText("HR: "+HR);
        tv_BVP.setText("BVP: "+BVP);
        tv_aks_x.setText("Aks x: "+aks_x);
        tv_aks_y.setText("Aks y: "+aks_y);
        tv_aks_z.setText("Aks z: "+aks_z);

        Log.i("*******","KALLER BCKGRNDWRKR");

        if (forste){

            BackgroundWorker backgroundworker1 = new BackgroundWorker(this);
            backgroundworker1.execute(type,ID, bruker_ID);


            forste = false;
        }

        type="logge";

            BackgroundWorker backgroundworker = new BackgroundWorker(this);
            backgroundworker.execute(type, EDR, HR, BVP, aks_x, aks_y, aks_z, ID, bruker_ID);




        Log.i("*******"," BACKGROUNDWORKER FERDIG *************");

    }
}
