package com.example.baard.mysqldemo;
//ENDRINGER IFM TILSETTING AV BLUETOOTH
import android.Manifest;
import android.app.ActionBar;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.empatica.empalink.ConnectionNotAllowedException;
import com.empatica.empalink.EmpaDeviceManager;
import com.empatica.empalink.config.EmpaSensorStatus;
import com.empatica.empalink.config.EmpaSensorType;
import com.empatica.empalink.config.EmpaStatus;
import com.empatica.empalink.delegate.EmpaDataDelegate;
import com.empatica.empalink.delegate.EmpaStatusDelegate;

//ENDRING SLUTT


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
//#####         etablere kobling til klokken
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

    //ENDRINGER FOR BLUETOOTH

    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_pERMISSION_ACCESS_COARSE_LOCATION = 1;

    private static final long STREAMING_TIME = 10000;
    // Denne *1000 bestemmer hvor mange sekunder koblingen blir kjørt

    private static final String EMPATICA_API_KEY = "234acf07689e4d2aacfe46bf5b6a816c";
    //Merk at dette er API-koden som skal til for å koble til klokken

    private EmpaDeviceManager deviceManager = null;

    private TextView accel_xLabel;
    private TextView accel_yLabel;
    private TextView accel_zLabel;
    private TextView bvpLabel;
    private TextView edaLabel;
    private TextView ibiLabel;
    private TextView temperatureLabel;
    private TextView batteryLabel;
    private TextView statusLabel;
    private TextView deviceNameLabel;
    private RelativeLayout dataCnt;





    //ENDRINGER FOR BLUETOOTH SLUTT

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
        accel_xLabel = (TextView) findViewById(R.id.textViewAksX);
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

    //ENDRINGER IFM IMPLEMENTERING AV BLUETOOTH

    private void initEmpaticaDeviceManager() {
        // Android 6 (API level 23) now require ACCESS_COARSE_LOCATION permission to use BLE
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_COARSE_LOCATION }, REQUEST_PERMISSION_ACCESS_COARSE_LOCATION);
        } else {
            // Create a new EmpaDeviceManager. MainActivity is both its data and status delegate.
            deviceManager = new EmpaDeviceManager(getApplicationContext(), this, this);

            if (TextUtils.isEmpty(EMPATICA_API_KEY)) {
                new AlertDialog.Builder(this)
                        .setTitle("Warning")
                        .setMessage("Please insert your API KEY")
                        .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // without permission exit is the only way
                                finish();
                            }
                        })
                        .show();
                return;
            }
            // Initialize the Device Manager using your API key. You need to have Internet access at this point.
            deviceManager.authenticateWithAPIKey(EMPATICA_API_KEY);
        }
    }

    public void logge(){
        Random randtall = new Random();
        Double hjelp;
        int stressint;

        String type = "forste";


     /*   hjelp=  (100* (4.5*randtall.nextDouble()) )  ;
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
*/
        Log.i("*******","KALLER BCKGRNDWRKR");

        if (forste){

            BackgroundWorker backgroundworker1 = new BackgroundWorker(this);
            backgroundworker1.execute(type,ID, bruker_ID);


            forste = false;
        }

        type="logge";

           /* BackgroundWorker backgroundworker = new BackgroundWorker(this);
            backgroundworker.execute(type, EDR, HR, BVP, aks_x, aks_y, aks_z, ID, bruker_ID);




        Log.i("*******"," BACKGROUNDWORKER FERDIG *************");*/

    }
}
