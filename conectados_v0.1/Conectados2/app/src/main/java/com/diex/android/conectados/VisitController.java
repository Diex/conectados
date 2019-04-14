package com.diex.android.conectados;

import android.app.Activity;

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

    VisitPoint oldest;


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

    }

    public void setInstallationsToMonitor(ArrayList<VisitPoint> installations){
        this.installations = installations;
        oldest = installations.get(0);
    }

    void inferAction(){

        // HACK
        if(oldest.getId().equals("game_0")) return;

        System.out.println("------------- inferAction ---------");

        VisitPoint preOldest = oldest;

        for(VisitPoint visitPoint : installations){
            oldest = oldest.getPersistenceTime() > visitPoint.getPersistenceTime() ? oldest : visitPoint;
        }

        if(preOldest == oldest) return; // no hago nada porque sigue siendo el mismo

        System.out.println("oldest changed to: \n" + oldest.toString());
        app.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((MainActivity) app).setCurrentItem(oldest);
            }
        });

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
