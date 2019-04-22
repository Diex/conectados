package com.diex.android.conectados;

import android.content.Context;
import android.os.AsyncTask;
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
import java.util.HashMap;




public class Postino extends AsyncTask<URL, Integer, Long> {

    private URL serverAddres;
    private HttpURLConnection urlConnection;
    private String TAG = "SendDataToServer";
    private Context context;
    private final String URL;
    // TODO esto no sirve por la busca en 8.8.8.8
    //private final String URL = "http://sulkys-Mac-pro.local/8000";

    HashMap<String, String> params;


    public Postino(Context context){
        this.context = context;
        URL = context.getString(R.string.server_url);
        params = new HashMap<>();
    }

    String sesion = "";
    String name = "";
    String loc = "";
    String email = "";
    String age = "";

    public void setSesion(String sesion) {
        this.sesion = sesion;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAge(String age) {
        this.age = age;
    }

    HttpURLConnection conn;


    protected Long doInBackground(java.net.URL... urls) {


        StringBuilder sbParams = new StringBuilder();

          params.put("message", "visitor");
          params.put("session", sesion);
          params.put("name",  name);
          params.put("age", age);
          params.put("location", loc);
          params.put("email", email);



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

            String url = URL;
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

            Log.d("PostVisit:", "result from server: " + result.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (conn != null) {
                conn.disconnect(); // cierro la conexión despues de recibir la respuesta
            }
        }

        return 0L;
    }

    protected void onProgressUpdate(Integer... progress) {

    }

    protected void onPostExecute(Long result) {
        Toast.makeText(context, "Gracias por tu información...", Toast.LENGTH_SHORT).show();
//        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            v.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
//        } else {
//            //deprecated in API 26
//            v.vibrate(50);
//        }
    }










}
