package com.example.baard.mysqldemo;

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

public class overvake extends AppCompatActivity {

    public TextView tv_EDR, tv_HR, tv_BVP, tv_aks_x, tv_aks_y, tv_aks_z;
    public Button stopp;
    public SeekBar stress;
    public String ID;

    private Timer timer;
    private TimerTask timerTask;
    final Handler handler = new Handler();


    public boolean fortsett;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i("********", " OVERVÅK initiert ************");
        ID = getIntent().getStringExtra("ID");
        fortsett = false;
        //context=this;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overvake);

        tv_EDR = (TextView) findViewById(R.id.textViewEDR);
        tv_HR = (TextView) findViewById(R.id.textViewHR);
        tv_BVP = (TextView) findViewById(R.id.textViewBVP);
        tv_aks_x = (TextView) findViewById(R.id.textViewAksX);
        tv_aks_y = (TextView) findViewById(R.id.textViewAksY);
        tv_aks_z = (TextView) findViewById(R.id.textViewAksZ);
        stopp = (Button) findViewById(R.id.buttonStopp);
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

    //NYE FUNKSJONER HER

    public void logge(){    //hele denne funksjonen er wastable
        Random randtall = new Random();
        Double hjelp=3.0;
        int stressint=3;

        String type = "overvak";


        String EDR = Double.toString(hjelp);
        String HR = Double.toString(hjelp);
        String BVP = Double.toString(hjelp);
        String aks_x = Double.toString(hjelp);
        String aks_y = Double.toString(hjelp);
        String aks_z = Double.toString(hjelp);

        stress.setProgress(stressint);

        tv_EDR.setText("EDR: "+EDR);
        tv_HR.setText("HR: "+HR);
        tv_BVP.setText("BVP: "+BVP);
        tv_aks_x.setText("Aks x: "+aks_x);
        tv_aks_y.setText("Aks y: "+aks_y);
        tv_aks_z.setText("Aks z: "+aks_z);

        Log.i("*******","KALLER BCKGRNDWRKR");

        BackgroundWorker backgroundworker = new BackgroundWorker(this);
        backgroundworker.execute(type, EDR, HR, BVP, aks_x, aks_y, aks_z, ID);


        Log.i("*******"," BACKGROUNDWORKER FERDIG *************");

    }


}
