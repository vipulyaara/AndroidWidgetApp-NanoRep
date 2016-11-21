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

//        String _accountName = "sales";//"nanorep";//"nanorep";
//        String _kb = "en";//"English";//"English";

        String _accountName = "nanorep";//"nanorep";
        String _kb = "English";//"English";

//        if(NRImpl.getInstance() != null) {
//            NRImpl.getInstance().reset();
//        }
        NRImpl.getInstance().init(getApplicationContext(), _accountName, _kb);
        Fabric.with(this, new Crashlytics());


//        Nanorep.
    }
}
