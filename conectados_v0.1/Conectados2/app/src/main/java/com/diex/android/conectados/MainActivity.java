package com.diex.android.conectados;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.Toolbar;

import com.diex.android.conectados.estimote.ProximityContentManager;
import com.diex.android.conectados.estimote.VisitPoint;
import com.estimote.mustard.rx_goodness.rx_requirements_wizard.Requirement;
import com.estimote.mustard.rx_goodness.rx_requirements_wizard.RequirementsWizardFactory;
import com.estimote.proximity_sdk.api.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;

public class MainActivity extends AppCompatActivity implements Visitable{

    public EstimoteCloudCredentials cloudCredentials =
            new EstimoteCloudCredentials("aboiledtiger-gmail-com-s-p-bux", "78c1cf864c4fb4f63e3758f9697aefdd");

    private ProximityContentManager proximityContentManager;

    ViewPager itemsViewer;
    ArrayList<VisitPoint> installations;
    VisitController visitController;
    String uniqueID = UUID.randomUUID().toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);


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

        PointsBuilder pointsBuilder = new PointsBuilder(this, "json/itemsDataFull.json");
        installations = pointsBuilder.getPointsList();

        itemsViewer = findViewById(R.id.itemsViewer);
        itemsViewer.setAdapter(new ItemsAdapter(this, installations));

        visitController = new VisitController(this);
        visitController.setInstallationsToMonitor(installations);


//        messenger.connectToServer();

    }

    private void startProximityContentManager() {
        proximityContentManager = new ProximityContentManager(this, cloudCredentials);
        proximityContentManager.setOnEnter(this);
        proximityContentManager.start();

    }


    public void setCurrentItem(VisitPoint vp){
        System.out.println("------- set current item: ---------");
//        System.out.println(vp);
        System.out.println(installations.indexOf(vp));
//        itemsViewer.getAdapter().notifyDataSetChanged();
        itemsViewer.setCurrentItem(installations.indexOf(vp));

        final Messenger messenger = new Messenger(this);
        messenger.connectToServer();;
        messenger.setSesion(uniqueID);
        messenger.setGameId(vp.getId());
        messenger.execute();

    }




    @Override
    public void onEnterZone(ProximityZoneContext s){

    }


    @Override
    public void onExitZone(ProximityZoneContext s){
    }

    @Override
    public void onContextChange(ArrayList<ProximityZoneContext> pzc){
        visitController.onContextChanged(pzc);
    }


    private void updateCheckboxes(){
        for(VisitPoint point : installations){
            if(point.getId().equals("game_0")) continue;
            CheckBox checkBox = (CheckBox) findViewById(getResId(point.getId(), R.id.class));
            checkBox.setChecked(point.isActive());
        }
    }

    public static int getResId(String resName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }


    @Override
    public void onEnterCloseZone(ProximityZoneContext s){
    }



    private void printContext(ProximityZoneContext context){
        for (String name: context.getAttachments().keySet()){
            String key = name.toString();
            String value = context.getAttachments().get(name).toString();
            System.out.println(key + " " + value);
        }
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

        if (id == R.id.action_finish) {
            setCurrentItem(installations.get((int) (Math.random()*installations.size())));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}