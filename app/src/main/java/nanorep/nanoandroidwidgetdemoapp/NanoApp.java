package nanorep.nanoandroidwidgetdemoapp;

import android.app.Application;

import com.nanorep.nanoclient.NRImpl;
import com.nanorep.nanoclient.Nanorep;

import java.io.Serializable;


/**
 * Created by nissimpardo on 06/06/16.
 */
public class NanoApp extends Application{
    @Override
    public void onCreate() {
        super.onCreate();

        String _accountName = "qa";//"nanorep";
        String _kb = "qa";//"English";

        NRImpl.init(getApplicationContext(), _accountName, _kb);
//        Nanorep.
    }
}
