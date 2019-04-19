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
    final int MIN_TIME = 20;


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
    }

    VisitPoint preOldest;
    VisitPoint oldest;

    void inferAction(){

        System.out.println("------------- inferAction ---------");
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

    public void onContextChanged(ArrayList<ProximityZoneContext> pzc){
        // TODO parsear las id vs los id en las installations

        for(VisitPoint visitPoint : installations){     // para todos los puntos
            if(visitPointIsInZone(visitPoint, pzc)) {
                visitPoint.setIsOn();
            }else{
                visitPoint.setIsOff();
            }

        }

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
}
