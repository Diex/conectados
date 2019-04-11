package com.estimote.proximity;

import android.app.Application;

import com.estimote.proximity_sdk.api.EstimoteCloudCredentials;

//
// Running into any issues? Drop us an email to: contact@estimote.com
//

public class MyApplication extends Application {

    public EstimoteCloudCredentials cloudCredentials =
            new EstimoteCloudCredentials("aboiledtiger-gmail-com-s-p-bux", "78c1cf864c4fb4f63e3758f9697aefdd");
}
