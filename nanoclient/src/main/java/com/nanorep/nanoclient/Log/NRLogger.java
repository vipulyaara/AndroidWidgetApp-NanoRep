package com.nanorep.nanoclient.Log;

import android.util.Log;

/**
 * Created by nanorep on 27/09/2016.
 */
public class NRLogger {

    private boolean debug = false;

    public NRLogger() {
    }

    public void log(String tagResponse, String msg){
        Log.d(tagResponse, msg);
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }
}
