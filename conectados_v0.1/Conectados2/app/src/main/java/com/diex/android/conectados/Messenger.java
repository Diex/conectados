package com.diex.android.conectados;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;

public class Messenger extends AsyncTask<URL, Integer, Long> {

    private URL serverAddres;
    private HttpURLConnection urlConnection;
    private String TAG = "SendDataToServer";
    private Context context;

    public Messenger(Context context){
        this.context = context;
    }


    String sesion = "";
    String gameId = "";

    public void setSesion(String sesion) {
        this.sesion = sesion;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    protected Long doInBackground(URL... urls) {


        StringBuilder sbParams = new StringBuilder();
        HashMap<String, String> params;

        params = new HashMap<>();
        Date d = new Date();
        params.put("timestamp",  Long.toString(d.getTime()));
        params.put("session", sesion);
        params.put("gameId", gameId);
        

        int i = 0;

        for (String key : params.keySet()) {
            try {
                if (i != 0){
                    sbParams.append("&");
                }
                sbParams.append(key).append("=")
                        .append(URLEncoder.encode(params.get(key), "UTF-8"));

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            i++;
        }

        try{

            String url = "http://192.168.0.251:8000";
            URL urlObj = new URL(url);
            conn = (HttpURLConnection) urlObj.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept-Charset", "UTF-8");

            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);

            conn.setRequestProperty("Content-Length", ""+sbParams.toString().length());
            conn.setRequestProperty("Connection", "close");
            conn.connect();


            String paramsString = sbParams.toString();

            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(paramsString);
            wr.flush();
            wr.close();



            Log.i(TAG, sbParams.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            InputStream in = new BufferedInputStream(conn.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            Log.d("test", "result from server: " + result.toString());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }


        return 0L;
/*
        try {
            Date d = new Date();
            StringBuilder result = new StringBuilder();
            result.append("session=");
            result.append(URLEncoder.encode(sesion, "UTF-8"));
            result.append("&timestamp=");
            result.append(URLEncoder.encode(""+d.getTime(), "UTF-8"));
            result.append("&visitPoint=");
            result.append(URLEncoder.encode(gameId, "UTF-8"));
            result.append(gameId);


            urlConnection.setRequestProperty("Content-Length", ""+result.toString().length());
            DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
            wr.writeBytes(result.toString());
            wr.flush();
            wr.close();

            Log.i(TAG, result.toString());

        }catch (Exception e){
            Log.i(TAG, e.toString());
        }finally{
            urlConnection.disconnect();
        }

        return 0L;
        */
    }

    protected void onProgressUpdate(Integer... progress) {

//            setProgressPercent(progress[0]);
    }

    protected void onPostExecute(Long result) {
        Toast.makeText(context, "Estás visitando...", Toast.LENGTH_SHORT).show();
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(100);
        }
//            showDialog("Downloaded " + result + " bytes");
    }

    HttpURLConnection conn;

    public void connectToServer(){
        try {




//            serverAddres = new URL("http://192.168.0.7:8000");
//            urlConnection = (HttpURLConnection) serverAddres.openConnection();
//            urlConnection.setDoOutput(true);
//            urlConnection.setDoInput(true);
//            urlConnection.setRequestMethod("POST");
//            urlConnection.setRequestProperty("Content-Type",
//                    "text/plain");
//            urlConnection.setRequestProperty("User-Agent", "diex");
//            urlConnection.setUseCaches (false);
//            urlConnection.setChunkedStreamingMode(255);


            Log.i(TAG, "url connection ok");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//    public void postRequest(){
//        doInBackground();
//    }


}
