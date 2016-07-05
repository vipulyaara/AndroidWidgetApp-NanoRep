package nanorep.nanoandroidwidgetdemoapp;

import android.app.Application;

import NanoRep.NanoRep;

/**
 * Created by nissimpardo on 06/06/16.
 */
public class NanoApp extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        NanoRep.initializeNanorep(getApplicationContext(), "fxcm-asia" ,"my.nanorep.com");
    }
}
