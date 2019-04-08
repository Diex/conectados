package com.diex.android.conectados.estimote;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.diex.android.conectados.MainActivity;
import com.diex.android.conectados.Visitable;
import com.estimote.proximity_sdk.api.EstimoteCloudCredentials;
import com.estimote.proximity_sdk.api.ProximityObserver;
import com.estimote.proximity_sdk.api.ProximityObserverBuilder;
import com.estimote.proximity_sdk.api.ProximityZone;
import com.estimote.proximity_sdk.api.ProximityZoneBuilder;
import com.estimote.proximity_sdk.api.ProximityZoneContext;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

//
// Running into any issues? Drop us an email to: contact@estimote.com
//

public class ProximityContentManager {

    private Context ctx;
    private ProximityContentAdapter proximityContentAdapter;
    private EstimoteCloudCredentials cloudCredentials;
    private ProximityObserver.Handler proximityObserverHandler;
    private Visitable visitable;

    public ProximityContentManager(Context context, ProximityContentAdapter proximityContentAdapter, EstimoteCloudCredentials cloudCredentials) {
        this.ctx = context;
        this.proximityContentAdapter = proximityContentAdapter;
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

        ProximityZone zone = new ProximityZoneBuilder()
                .forTag("game")
                .inCustomRange(1.0)


//                .onContextChange(new Function1<Set<? extends ProximityZoneContext>, Unit>() {
//                    @Override
//                    //The type with only one value: the Unit object. This type corresponds to the void type in Java.
//                    //A higher-order function is a function that takes functions as parameters, or returns a function.
//                    public Unit invoke(Set<? extends ProximityZoneContext> contexts) {
//
//                        List<ProximityContent> nearbyContent = new ArrayList<>(contexts.size());
//
//                        for (ProximityZoneContext proximityContext : contexts) {
//                            String title = proximityContext.getAttachments().get("gameId");
//                            if (title == null) {
//                                title = "unknown";
//                            }
//                            String subtitle = Utils.getShortIdentifier(proximityContext.getDeviceId());
//                            String c = proximityContext.getAttachments().get("color");
//                            nearbyContent.add(new ProximityContent(title, subtitle, c));
//                        }
//
//                        proximityContentAdapter.setNearbyContent(nearbyContent);
//                        proximityContentAdapter.notifyDataSetChanged();
//
//                        return null;
//                    }
//                })
                .onEnter(new Function1<ProximityZoneContext, Unit>() {
                    @Override
                    public Unit invoke(ProximityZoneContext context) {
                        String deskOwner = context.getAttachments().get("desk-owner");
                        Log.d("app", "Welcome to " + deskOwner + "'s desk");
                        visitable.onEnterZone(context.getAttachments().get("gameId"));
                        return null;
                    }
                })
                .onExit(new Function1<ProximityZoneContext, Unit>() {
                    @Override
                    public Unit invoke(ProximityZoneContext context) {
                        visitable.onExitZone(context.getAttachments().get("gameId"));
                        Log.d("app", "Bye bye, come again!");
                        return null;
                    }
                })
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
