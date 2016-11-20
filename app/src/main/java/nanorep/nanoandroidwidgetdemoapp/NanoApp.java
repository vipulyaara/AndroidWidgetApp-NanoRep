package nanorep.nanoandroidwidgetdemoapp;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.nanorep.nanoclient.NRImpl;
import com.nanorep.nanoclient.Nanorep;

import java.io.Serializable;

import io.fabric.sdk.android.Fabric;


/**
 * Created by nissimpardo on 06/06/16.
 */
public class NanoApp extends Application{
    @Override
    public void onCreate() {
        super.onCreate();

        String _accountName = "gett";//"nanorep";
        String _kb = "English_IL";//"English";

        NRImpl.init(getApplicationContext(), _accountName, _kb);
        Fabric.with(this, new Crashlytics());


//        Nanorep.
    }
}
