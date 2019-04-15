package com.diex.android.conectados;

import android.content.Context;


import com.diex.android.conectados.estimote.VisitPoint;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class PointsBuilder {

    JSONObject installations;
    Context ctx;

    public PointsBuilder(Context ctx, String jsonDataPath){
        this.ctx = ctx;
        try {
            installations =  new JSONObject(loadJSONFromAsset(jsonDataPath));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    ArrayList<VisitPoint> getPointsList(){

        ArrayList<VisitPoint> vps = new ArrayList<VisitPoint>();
        try {
            int qty = installations.getJSONArray("items").length();

            for(int point = 0; point < qty ; point++){
                JSONObject item = installations.getJSONArray("items").getJSONObject(point);
                VisitPoint vp = VisitPoint.fromJSONObject(item);
                vps.add(vp);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return vps;
    }


    String loadJSONFromAsset(String path) {
        String json = null;
        try {
            InputStream is = ctx.getAssets().open(path);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
