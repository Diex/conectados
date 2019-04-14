package com.diex.android.conectados;

import android.app.Activity;
import android.content.Context;

import com.diex.android.conectados.estimote.VisitPoint;
import com.estimote.proximity_sdk.api.ProximityZoneContext;

import java.lang.reflect.Array;
import java.util.ArrayList;



public class VisitController {

    Activity app;
    Thread t;
    ArrayList<VisitPoint> installations;

    VisitPoint currentPoint = null;
    long timeSinceLastPointChanged = -1;




    public VisitController(Activity a){
        this.app = a;
        t = new Thread(){
            @Override
            public void run(){
                while(!isInterrupted()){
                    try {
                        Thread.sleep(1000);
                        inferAction();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        };

    }

    public void setInstallationsToMonitor(ArrayList<VisitPoint> installations){
        this.installations = installations;
    }

    void inferAction(){

    }

    public void onEnter(ProximityZoneContext s){
        updateVisitPoint(s);
    }

    public void onContextChanged(ArrayList<ProximityZoneContext> pzc){
        // TODO parsear las id vs los id en las installations

        setPointsOff();

        for (ProximityZoneContext zone : pzc){
            updateVisitPoint(zone);
        }


        // updatear el tiempo de presencia en cada uno
        // determinar cual es la locacion acutal
        // si cambio...
        // y si cambio cuanto tiempo hace que esta ahi parado y disparar un evento de cambio de pantalla

        //        System.out.println("|||| something change...: ");
//        for (ProximityZoneContext p : pzc) {
//            printContext(p);
//            createVisitZone(p);
//        }


    }

    void setPointsOff(){
        for(VisitPoint vp : installations){
            vp.setIsOff();
        }
    }

    void updateVisitPoint(ProximityZoneContext s){
          VisitPoint inContext = findPoint(s);
          if(inContext != null && inContext.getId().equals(s.getAttachments().get("beaconId"))) inContext.setIsOn();
    }


    VisitPoint findPoint(ProximityZoneContext s){
        for(VisitPoint vp : installations){
            if(vp.getId().equals(s.getAttachments().get("beaconId"))){
                return vp;
            }
        }
        return null;
    }

}
