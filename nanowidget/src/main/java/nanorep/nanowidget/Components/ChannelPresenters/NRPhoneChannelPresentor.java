package nanorep.nanowidget.Components.ChannelPresenters;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;

import com.nanorep.nanoclient.Channeling.NRChanneling;
import com.nanorep.nanoclient.Channeling.NRChannelingPhoneNumber;

import nanorep.nanowidget.DataClasse.NRResult;

/**
 * Created by nissimpardo on 26/06/16.
 */
public class NRPhoneChannelPresentor implements NRChannelPresentor {
    private NRChanneling mChanneling;
    private Context mContext;

    public NRPhoneChannelPresentor(Context context) {
        mContext = context;
    }

    @Override
    public void present() {
        Intent intent = new Intent(Intent.ACTION_CALL);

        intent.setData(Uri.parse("tel:" + ((NRChannelingPhoneNumber) mChanneling).getPhoneNumber()));
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext,
                    Manifest.permission.CALL_PHONE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions((Activity) mContext,
                        new String[]{Manifest.permission.CALL_PHONE},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
            return;
        }
        mContext.startActivity(intent);
    }

    @Override
    public void setChannel(NRChanneling channeling) {
        mChanneling = channeling;
    }

    @Override
    public NRResult getResult() {
        return null;
    }
}
