package com.diex.android.conectados.estimote;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.diex.android.conectados.Visitable;
import com.estimote.proximity_sdk.api.EstimoteCloudCredentials;
import com.estimote.proximity_sdk.api.ProximityObserver;
import com.estimote.proximity_sdk.api.ProximityObserverBuilder;
import com.estimote.proximity_sdk.api.ProximityZone;
import com.estimote.proximity_sdk.api.ProximityZoneBuilder;
import com.estimote.proximity_sdk.api.ProximityZoneContext;



import java.util.ArrayList;
import java.util.Set;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

//
// Running into any issues? Drop us an email to: contact@estimote.com
//

public class ProximityContentManager {

    private Context ctx;

    private EstimoteCloudCredentials cloudCredentials;
    private ProximityObserver.Handler proximityObserverHandler;
    private Visitable visitable;


    public ProximityContentManager(Context context, EstimoteCloudCredentials cloudCredentials) {
        this.ctx = context;
        this.cloudCredentials = cloudCredentials;
    }

    public void start() {

        ProximityObserver proximityObserver = new ProximityObserverBuilder(ctx, cloudCredentials)
                .withBalancedPowerMode()
                .withEstimoteSecureMonitoringDisabled()
                .withTelemetryReportingDisabled()
                .onError(new Function1<Throwable, Unit>() {
                    @Override
                    public Unit invoke(Throwable throwable) {


                        Log.e("app", "proximity observer error: " + throwable);

                        Toast toast = Toast.makeText(ctx, "Reinicie Blutooth...", Toast.LENGTH_SHORT);
                        toast.show();


                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ((Activity)ctx).finish();
                            }
                        }, Toast.LENGTH_SHORT);

                        return null;
                    }
                })
                .build();



        ProximityZone closer = new ProximityZoneBuilder()
                .forTag("game")
                .inCustomRange(1.0)
                .onEnter(new Function1<ProximityZoneContext, Unit>() {
                    @Override
                    public Unit invoke(ProximityZoneContext context) {
                        visitable.onEnterCloseZone(context);
                        return null;
                    }
                })
                .build();

        ProximityZone zone = new ProximityZoneBuilder()
                .forTag("game")
                .inCustomRange(1.0)
                .onContextChange(new Function1<Set<? extends ProximityZoneContext>, Unit>() {
                    @Override
                    //The type with only one value: the Unit object. This type corresponds to the void type in Java.
                    //A higher-order function is a function that takes functions as parameters, or returns a function.
                    public Unit invoke(Set<? extends ProximityZoneContext> contexts) {
                        ArrayList<ProximityZoneContext> data = new ArrayList<ProximityZoneContext>();
                        for (ProximityZoneContext proximityContext : contexts) {
                          data.add(proximityContext);
//                          System.out.println(proximityContext.toString());
                        }
                        visitable.onContextChange(data);
                        return null;
                    }
                })
//                .onEnter(new Function1<ProximityZoneContext, Unit>() {
//                    @Override
//                    public Unit invoke(ProximityZoneContext context) {
//                        visitable.onEnterZone(context);
//                        return null;
//                    }
//                })
//                .onExit(new Function1<ProximityZoneContext, Unit>() {
//                    @Override
//                    public Unit invoke(ProximityZoneContext context) {
//                        visitable.onExitZone(context);
//                        Log.d("app", "Bye bye, come again!");
//                        return null;
//                    }
//                })
                .build();

        proximityObserverHandler = proximityObserver.startObserving(zone);
    }

    public void stop() {
        proximityObserverHandler.stop();
    }

    // Setter
    public void setOnEnter(Visitable visitable){
        this.visitable = visitable;
    }
}
