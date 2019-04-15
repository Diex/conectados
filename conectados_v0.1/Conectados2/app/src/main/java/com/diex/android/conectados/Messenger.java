package com.diex.android.conectados;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Messenger extends AsyncTask<URL, Integer, Long> {

    private URL serverAddres;
    private HttpURLConnection urlConnection;
    private String TAG = "SendDataToServer";
    private Context context;

    public Messenger(Context context){
        this.context = context;
    }


    String sesion = "";
    String visitiPointId = "";

    public void setSesion(String sesion) {
        this.sesion = sesion;
    }

    public void setVisitiPointId(String visitiPointId) {
        this.visitiPointId = visitiPointId;
    }

    protected Long doInBackground(URL... urls) {
        HashMap data = new HashMap();

        data.put("session", sesion);
        Date d = new Date();
        data.put("timestamp", ""+ d.getTime());
        data.put("visitPoint", visitiPointId);


        try {

            StringBuilder result = new StringBuilder();
            result.append("session="+sesion);

//            OutputStream os = urlConnection.getOutputStream();
//
//            os.write();

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
    }

    protected void onProgressUpdate(Integer... progress) {

//            setProgressPercent(progress[0]);
    }

    protected void onPostExecute(Long result) {
        Toast.makeText(context, "mensage enviado...", Toast.LENGTH_SHORT).show();
//            showDialog("Downloaded " + result + " bytes");
    }

    public void connectToServer(){
        try {
            serverAddres = new URL("http://192.168.0.7:8000");
            urlConnection = (HttpURLConnection) serverAddres.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            urlConnection.setRequestProperty("User-Agent", "diex");
            urlConnection.setUseCaches (false);
            urlConnection.setChunkedStreamingMode(0);


            Log.i(TAG, "url connection ok");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//    public void postRequest(){
//        doInBackground();
//    }


}
