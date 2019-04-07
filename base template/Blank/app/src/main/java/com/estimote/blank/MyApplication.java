package com.estimote.blank;

import android.app.Application;

import com.estimote.proximity_sdk.api.EstimoteCloudCredentials;

//
// Running into any issues? Drop us an email to: contact@estimote.com
//

public class MyApplication extends Application {

    EstimoteCloudCredentials estimoteCloudCredentials =
            new EstimoteCloudCredentials("aboiledtiger-gmail-com-s-u-eln", "3dd1244344e48116f1b21d65f083281a");

}
