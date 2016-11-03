package nanorep.nanowidget.interfaces;

import com.nanorep.nanoclient.Response.NRFAQGroupItem;

import java.util.ArrayList;

import nanorep.nanowidget.DataClasse.NRResult;


/**
 * Created by noat on 03/11/2016.
 */

public interface NRConfigFetcherListener {
    void onConfigurationReady();
    void insertRows(ArrayList<NRFAQGroupItem> groups);
}
