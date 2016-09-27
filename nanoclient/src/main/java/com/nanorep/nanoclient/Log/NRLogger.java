package com.nanorep.nanoclient.Log;

import android.util.Log;

/**
 * Created by nanorep on 27/09/2016.
 */
public class NRLogger {
    private static NRLogger ourInstance = new NRLogger();

    private boolean debug = false;


    public static NRLogger getInstance() {
        return ourInstance;
    }

    private NRLogger() {
    }

    public void log(String tagResponse, String msg){
        Log.d(tagResponse, msg);
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }
}
