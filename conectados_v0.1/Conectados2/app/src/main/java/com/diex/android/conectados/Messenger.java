package com.diex.android.conectados;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.DataOutputStream;
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
    String visitPointId = "";

    public void setSesion(String sesion) {
        this.sesion = sesion;
    }

    public void setVisitPointId(String visitPointId) {
        this.visitPointId = visitPointId;
    }

    protected Long doInBackground(URL... urls) {



        try {
            Date d = new Date();
            StringBuilder result = new StringBuilder();
            result.append("session=");
//            result.append(URLEncoder.encode(sesion, "UTF-8"));
            result.append(sesion);
            result.append("&timestamp=");
//            result.append(URLEncoder.encode(""+d.getTime(), "UTF-8"));
            result.append(""+d.getTime());
            result.append("&visitPoint=");
//            result.append(URLEncoder.encode(visitPointId+"1234567890", "UTF-8"));
            result.append(visitPointId);
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
            urlConnection.setChunkedStreamingMode(255);


            Log.i(TAG, "url connection ok");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//    public void postRequest(){
//        doInBackground();
//    }


}
