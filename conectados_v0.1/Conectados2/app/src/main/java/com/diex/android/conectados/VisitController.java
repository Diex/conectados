package com.diex.android.conectados;

import android.app.Activity;
import android.content.Context;

public class VisitController {

    Activity app;
    Thread t;

    public VisitController(Activity a){
        this.app = a;
        t = new Thread(){
            @Override
            public void run(){
                while(!isInterrupted()){
                    try {
                        Thread.sleep(1000);
                        inferAction();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        };

    }


    void inferAction(){

    }

    public void onContextChanged()

}
