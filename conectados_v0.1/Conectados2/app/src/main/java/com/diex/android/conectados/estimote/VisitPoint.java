package com.diex.android.conectados.estimote;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;


public class VisitPoint {

    private long enterTime = -1;
    private long exitTime = 0;
    private int visits = 0;

    // data para mostrar...
    private String id;

    private String title;
    private String description;
    private String img;


    public VisitPoint(){

    }

    public void isClose(){
        Date d = new Date();
        enterTime = d.getTime();
    }

    public void exit(){
        // escribo a la db

    }

    @Override
    public boolean equals(Object obj) {
        return this.id.equals(obj);
    }

    public static VisitPoint fromJSONObject(JSONObject data){
        VisitPoint vp = new VisitPoint();

        try {
            vp.id = data.getString("beaconId");
            vp.title = data.getString("gameId");
            vp.description = data.getString("description");
            vp.img = data.getString("img");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return vp;
    }



    public String toString(){
        return "beaconId: " + id + "\n"+
                "title: " + title + "\n"+
                "description: " + description+ "\n"+
                "img: " + img + "\n"+
                "---------------";
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImg() {
        return img;
    }
}
