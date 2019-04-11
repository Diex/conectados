package com.diex.android.conectados;

import com.estimote.proximity_sdk.api.ProximityZoneContext;

import java.util.ArrayList;


public interface Visitable {

        void onEnterZone(ProximityZoneContext s);
        void onExitZone(ProximityZoneContext s);
        void onContextChange(ArrayList<ProximityZoneContext> pzc);
        void onEnterCloseZone(ProximityZoneContext s);
}
