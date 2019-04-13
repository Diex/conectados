package com.diex.android.conectados;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.diex.android.conectados.estimote.ProximityContentManager;
import com.diex.android.conectados.estimote.VisitPoint;
import com.estimote.mustard.rx_goodness.rx_requirements_wizard.Requirement;
import com.estimote.mustard.rx_goodness.rx_requirements_wizard.RequirementsWizardFactory;
import com.estimote.proximity_sdk.api.*;

import java.util.ArrayList;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;

public class MainActivity extends AppCompatActivity implements Visitable{

    public EstimoteCloudCredentials cloudCredentials =
            new EstimoteCloudCredentials("aboiledtiger-gmail-com-s-p-bux", "78c1cf864c4fb4f63e3758f9697aefdd");

    private ProximityContentManager proximityContentManager;

    ViewPager itemsViewer;
    ArrayList<VisitPoint> installations;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);


//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });



        // estimoe

        RequirementsWizardFactory
                .createEstimoteRequirementsWizard()
                .fulfillRequirements(this,
                        new Function0<Unit>() {
                            @Override
                            public Unit invoke() {
                                Log.d("app", "requirements fulfilled");
                                startProximityContentManager();
                                return null;
                            }
                        },
                        new Function1<List<? extends Requirement>, Unit>() {
                            @Override
                            public Unit invoke(List<? extends Requirement> requirements) {
                                Log.e("app", "requirements missing: " + requirements);
                                return null;
                            }
                        },
                        new Function1<Throwable, Unit>() {
                            @Override
                            public Unit invoke(Throwable throwable) {
                                Log.e("app", "requirements error: " + throwable);
                                return null;
                            }
                        });

        PointsBuilder pointsBuilder = new PointsBuilder(this, "json/itemsData.json");
        installations = pointsBuilder.getPointsList();
        for(VisitPoint vp : installations) System.out.println(vp.toString());

        itemsViewer=(ViewPager)findViewById(R.id.itemsViewer);
        itemsViewer.setAdapter(new ItemsAdapter(this, installations));

    }





    @Override
    public void onEnterZone(ProximityZoneContext s){
        System.out.println(">>>>> ENTERING...: ");
        printContext(s);
        createVisitZone(s);

    }

    void createVisitZone(ProximityZoneContext s){
//        String zoneId = s.getAttachments().get("beaconId");
//        if(visitPointsNames.contains(zoneId)){
//            visitPointsNames.remove(visitPointsNames.indexOf(zoneId));
//            installations.add(new VisitPoint());
//            System.out.println("xxxx creo la zona...: " + zoneId);
//        }
    }

    @Override
    public void onExitZone(ProximityZoneContext s){
//        System.out.println("<<<< EXITING...: ");
//        System.out.println(s);
    }

    @Override
    public void onContextChange(ArrayList<ProximityZoneContext> pzc){
//        System.out.println("|||| something change...: ");
//        for (ProximityZoneContext p : pzc) {
//            printContext(p);
//            createVisitZone(p);
//        }
    }


    @Override
    public void onEnterCloseZone(ProximityZoneContext s){
        // si esta la zona (point) creado lo activo...
        String zoneId = s.getAttachments().get("beaconId");
        System.out.println("ESTOY REALMENTE CERCA...");
        if(installations.contains(zoneId)){
            System.out.println("habilito la zona: " +zoneId);
        };
    }



    private void printContext(ProximityZoneContext context){


        for (String name: context.getAttachments().keySet()){
            String key = name.toString();
            String value = context.getAttachments().get(name).toString();
            System.out.println(key + " " + value);
        }
    }
    private void startProximityContentManager() {
        proximityContentManager = new ProximityContentManager(this, cloudCredentials);
        proximityContentManager.setOnEnter(this);
        proximityContentManager.start();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (proximityContentManager != null)
            proximityContentManager.stop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
