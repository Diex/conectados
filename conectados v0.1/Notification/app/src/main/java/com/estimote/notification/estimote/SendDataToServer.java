package com.estimote.notification.estimote;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

public class SendDataToServer extends AsyncTask<URL, Integer, Long> {

    private URL serverAddres;
    private HttpURLConnection urlConnection;
    private String TAG = "SendDataToServer";
    private Context context;

    public SendDataToServer(Context context){
        this.context = context;
    }

    protected Long doInBackground(URL... urls) {
        HashMap data = new HashMap();

        data.put("fname", "vinod");
        data.put("fphone", "1234567890");
        data.put("femail", "abc@gmail.com");
        data.put("fcomment", "Help");

        try {

            OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
            JSONObject jso = new JSONObject(data);
            wr.write( jso.toString());
            wr.flush();
            Log.i(TAG, jso.toString());
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            Log.i(TAG, "response...");
            byte[] contents = new byte[1024];

            int bytesRead = 0;
            String strFileContents = "";
            while((bytesRead = in.read(contents)) != -1) {
                strFileContents += new String(contents, 0, bytesRead);
            }

            Log.i(TAG, strFileContents);

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
//            urlConnection.setRequestMethod();


            Log.i(TAG, "url connection ok");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void postRequest(){
       doInBackground();
    }


}
