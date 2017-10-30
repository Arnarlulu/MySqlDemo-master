package com.example.baard.mysqldemo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import static android.support.v4.content.ContextCompat.startActivity;

/**
 * Created by baard on 14.09.2017.
 */

//##### TO DO:  #####
//#####         Implemntere sessions                    #####
//#####         Returnere BrukerID fra innlogging       #####
//#####         Implementere sanntids overvåkning       #####
//#####         Fikse bugs i registrering/innlogging    #####
//#####         kvalitetssikre varselboks               #####

public class BackgroundWorker extends AsyncTask <String, Void, String> {
    Context context;
    AlertDialog alertDialog;
    BackgroundWorker(Context ctx) {
        context = ctx;
    }


    @Override
    protected String doInBackground(String... params) {
        Log.i("**********"," BACKGROUNDWORKER STARTER *************");
        String type = params[0];
        String login_url = "http://stressapp.no/login.php";
        String reg_url = "http://stressapp.no/registrering.php";
        String logge_url = "http://stressapp.no/logge.php";
        String forste_sesjon_url = "http://stressapp.no/forste_sesjon.php";
        String siste_url = "http://stressapp.no/siste_sesjon.php";
        if (type.equals("login")) {
            try {
                String brukernavn = params[1];
                String passord = params[2];
                URL url= new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputstream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputstream, "UTF-8"));

                String postData = URLEncoder.encode("brukernavn","UTF-8")+"="+URLEncoder.encode(brukernavn,"UTF-8")+"&"+
                        URLEncoder.encode("passord","UTF-8")+"="+URLEncoder.encode(passord,"UTF-8");

                bufferedWriter.write(postData);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputstream.close();

                InputStream inputStream= httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result="";
                String line;
                while ((line = bufferedReader.readLine()) != null){
                    result +=line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                Log.i("*******","LOGIN GJENNOMFØRT, RESULT: "+result);
                return result;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (type.equals("reg")){
            try {
                String fornavn = params[1];
                String etternavn = params[2];
                String brukernavn = params[3];
                String passord = params[4];

                URL url= new URL(reg_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputstream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputstream, "UTF-8"));

                String postData = URLEncoder.encode("Fornavn","UTF-8")+"="+URLEncoder.encode(fornavn,"UTF-8")+"&"+
                        URLEncoder.encode("Etternavn","UTF-8")+"="+URLEncoder.encode(etternavn,"UTF-8")+"&"+
                        URLEncoder.encode("Brukernavn","UTF-8")+"="+URLEncoder.encode(brukernavn,"UTF-8")+"&"+
                        URLEncoder.encode("Passord","UTF-8")+"="+URLEncoder.encode(passord,"UTF-8");

                bufferedWriter.write(postData);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputstream.close();

                InputStream inputStream= httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result="";
                String line;
                while ((line = bufferedReader.readLine()) != null){
                    result +=line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();


                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        else if (type.equals("forste"))
        {
            Log.i("*******","BKGRNDWRKER FORSTE == TRUE");
            try{

                String ID = params[1];
                String bruker_ID = params[2];

                URL url= new URL(forste_sesjon_url);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputstream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputstream, "UTF-8"));

                String postData =
                                URLEncoder.encode("ID","UTF-8")+"="+URLEncoder.encode(ID,"UTF-8")+"&"+
                                URLEncoder.encode("Bruker_ID","UTF-8")+"="+URLEncoder.encode(bruker_ID,"UTF-8");

                Log.i("*******","I forste, URL connections oppretett. string postdata: "+postData);


                bufferedWriter.write(postData);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputstream.close();

                Log.i("*******","i forste, outputstream lukket");

                InputStream inputStream= httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

                Log.i("*******","i forste, inputstream lukket");

                String result="";
                String line;

                while ((line = bufferedReader.readLine()) != null){
                    result +=line;
                }

                Log.i("********","Første sesjon kjørt, result: "+result);

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                String r = "forste sesjon ok";
                return r;

            } catch (MalformedURLException e) {
                Log.i("*******","Catch i forste, malformed URL");
                e.printStackTrace();
            } catch (IOException e) {
                Log.i("********","Catch i forste, io Exception");
                e.printStackTrace();
            }

        }

        else if(type.equals("logge")){
            Log.i("**********"," BACKGROUNDWORKER LOGGE *************");
            try {
                String EDR=params[1];
                String HR= params[2];
                String BVP= params[3];
                String aks_x= params[4];
                String aks_y= params[5];
                String aks_z= params[6];
                String ID= params[7];
                String bruker_ID = params[8];

                //#####    Skriver data til databasen   #####

                URL url= new URL(logge_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputstream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputstream, "UTF-8"));

                String postData = URLEncoder.encode("EDR","UTF-8")+"="+URLEncoder.encode(EDR,"UTF-8")+"&"+
                        URLEncoder.encode("HR","UTF-8")+"="+URLEncoder.encode(HR,"UTF-8")+"&"+
                        URLEncoder.encode("BVP","UTF-8")+"="+URLEncoder.encode(BVP,"UTF-8")+"&"+
                        URLEncoder.encode("aks_x","UTF-8")+"="+URLEncoder.encode(aks_x,"UTF-8")+"&"+
                        URLEncoder.encode("aks_y","UTF-8")+"="+URLEncoder.encode(aks_y,"UTF-8")+"&"+
                        URLEncoder.encode("aks_z","UTF-8")+"="+URLEncoder.encode(aks_z,"UTF-8")+"&"+
                        URLEncoder.encode("ID","UTF-8")+"="+URLEncoder.encode(ID,"UTF-8")+"&"+
                        URLEncoder.encode("Bruker_ID","UTF-8")+"="+URLEncoder.encode(bruker_ID,"UTF-8");


                bufferedWriter.write(postData);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputstream.close();

                InputStream inputStream= httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result="";
                String line;
                while ((line = bufferedReader.readLine()) != null){
                    result +=line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                Log.i("*******","BAKGRNDWRKR KJØRT LOGGE, RESULT: "+result);

                String r="Logg ok";

                return r;


        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        }

        else if(type.equals("siste")){
            Log.i("**********"," BACKGROUNDWORKER SISTE *************");
            try {
                //String sensor_ID=params[1];
                String bruker_ID=params[2];


                //#####    Skriver data til databasen   #####

                URL url= new URL(siste_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                Log.i("**********"," SISTE  Opprettet URL ");

                //#####     NETTOPP KILIPPET INN    #####

                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                Log.i("**********"," SISTE  Poste funksjon opprettet ");

                OutputStream outputstream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputstream, "UTF-8"));

                String postData = URLEncoder.encode("bruker_ID","UTF-8")+"="+URLEncoder.encode(bruker_ID,"UTF-8");

                Log.i("**********"," SISTE  outputstream starter ");

                bufferedWriter.write(postData);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputstream.close();

                Log.i("**********"," SISTE  outputstream ferdig");

                //#####     NETTOPP KLIPPET INN     #####


                InputStream inputStream= httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

                Log.i("**********"," SISTE  Inputstream ferdig ");

                String result="";
                String line;
                while ((line = bufferedReader.readLine()) != null){
                    result +=line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                Log.i("*******","BAKGRNDWRKR KJØRT SISTE, RESULT: "+result);

                String r="siste ok";

                return r;
            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }



    @Override
    protected void onPreExecute() {
        alertDialog= new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Status");

    }


    //#######   HER MÅ DET RYDDES OPP!!!    #####
    @Override
    protected void onPostExecute(String aVoid) {

        if (aVoid.matches("\\d+")){ //##### om aVoid inneholder en int, kjøres denne if'en

            //##### Starter koble til, sender bruker ID #####  !(aVoid).equals("Kunne ikke logge inn")  !(aVoid).equals("Data Registrert")

            Log.i("*********","LOGIN GODKJENT --- bruker ID: "+ aVoid.toString());
            Intent intent = new Intent(this.context , KobleTil.class);
            intent.putExtra("Bruker_ID",aVoid.toString());
            context.startActivity(intent);

        }


        else if(aVoid == "Logg ok"){
            Log.i("*******","BCKGRNDWRKR I IF LOGG OK , aVoid: "+aVoid);
        }

        else if(!(aVoid).equals("Data Registrert") && !(aVoid).equals("forste sesjon ok") && !(aVoid).equals("siste ok")){
            alertDialog.setMessage(aVoid);
            alertDialog.show();
        }

    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
