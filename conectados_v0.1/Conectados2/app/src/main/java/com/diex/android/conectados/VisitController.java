package com.diex.android.conectados;

import android.app.Activity;
import android.os.Handler;

import com.diex.android.conectados.estimote.VisitPoint;
import com.estimote.proximity_sdk.api.ProximityZoneContext;

import java.util.ArrayList;



public class VisitController {

    Activity app;
    Thread t;
    ArrayList<VisitPoint> installations;

    VisitPoint currentPoint = null;
    long timeSinceLastPointChanged = -1;

    int lookupTime = 3000;
    final int MIN_TIME = 10;



    public VisitController(Activity a){
        this.app = a;
        t = new Thread(){
            @Override
            public void run(){
                while(!isInterrupted()){
                    try {
                        Thread.sleep(lookupTime);
                        inferAction();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        };

        t.start();


//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                //Do something after 100ms
//                for(VisitPoint visitPoint : installations){
//                    if(visitPoint.getId().equals("game_0")) {
//                       installations.remove(visitPoint);
//                        oldest = installations.get(0);
//                        return;
//                    }
//
//                }
//            }
//        }, 10000);

    }

    public void setInstallationsToMonitor(ArrayList<VisitPoint> installations){
        this.installations = installations;
//        oldest = installations.get(0);
    }

    VisitPoint preOldest;
    VisitPoint oldest;

    void inferAction(){

        System.out.println("------------- inferAction ---------");


//        if(oldest != null) System.out.println("oldest: " + oldest.getId() + " oldest.persistenceTime: " + oldest.getPersistenceTime());

        ArrayList<VisitPoint> actives = new ArrayList<VisitPoint>();


        String acc = "";
        for(VisitPoint visitPoint : installations){
            acc += "-0";
            if(!visitPoint.isActive()) continue;
            actives.add(visitPoint);
            acc = acc.substring(0,acc.length() - 1);
            acc += "1";
        }

        System.out.println(acc);

        if(actives.size() < 1) return;

        oldest = actives.get(0);
        for(VisitPoint activePoint : actives){
            if(activePoint.getPersistenceTime() < MIN_TIME) continue; // no lo tengo en cuenta si paso menos de 5 segs
            oldest = oldest.getPersistenceTime() > activePoint.getPersistenceTime() ? oldest : activePoint;
        }


        // si no es el mismo en el que estaba ...
        if(preOldest == oldest) return;

        // lo cambio
        System.out.println("oldest changed to: \n" + oldest.toString());

        app.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((MainActivity) app).setCurrentItem(oldest);
            }
        });

        // y ahora es el nuevo preOldest
        preOldest = oldest;

    }

//    public void onEnter(ProximityZoneContext s){
//        updateVisitPoint(s);
//    }

    public void onContextChanged(ArrayList<ProximityZoneContext> pzc){
        // TODO parsear las id vs los id en las installations

        for(VisitPoint visitPoint : installations){     // para todos los puntos
            if(visitPointIsInZone(visitPoint, pzc)) {
                visitPoint.setIsOn();
            }else{
                visitPoint.setIsOff();
            }

        }

//        setPointsOff();
//
//        for (ProximityZoneContext zone : pzc){
//            updateVisitPoint(zone);
//        }


        // updatear el tiempo de presencia en cada uno
        // determinar cual es la locacion acutal
        // si cambio...
        // y si cambio cuanto tiempo hace que esta ahi parado y disparar un evento de cambio de pantalla


    }
    public void onExit(ProximityZoneContext pzc){

    }

    boolean visitPointIsInZone(VisitPoint visitPoint, ArrayList<ProximityZoneContext> pcz){
        String id = visitPoint.getId();
        for(ProximityZoneContext zone : pcz){
            if(zone.getAttachments().get("beaconId").equals(id)){
                return true; // early return??
            }
        }
        return false;
    }


//    void setPointsOff(){
//        for(VisitPoint vp : installations){
//            vp.setIsOff();
//        }
//    }

//    void updateVisitPoint(ProximityZoneContext s){
//          VisitPoint inContext = findPoint(s);
//          if(inContext != null && inContext.getId().equals(s.getAttachments().get("beaconId"))) inContext.setIsOn();
//    }


//    VisitPoint findPoint(ProximityZoneContext s){
//        for(VisitPoint vp : installations){
//            if(vp.getId().equals(s.getAttachments().get("beaconId"))){
//                return vp;
//            }
//        }
//        return null;
//    }

}
